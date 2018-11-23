package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.app.core.entities.entity.EntityType;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class NegotiatorImpl implements Negotiator {

    private ObjectId userId;
    private ObjectId userAccountId;
    private ObjectId accountId;
    private EntityType accountType;
    private BidManagerViewSide type;
    private String logo;
    private String companyName;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String profilePicture;
    private String emailAddress;

    public NegotiatorImpl() {}

    public NegotiatorImpl(ObjectId userAccountId, BidManagerViewSide side) {
        this.userAccountId = userAccountId;
        type = side;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(ObjectId userAccountId) {
        this.userAccountId = userAccountId;
    }

    public ObjectId getAccountId() {
        return accountId;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public EntityType getAccountType() {
        return accountType;
    }

    public void setAccountType(EntityType accountType) {
        this.accountType = accountType;
    }

    public String getAccountLogo() {
        return logo;
    }

    public void setAccountLogo(String logo) {
        this.logo = logo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public BidManagerViewSide getType() {
        return type;
    }

    public void setType(BidManagerViewSide type) {
        this.type = type;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
