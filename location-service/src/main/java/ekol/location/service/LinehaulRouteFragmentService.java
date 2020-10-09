package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.*;
import ekol.location.repository.LinehaulRouteFragmentRepository;
import ekol.location.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinehaulRouteFragmentService {

    @Autowired
    private LinehaulRouteFragmentRepository linehaulRouteFragmentRepository;

    @Autowired
    private LinehaulRouteService linehaulRouteService;

    @Autowired
    private LinehaulRouteLegService linehaulRouteLegService;

    public LinehaulRouteFragment findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteFragment persistedEntity = linehaulRouteFragmentRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRouteFragment with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public LinehaulRouteFragment createOrUpdate(LinehaulRouteFragment fragment) {

        if (fragment == null) {
            throw new BadRequestException("LinehaulRouteFragment cannot be null.");
        }

        if (fragment.getId() != null) {
            findByIdOrThrowResourceNotFoundException(fragment.getId());
        }

        if (fragment.getParent() == null || fragment.getParent().getId() == null) {
            throw new BadRequestException("LinehaulRouteFragment.parent.id cannot be null.");
        }

        LinehaulRoute parent = linehaulRouteService.findByIdOrThrowResourceNotFoundException(fragment.getParent().getId());
        fragment.setParent(parent);

        if (fragment.getOrderNo() == null) {
            throw new BadRequestException("LinehaulRouteFragment.orderNo cannot be null.");
        }

        ServiceUtils.ensureValueIsGreaterThanZero(fragment.getOrderNo(), "LinehaulRouteFragment.orderNo");

        if (fragment.getType() == null) {
            throw new BadRequestException("LinehaulRouteFragment.type cannot be null.");
        }

        // Yeni bir LinehaulRouteFragmentType eklenirse haberdar olmamız lazım. Aşağıda bazı yerlerde LinehaulRouteFragmentType'a göre işlem yapılıyor.
        if (!fragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED) && !fragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
            throw new BadRequestException("No implementation for " + fragment.getType());
        }

        if (fragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {

            if (fragment.getLeg() == null || fragment.getLeg().getId() == null) {
                throw new BadRequestException("LinehaulRouteFragment.leg.id cannot be null.");
            }

            LinehaulRouteLeg leg = linehaulRouteLegService.findByIdOrThrowResourceNotFoundException(fragment.getLeg().getId());
            fragment.setLeg(leg);

            LinehaulRouteFragment fragmentByParentIdAndLegId = linehaulRouteFragmentRepository.findByParentIdAndLegId(parent.getId(), leg.getId());

            if (fragmentByParentIdAndLegId != null) {
                if (fragment.getId() == null || !fragment.getId().equals(fragmentByParentIdAndLegId.getId())) {
                    throw new BadRequestException("There is already one LinehaulRouteFragment with same parent and leg: "
                            + parent.getId() + ", " + leg.getId());
                }
            }

            fragment.setRoute(null);
            fragment.setFrom(leg.getFrom());
            fragment.setTo(leg.getTo());

        } else if (fragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {

            if (fragment.getRoute() == null || fragment.getRoute().getId() == null) {
                throw new BadRequestException("LinehaulRouteFragment.route.id cannot be null.");
            }

            LinehaulRoute route = linehaulRouteService.findByIdOrThrowResourceNotFoundException(fragment.getRoute().getId());

            if (route.getId().equals(parent.getId())) {
                throw new BadRequestException("A route cannot contain itself.");
            }

            fragment.setRoute(route);

            LinehaulRouteFragment fragmentByParentIdAndRouteId = linehaulRouteFragmentRepository.findByParentIdAndRouteId(parent.getId(), route.getId());

            if (fragmentByParentIdAndRouteId != null) {
                if (fragment.getId() == null || !fragment.getId().equals(fragmentByParentIdAndRouteId.getId())) {
                    throw new BadRequestException("There is already one LinehaulRouteFragment with same parent and route: "
                            + parent.getId() + ", " + route.getId());
                }
            }

            fragment.setLeg(null);
            fragment.setFrom(route.getFrom());
            fragment.setTo(route.getTo());
        }

        if (fragment.getOrderNo() > 1) {

            LinehaulRouteFragment previousFragment = linehaulRouteFragmentRepository.findByParentIdAndOrderNo(parent.getId(), fragment.getOrderNo() - 1);

            // Bir önceki fragment'ın to'su ile bunun from'u aynı olmalı.
            if (previousFragment != null) {

                LinehaulRouteLegStop toOfPrevious = null;
                LinehaulRouteLegStop fromOfCurrent = null;

                if (previousFragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
                    toOfPrevious = previousFragment.getLeg().getTo();
                } else if (previousFragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
                    toOfPrevious = previousFragment.getRoute().getTo();
                }

                if (fragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
                    fromOfCurrent = fragment.getLeg().getFrom();
                } else if (fragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
                    fromOfCurrent = fragment.getRoute().getFrom();
                }

                if (!toOfPrevious.getId().equals(fromOfCurrent.getId())) {
                    throw new BadRequestException("LinehaulRouteFragment.from must be the same as previous LinehaulRouteFragment.to: " + fragment.getOrderNo());
                }
            }
        }

        LinehaulRouteFragment nextFragment = linehaulRouteFragmentRepository.findByParentIdAndOrderNo(parent.getId(), fragment.getOrderNo() + 1);

        // Bir sonraki fragment'ın from'u ile bunun to'su aynı olmalı.
        if (nextFragment != null) {

            LinehaulRouteLegStop fromOfNext = null;
            LinehaulRouteLegStop toOfCurrent = null;

            if (nextFragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
                fromOfNext = nextFragment.getLeg().getFrom();
            } else if (nextFragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
                fromOfNext = nextFragment.getRoute().getFrom();
            }

            if (fragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
                toOfCurrent = fragment.getLeg().getTo();
            } else if (fragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
                toOfCurrent = fragment.getRoute().getTo();
            }

            if (!toOfCurrent.getId().equals(fromOfNext.getId())) {
                throw new BadRequestException("LinehaulRouteFragment.to must be the same as next LinehaulRouteFragment.from: " + fragment.getOrderNo());
            }
        }

        return linehaulRouteFragmentRepository.save(fragment);
    }

    @Transactional
    public void softDelete(Long id) {
        LinehaulRouteFragment persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        linehaulRouteFragmentRepository.save(persistedEntity);
    }
}
