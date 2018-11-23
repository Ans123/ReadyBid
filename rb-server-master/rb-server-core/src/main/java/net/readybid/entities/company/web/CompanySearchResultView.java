package net.readybid.entities.company.web;

import net.readybid.entities.company.core.Company;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class CompanySearchResultView implements ViewModel<Company> {

    public static final ViewModelFactory<Company, CompanySearchResultView> FACTORY = CompanySearchResultView::new;

    public String id;
    public String name;
    public String fullAddress;

    public CompanySearchResultView(Company company) {
        id = company.getId().toString();
        name = company.getName();
        fullAddress = company.getFullAddress();
    }

    public CompanySearchResultView() {}
}
