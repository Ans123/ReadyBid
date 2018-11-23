package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.RateLoadingInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UpdateEntityRateLoadingInformationRequest {

    @NotNull
    @Size(min=1, max=10)
    @Valid
    public List<RateLoadingInformationRequest> information;

    public List<RateLoadingInformation> getInformation() {
        final List<RateLoadingInformation> list = new ArrayList<>();

        for(RateLoadingInformationRequest infoRequest : information){
            list.add(infoRequest.get());
        }
        return list;
    }
}
