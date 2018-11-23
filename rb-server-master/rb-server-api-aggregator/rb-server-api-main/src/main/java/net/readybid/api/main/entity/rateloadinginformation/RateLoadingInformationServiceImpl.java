package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.core.EntityService;
import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.entities.rfp.RateLoadingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLoadingInformationServiceImpl implements RateLoadingInformationService {

    private final RateLoadingInformationRepository repository;
    private final EntityService entityService;

    @Autowired
    public RateLoadingInformationServiceImpl(
            RateLoadingInformationRepository repository,
            EntityService entityService) {
        this.repository = repository;
        this.entityService = entityService;
    }

    @Override
    public EntityRateLoadingInformation get(String entityId) {
        return repository.getByEntityId(entityId);
    }

    @Override
    public void update(String entityId, UpdateEntityRateLoadingInformationRequest updateInformationRequest) {
        final List<RateLoadingInformation> information = updateInformationRequest.getInformation();
        final Entity entity = entityService.getEntityIncludingUnverified(entityId);
        repository.update(entityId, entity.getType(), information);
    }
}
