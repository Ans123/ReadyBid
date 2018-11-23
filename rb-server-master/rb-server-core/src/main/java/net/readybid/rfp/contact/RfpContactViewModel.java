package net.readybid.rfp.contact;

import net.readybid.rfp.company.RfpCompanyViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/8/2017.
 * 
 */
public class RfpContactViewModel implements ViewModel<RfpContact> {

    public static final ViewModelFactory<RfpContact, RfpContactViewModel> FACTORY = RfpContactViewModel::new;

    public String id;
    public String firstName;
    public String lastName;
    public String fullName;
    public String emailAddress;
    public RfpCompanyViewModel company;
    public String phone;
    public String jobTitle;
    public boolean isUser;

    public RfpContactViewModel(RfpContact rfpContact) {
        id = String.valueOf(rfpContact.getId());
        firstName = rfpContact.getFirstName();
        lastName = rfpContact.getLastName();
        fullName = rfpContact.getFullName();
        emailAddress = rfpContact.getEmailAddress();
        company = RfpCompanyViewModel.FACTORY.createAsPartial(rfpContact.getCompany());
        phone = rfpContact.getPhone();
        jobTitle = rfpContact.getJobTitle();
        isUser = rfpContact.isUser();
    }
}
