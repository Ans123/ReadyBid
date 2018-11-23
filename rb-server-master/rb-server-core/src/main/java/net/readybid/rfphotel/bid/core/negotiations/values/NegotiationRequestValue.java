package net.readybid.rfphotel.bid.core.negotiations.values;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by DejanK on 8/10/2017.
 *
 */
public class NegotiationRequestValue {

    @NotNull
    public BigDecimal value;

    @NotNull
    public Boolean valid;

    @NotNull
    public NegotiationValueType type;

    @NotNull
    public Boolean included;
}
