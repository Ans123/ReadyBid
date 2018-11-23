package net.readybid.web;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.user.BasicUserDetailsViewModel;
import net.readybid.utils.StatusDetails;

import java.util.Date;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class StatusDetailsViewModel implements ViewModel<StatusDetails<?>> {

    public static final ViewModelFactory<StatusDetails<?>, StatusDetailsViewModel> FACTORY = StatusDetailsViewModel::new;
    public Date at;
    public String value;
    public BasicUserDetailsViewModel by;

    public StatusDetailsViewModel(StatusDetails<?> statusDetails) {
        if(statusDetails == null) throw new NotFoundException();
        at = statusDetails.getAt();
        value = String.valueOf(statusDetails.getValue());
        by = BasicUserDetailsViewModel.FACTORY.createAsPartial(statusDetails.getBy());
    }
}
