package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.location.domain.LinehaulRouteLeg;
import ekol.location.domain.LinehaulRouteLegSchedule;
import ekol.location.domain.RouteLegExpedition;
import ekol.location.domain.RouteLegExpeditionStatus;
import ekol.location.repository.LinehaulRouteLegRepository;
import ekol.location.repository.RouteLegExpeditionRepository;
import ekol.location.repository.specs.RouteLegSpecifications;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by burak on 11/12/17.
 */
@Service
public class RouteLegExpeditionService {

    private static Logger logger =  LoggerFactory.getLogger(RouteLegExpeditionService.class);

    @Autowired
    private LinehaulRouteLegRepository linehaulRouteLegRepository;

    @Autowired
    private RouteLegExpeditionRepository routeLegExpeditionRepository;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private static final String COUNTER_KEY = "locationService.routeLegExpedition.counter";

    private RedisAtomicLong expeditionCodeCounter;

    private static final int EXPEDITION_GENERATION_DAYS = 14;

    @PostConstruct
    public void init() {
        /**
         * Dikkat: initialValue alan RedisAtomicLong constructor'ını kullanırsak
         * uygulama her açıldığında, sayaç initialValue değerinden başlıyor.
         * Bizim istediğimiz, uygulama her açıldığında sayacın kaldığı yerden devam etmesi.
         */
        expeditionCodeCounter = new RedisAtomicLong(COUNTER_KEY, redisConnectionFactory);
    }

    @Scheduled(cron = "0 0 0 ? * *")
    @Transactional
    public void generateExpeditionsForTwoWeeks() {
        Iterable<LinehaulRouteLeg> routeLegs = linehaulRouteLegRepository.findAll();
        generateExpeditions(routeLegs);
    }

    @Transactional
    public void generateExpeditionsOfRouteLegForTwoWeeks(Long routeLegId) {
        LinehaulRouteLeg routeLeg = linehaulRouteLegRepository.findOne(routeLegId);
        generateExpeditions(Arrays.asList(routeLeg));
    }

    private void generateExpeditions(Iterable<LinehaulRouteLeg> routeLegs) {

        ZonedDateTime generationStartDay = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime generationEndDay = generationStartDay.plusDays(EXPEDITION_GENERATION_DAYS);

        ZonedDateTime currentIterationTime = generationStartDay;

        boolean iteratingForCurrentDay = true;

        //Iterate for each day
        while(currentIterationTime.isBefore(generationEndDay)) {

            Integer year = currentIterationTime.getYear();
            Integer day = currentIterationTime.getDayOfYear();
            DayOfWeek dayOfWeek = currentIterationTime.getDayOfWeek();

            Integer iterationKey = year * 1000 + day;

            //Find expeditions created for current iteration Day
            List<RouteLegExpedition> expeditionList = routeLegExpeditionRepository.findByKey(iterationKey);

            //Each Route Leg
            for (LinehaulRouteLeg routeLeg: routeLegs) {

                if (routeLeg.getSchedules() != null) {

                    //Each Schedule of route Leg
                    for (LinehaulRouteLegSchedule schedule : routeLeg.getSchedules()) {

                        try {
                            //if the schedule belongs to current day of week, check/crete expeditions
                            if (schedule.getDepartureDay() == dayOfWeek) {

                                //find existed expeditions of schedule for current day
                                //it is expected to have only 1 or 0 expedition previously created or not)
                                //bussines logic says; "if schedule date is updated, keep the current expedition and create a new expedition for updated time
                                //so if the schedule date is updated, the schedule will have extra expeditions for the following two weeks
                                //that is way we have a list of expeditions for each day
                                List<RouteLegExpedition> existedExpeditions = expeditionList.stream().filter(expedition ->
                                        expedition.getParentSchedule().getId().equals(schedule.getId())).collect(Collectors.toList());

                                boolean createExpedition = false;
                                if (existedExpeditions.size() == 0) {
                                    //if there is no expedition created for this schedule, create a new one
                                    createExpedition = true;
                                } else {
                                    //if exists, check if one of the expeditions have the same time value with schedule
                                    LocalTime scheduleTime = schedule.getDepartureTime();

                                    //ıf there is no expedition time value matching with schedule, that means the schedule is updated; keep previous expedition, create a new one
                                    boolean isExpeditionCreatedForScheduleTime = existedExpeditions.stream().filter(expedition -> {
                                        LocalTime expeditionTime = expedition.getDeparture().getDateTime().toLocalTime();
                                        return scheduleTime.equals(expeditionTime);
                                    }).findFirst().isPresent();

                                    if (!isExpeditionCreatedForScheduleTime) {
                                        createExpedition = true;
                                    }
                                }

                                //if iteration is for today and schedule time is before the iteration time, do not create tariff
                                if(iteratingForCurrentDay) {
                                    if(schedule.getDepartureTime().isBefore(currentIterationTime.toLocalTime())) {
                                        createExpedition = false;
                                    }
                                }

                                if (createExpedition) {
                                    RouteLegExpedition expedition = createExpedition(routeLeg, schedule,
                                            currentIterationTime.toLocalDate(), schedule.getDepartureTime(), iterationKey);
                                    routeLegExpeditionRepository.save(expedition);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error Occured while creating tariffs (schedule id: " + (schedule != null ? schedule.getId() : "-") + ") ",  e);
                        }
                    }
                }
            }
            currentIterationTime = currentIterationTime.plusDays(1);
            iteratingForCurrentDay = false;
        }

    }

    private RouteLegExpedition createExpedition(LinehaulRouteLeg leg, LinehaulRouteLegSchedule schedule,
                                                LocalDate departureDate, LocalTime departureTime,Integer key) {

        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

        RouteLegExpedition expedition = new RouteLegExpedition();

        String code = String.format("%06d", expeditionCodeCounter.incrementAndGet());
        expedition.setCode(RouteLegExpedition.getCodePrefix(leg.getType()) + code);
        expedition.setKey(key);

        expedition.setLinehaulRouteLeg(leg);
        expedition.setParentSchedule(schedule);

        //set to current day
        String fromTimeZone = leg.getFrom().getTimezone() != null ? leg.getFrom().getTimezone() : "UTC";
        FixedZoneDateTime departure = new FixedZoneDateTime(departureDateTime, fromTimeZone);
        expedition.setDeparture(departure);

        String toTimeZone = leg.getTo().getTimezone() != null ? leg.getTo().getTimezone() : "UTC";
        FixedZoneDateTime arrival = new FixedZoneDateTime(departureDateTime.plusMinutes(schedule.getDuration()), toTimeZone);
        expedition.setArrival(arrival);

        expedition.setStatus(RouteLegExpeditionStatus.PLANNING);

        return expedition;
    }

    public List<RouteLegExpedition> findRouteLegExpeditionStartingFromNow(Long routeLegId, String fromLocationTimeZone) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String fromTimeZone = fromLocationTimeZone != null ? fromLocationTimeZone : "UTC";
        FixedZoneDateTime nowWithFromLocTimezone =
                new FixedZoneDateTime(
                        now.withZoneSameInstant(ZoneId.of(fromTimeZone)).toLocalDateTime(),
                        fromTimeZone);

        List<RouteLegExpedition> expeditions =
                routeLegExpeditionRepository.findByLinehaulRouteLegIdAndDepartureDateTimeGreaterThanOrderByDepartureDateTimeAsc(routeLegId, nowWithFromLocTimezone.getDateTime());

        return expeditions;
    }

    public RouteLegExpedition findById(Long expeditionId) {

        RouteLegExpedition expedition = routeLegExpeditionRepository.findOne(expeditionId);

        if(expedition == null) {
            throw new ResourceNotFoundException("Expedition with given id does not exist.");
        }

        return expedition;
    }

    public List<RouteLegExpedition> findByRouteLegId(Long routeLegId, boolean includeHistory, String departureDateFrom, String departureDateTo) {


        if(routeLegId == null) {
            throw new BadRequestException("Missing Route Leg Id");
        }
        Specifications<RouteLegExpedition> specification = Specifications.where(RouteLegSpecifications.havingRouteLeg(routeLegId));

        if(!StringUtils.isEmpty(departureDateFrom)) {
            departureDateFrom += departureDateFrom.length() == 10 ? " 00:00" : "";
            specification = specification.and(RouteLegSpecifications.departureGreaterThanOrEqual(
                    LocalDateTime.parse(departureDateFrom, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            ));
        }

        if(!StringUtils.isEmpty(departureDateTo)) {
            departureDateTo += departureDateTo.length() == 10 ? " 23:59" : "";
            specification = specification.and(RouteLegSpecifications.departureLessThanOrEqual(
                    LocalDateTime.parse(departureDateTo, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            ));
        }

        if(!includeHistory) {
            specification = specification.and(RouteLegSpecifications.notHavingStatus(RouteLegExpeditionStatus.COMPLETED));
        }

        return routeLegExpeditionRepository.findAll(specification);
    }

    public RouteLegExpedition update(RouteLegExpedition routeLegExpedition) {

        if(routeLegExpedition == null) {
            throw new ResourceNotFoundException("Expedition to be updated is null.");
        }

        if(routeLegExpedition.getId() == null) {
            throw new ResourceNotFoundException("Expedition to be updated has no Id.");
        }

        return routeLegExpeditionRepository.save(routeLegExpedition);

    }
}
