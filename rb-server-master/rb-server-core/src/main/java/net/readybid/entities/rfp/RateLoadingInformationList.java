package net.readybid.entities.rfp;

import java.util.List;

public interface RateLoadingInformationList {
    long getRateLoadingInformationSize();

    List<? extends RateLoadingInformation> getRateLoadingInformation();

    RateLoadingInformation getRateLoadingInformation(int i);
}
