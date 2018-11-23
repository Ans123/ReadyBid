package net.readybid.rfp.contact;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.rfp.company.RfpCompany;
import org.bson.types.ObjectId;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class RfpContactImpl implements RfpContact {

    private ObjectId id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private RfpCompany company;
    private String phone;
    private String jobTitle;
    private boolean isUser;

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setCompany(RfpCompany company) {
        this.company = company;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public ObjectId getId() {
        return id;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public String getCompanyName() {
        return company == null ? null : company.getName();
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public RfpCompany getCompany() {
        return company;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setIsUser(boolean isEditable) {
        this.isUser = isEditable;
    }

    public boolean isUser() {
        return isUser;
    }

    @Override
    public ObjectId getCompanyAccountId() {
        return company == null ? null : company.getAccountId();
    }

    @Override
    public EntityType getCompanyType() {
        return company == null ? null : company.getType();
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        return new InternetAddress(emailAddress, fullName);
    }
}
