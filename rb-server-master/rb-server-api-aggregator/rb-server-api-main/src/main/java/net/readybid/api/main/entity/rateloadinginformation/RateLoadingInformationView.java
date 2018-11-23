package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

@SuppressWarnings("WeakerAccess")
public class RateLoadingInformationView implements ViewModel<RateLoadingInformation> {

    public static final ViewModelFactory<RateLoadingInformation, RateLoadingInformationView> FACTORY = RateLoadingInformationView::new;

    public String arcIatas;
    public String pseudoCityCode;
    public String gdsName;
    public String rateAccessCode;

    public RateLoadingInformationView(RateLoadingInformation rateLoadingInformation) {
        this.arcIatas = rateLoadingInformation.getArcIatas();
        this.pseudoCityCode = rateLoadingInformation.getPseudoCityCode();
        this.gdsName = rateLoadingInformation.getGdsName();
        this.rateAccessCode = rateLoadingInformation.getRateAccessCode();
    }
}
