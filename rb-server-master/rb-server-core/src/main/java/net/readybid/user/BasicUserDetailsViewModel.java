package net.readybid.user;


import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class BasicUserDetailsViewModel implements ViewModel<BasicUserDetails> {

    public static final ViewModelFactory<BasicUserDetails, BasicUserDetailsViewModel> FACTORY = BasicUserDetailsViewModel::new;
    public ObjectId id;
    public String firstName;
    public String lastName;
    public String emailAddress;
    public String phone;

    public BasicUserDetailsViewModel(BasicUserDetails basicUserDetails) {
        id = basicUserDetails.getId();
        firstName = basicUserDetails.getFirstName();
        lastName = basicUserDetails.getLastName();
        emailAddress = basicUserDetails.getEmailAddress();
        phone = basicUserDetails.getPhone();
    }
}
