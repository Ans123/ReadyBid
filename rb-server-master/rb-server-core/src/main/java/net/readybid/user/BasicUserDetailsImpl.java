package net.readybid.user;

import org.bson.types.ObjectId;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class BasicUserDetailsImpl implements BasicUserDetails {

    protected ObjectId id;
    protected String firstName;
    protected String lastName;
    protected String emailAddress;
    protected String phone;

    public BasicUserDetailsImpl() {}

    public BasicUserDetailsImpl(BasicUserDetails bud) {
        setId(bud.getId());
        setFirstName(bud.getFirstName());
        setLastName(bud.getLastName());
        setEmailAddress(bud.getEmailAddress());
        setPhone(bud.getPhone());
    }

    @Override
    public ObjectId getId() {
        return id;
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
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress == null ? null : emailAddress.toLowerCase();
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        return new InternetAddress(getEmailAddress(), getFullName());
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
