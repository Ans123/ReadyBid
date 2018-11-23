package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationRequestValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * Created by DejanK on 7/27/2017.
 *
 */
public class NegotiationRequest {

    @NotNull
    public Map<String, NegotiationRequestValue> rates;

    @NotNull
    public Map<String, NegotiationRequestValue> amenities;

    @Size(max = 10000)
    public String message;
}
