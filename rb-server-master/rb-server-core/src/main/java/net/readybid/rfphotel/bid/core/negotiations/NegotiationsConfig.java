package net.readybid.rfphotel.bid.core.negotiations;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 7/25/2017.
 */
public abstract class NegotiationsConfig {

    public abstract String getPrimaryRatePrefix();

    public abstract List<Map<String, Object>> getSeasons();
}
