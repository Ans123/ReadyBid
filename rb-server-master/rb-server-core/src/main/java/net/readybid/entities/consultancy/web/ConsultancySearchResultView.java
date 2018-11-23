package net.readybid.entities.consultancy.web;

import net.readybid.entities.consultancy.core.Consultancy;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class ConsultancySearchResultView implements ViewModel<Consultancy> {

    public static final ViewModelFactory<Consultancy, ConsultancySearchResultView> FACTORY = ConsultancySearchResultView::new;

    public String id;
    public String name;
    public String fullAddress;

    public ConsultancySearchResultView(Consultancy consultancy) {
        id = consultancy.getId().toString();
        name = consultancy.getName();
        fullAddress = consultancy.getFullAddress();
    }

    public ConsultancySearchResultView() {}
}
