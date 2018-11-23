package net.readybid.rfp.company;


import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.location.AddressView;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpCompanyViewModel implements ViewModel<RfpCompany> {

    public static final ViewModelFactory<RfpCompany, RfpCompanyViewModel> FACTORY = RfpCompanyViewModel::new;

    public ObjectId accountId;
    public ObjectId entityId;
    public String name;
    public EntityType type;
    public EntityIndustry industry;
    public AddressView address;
    public String fullAddress;
    public String website;
    public String logo;
    public String emailAddress;
    public String phone;

    public RfpCompanyViewModel(RfpCompany rfpCompany) {
        accountId = rfpCompany.getAccountId();
        entityId = rfpCompany.getEntityId();
        name = rfpCompany.getName();
        type = rfpCompany.getType();
        industry = rfpCompany.getIndustry();
        address = AddressView.FACTORY.createAsPartial(rfpCompany.getAddress());
        fullAddress = rfpCompany.getFullAddress();
        website = rfpCompany.getWebsite();
        logo = rfpCompany.getLogo();
        emailAddress = rfpCompany.getEmailAddress();
        phone = rfpCompany.getPhone();
    }
}
