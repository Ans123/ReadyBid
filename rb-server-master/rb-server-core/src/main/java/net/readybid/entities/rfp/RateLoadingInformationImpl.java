package net.readybid.entities.rfp;

public class RateLoadingInformationImpl implements RateLoadingInformation {

    private String arcIatas = "";
    private String pseudoCityCode = "";
    private String gdsName = "";
    private String rateAccessCode = "";

    @Override
    public String getArcIatas() {
        return arcIatas;
    }

    @Override
    public String getPseudoCityCode() {
        return pseudoCityCode;
    }

    @Override
    public String getGdsName() {
        return gdsName;
    }

    @Override
    public String getRateAccessCode() {
        return rateAccessCode;
    }

    public void setArcIatas(String arcIatas) {
        this.arcIatas = arcIatas;
    }

    public void setGdsName(String gdsName) {
        this.gdsName = gdsName;
    }

    public void setPseudoCityCode(String pseudoCityCode) {
        this.pseudoCityCode = pseudoCityCode;
    }

    public void setRateAccessCode(String rateAccessCode) {
        this.rateAccessCode = rateAccessCode;
    }
}
