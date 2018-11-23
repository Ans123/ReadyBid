package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.EntityRateLoadingInformation;

public interface RateLoadingInformationFacade {
    EntityRateLoadingInformation get(String entityId);

    void update(String entityId, UpdateEntityRateLoadingInformationRequest updateInformationRequest);
}
