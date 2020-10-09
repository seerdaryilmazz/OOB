package ekol.location.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.*;
import ekol.location.client.KartoteksServiceClient;
import ekol.location.client.dto.Company;
import ekol.location.domain.*;
import ekol.location.repository.LinehaulRouteLegNonScheduleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LinehaulRouteLegNonScheduleService {

    private LinehaulRouteLegNonScheduleRepository linehaulRouteLegNonScheduleRepository;
    private LinehaulRouteLegService linehaulRouteLegService;
    private KartoteksServiceClient kartoteksServiceClient;

    public LinehaulRouteLegNonSchedule findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLegNonSchedule persistedEntity = linehaulRouteLegNonScheduleRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Linehaul route leg non-schedule with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public LinehaulRouteLegNonSchedule createOrUpdate(LinehaulRouteLegNonSchedule nonSchedule) {

        if (nonSchedule == null) {
            throw new BadRequestException("A linehaul route leg non-schedule must be specified.");
        }

        if (nonSchedule.getId() != null) {
            findByIdOrThrowResourceNotFoundException(nonSchedule.getId());
        }

        if (nonSchedule.getParent() == null || nonSchedule.getParent().getId() == null) {
            throw new BadRequestException("A linehaul route leg must be specified.");
        }

        LinehaulRouteLeg parent = linehaulRouteLegService.findByIdOrThrowResourceNotFoundException(nonSchedule.getParent().getId());
        nonSchedule.setParent(parent);

        if (nonSchedule.getCompanyId() != null) {
            Company company = kartoteksServiceClient.getCompany(nonSchedule.getCompanyId());
            nonSchedule.setCompanyName(company.getName());
        } else {
            nonSchedule.setCompanyName(null);
        }

        if (nonSchedule.getDuration() == null) {
            throw new BadRequestException("A duration must be specified.");
        }

        return linehaulRouteLegNonScheduleRepository.save(nonSchedule);
    }

    @Transactional
    public void softDelete(Long id) {
        LinehaulRouteLegNonSchedule persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        linehaulRouteLegNonScheduleRepository.save(persistedEntity);
    }
}
