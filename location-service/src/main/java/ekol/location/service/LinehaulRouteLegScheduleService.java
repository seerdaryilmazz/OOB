package ekol.location.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.*;
import ekol.location.client.KartoteksServiceClient;
import ekol.location.client.dto.Company;
import ekol.location.domain.*;
import ekol.location.repository.LinehaulRouteLegScheduleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LinehaulRouteLegScheduleService {
    
	private LinehaulRouteLegScheduleRepository linehaulRouteLegScheduleRepository;
    private LinehaulRouteLegService linehaulRouteLegService;
    private KartoteksServiceClient kartoteksServiceClient;

    public LinehaulRouteLegSchedule findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLegSchedule persistedEntity = linehaulRouteLegScheduleRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Linehaul route leg schedule with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public LinehaulRouteLegSchedule createOrUpdate(LinehaulRouteLegSchedule schedule) {

        if (schedule == null) {
            throw new BadRequestException("A linehaul route leg schedule must be specified.");
        }

        if (schedule.getId() != null) {
            findByIdOrThrowResourceNotFoundException(schedule.getId());
        }

        if (schedule.getParent() == null || schedule.getParent().getId() == null) {
            throw new BadRequestException("A linehaul route leg must be specified.");
        }

        LinehaulRouteLeg parent = linehaulRouteLegService.findByIdOrThrowResourceNotFoundException(schedule.getParent().getId());
        schedule.setParent(parent);

        if (schedule.getCompanyId() != null) {
            Company company = kartoteksServiceClient.getCompany(schedule.getCompanyId());
            schedule.setCompanyName(company.getName());
        } else {
            schedule.setCompanyName(null);
        }

        if (parent.getType().equals(RouteLegType.SEAWAY)) {

            if (StringUtils.isBlank(schedule.getFerryName())) {
                throw new BadRequestException("A ferry name must be specified.");
            }

            schedule.setFerryName(schedule.getFerryName().trim().toUpperCase());

        } else {
            schedule.setFerryName(null);
        }

        if (schedule.getDepartureDay() == null) {
            throw new BadRequestException("A departure day must be specified.");
        }

        if (schedule.getDepartureTime() == null) {
            throw new BadRequestException("A departure time must be specified.");
        }

        if (schedule.getDuration() == null) {
            throw new BadRequestException("A duration must be specified.");
        }

        return linehaulRouteLegScheduleRepository.save(schedule);
    }

    @Transactional
    public void softDelete(Long id) {
        LinehaulRouteLegSchedule persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        linehaulRouteLegScheduleRepository.save(persistedEntity);
    }
}
