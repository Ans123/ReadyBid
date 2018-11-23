package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.EntityRateLoadingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLoadingInformationFacadeImpl implements RateLoadingInformationFacade {

    private final RateLoadingInformationService rateLoadingInformationService;

    @Autowired
    public RateLoadingInformationFacadeImpl(RateLoadingInformationService rateLoadingInformationService) {
        this.rateLoadingInformationService = rateLoadingInformationService;
    }

    @Override
    public EntityRateLoadingInformation get(String entityId) {
        return rateLoadingInformationService.get(entityId);
    }

    @Override
    public void update(String entityId, UpdateEntityRateLoadingInformationRequest updateInformationRequest) {
        // todo: add Permissions Check
        rateLoadingInformationService.update(entityId, updateInformationRequest);
    }
}
