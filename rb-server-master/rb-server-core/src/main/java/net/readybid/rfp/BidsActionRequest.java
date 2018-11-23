package net.readybid.rfp;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by DejanK on 2/5/2017.
 *
 */
public class BidsActionRequest {

    @NotNull
    @NotEmpty
    public List<String> bids;
}
