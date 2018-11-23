package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.entities.rfp.RateLoadingInformationImpl;

import javax.validation.constraints.Size;

public class RateLoadingInformationRequest {

    @Size(max = 50)
    public String arcIatas;

    @Size(max = 50)
    public String pseudoCityCode;

    @Size(max = 50)
    public String gdsName;

    @Size(max = 50)
    public String rateAccessCode;

    public RateLoadingInformation get() {
        final RateLoadingInformationImpl info = new RateLoadingInformationImpl();
        info.setArcIatas(arcIatas);
        info.setPseudoCityCode(pseudoCityCode);
        info.setGdsName(gdsName);
        info.setRateAccessCode(rateAccessCode);
        return info;
    }
}
