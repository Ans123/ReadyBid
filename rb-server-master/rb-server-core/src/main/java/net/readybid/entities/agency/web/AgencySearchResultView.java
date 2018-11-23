package net.readybid.entities.agency.web;

import net.readybid.entities.agency.core.Agency;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class AgencySearchResultView implements ViewModel<Agency> {

    public static final ViewModelFactory<Agency, AgencySearchResultView> FACTORY = AgencySearchResultView::new;

    public String id;
    public String name;
    public String fullAddress;

    public AgencySearchResultView(Agency agency) {
        id = agency.getId().toString();
        name = agency.getName();
        fullAddress = agency.getFullAddress();
    }

    public AgencySearchResultView() {}
}
