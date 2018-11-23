package net.readybid.mongodb;

import net.readybid.user.BasicUserDetailsViewModel;
import net.readybid.utils.CreationDetails;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.Date;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class CreationDetailsViewModel implements ViewModel<CreationDetails> {

    public static final ViewModelFactory<CreationDetails, CreationDetailsViewModel> FACTORY = CreationDetailsViewModel::new;
    public Date at;
    public BasicUserDetailsViewModel by;

    public CreationDetailsViewModel(CreationDetails creationDetails) {
        at = creationDetails.getAt();
        by = BasicUserDetailsViewModel.FACTORY.createAsPartial(creationDetails.getBy());
    }
}
