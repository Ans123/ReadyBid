package net.readybid.auth.account.web;

import net.readybid.FilePaths;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountStatus;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.location.LocationViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/29/2017.
 *
 */
public class AccountViewModel implements ViewModel<Account>  {

    public static final ViewModelFactory<Account, AccountViewModel> FACTORY = AccountViewModel::new;

    public ObjectId id;
    public ObjectId entityId;
    public String type;
    public String name;
    public EntityIndustry industry;
    public LocationViewModel location;
    public String website;
    public String logo;
    public String emailAddress;
    public String phone;
    public AccountStatus status;
    public long changed;

    public AccountViewModel(Account account) {
        id = account.getId();
        entityId = account.getEntityId();
        type = account.getType() == null ? null : String.valueOf(account.getType());
        name = account.getName();
        industry = account.getIndustry();
        location = LocationViewModel.FACTORY.createAsPartial(account.getLocation());
        website = account.getWebsite();
        logo = getLogoWithPath(account.getLogo());
        emailAddress = account.getEmailAddress();
        phone = account.getPhone();
        status = account.getStatusValue();
        changed = account.getLastChangeTimestamp();
    }

    private String getLogoWithPath(String logo) {
        return logo == null || logo.isEmpty() ? null : FilePaths.LOGO + logo;
    }

}
