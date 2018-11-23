package net.readybid.rfphotel.bid.core.negotiations;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class HotelRfpNegotiationsConfig extends NegotiationsConfig {

    public List<Map<String, Object>> seasons;
    public List<Map<String, Object>> roomTypes;
    public List<String> rates;
    public List<String> amenities;
    public String currency;
    public int occupancies;

    @Override
    public String getPrimaryRatePrefix() {
        return rates.get(0);
    }

    @Override
    public List<Map<String, Object>> getSeasons() {
        return seasons;
    }
}
