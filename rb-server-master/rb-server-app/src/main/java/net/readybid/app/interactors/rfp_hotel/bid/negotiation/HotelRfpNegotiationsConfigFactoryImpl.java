package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;
import net.readybid.utils.RbMapUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 7/26/2017.
 *
 */
@Service
public class HotelRfpNegotiationsConfigFactoryImpl implements HotelRfpNegotiationsConfigFactory {

    @Override
    public HotelRfpNegotiationsConfig create(Map<String, String> response) {
        final HotelRfpNegotiationsConfig config = new HotelRfpNegotiationsConfig();

        addRatesConfig(config, response);
        config.amenities = readAmenities(response);
        config.currency = readCurrency(response);

        return config;
    }

    private void addRatesConfig(HotelRfpNegotiationsConfig config, Map<String, String> response) {
        final List<String> ratesKeys = readRatesKeys(response);

        final Set<String> rates = new HashSet<>();
        final Set<String> seasons = new HashSet<>();
        final Set<String> roomTypes = new HashSet<>();

        for(String rateKey : ratesKeys) {
            String[] parts = rateKey.split("_");
            rates.add(String.format("%s%s", parts[0].toLowerCase(), parts[3].substring(0,1))); // lraS nlraD
            seasons.add(parts[1].toLowerCase()); // s1, s2....
            roomTypes.add(parts[2].toLowerCase()); // rt1, rt2...
        }

        config.rates = readRates(rates);
        config.occupancies = readOccupancies(config.rates);
        config.seasons = readSeasons(seasons, response);
        config.roomTypes = readRoomTypes(roomTypes, response);
    }

    private List<String> readRates(Set<String> ratesSet) {
        final List<String> rates = new ArrayList<>();
        // to enforce order
        if(ratesSet.contains("lraS")) rates.add("lraS");
        if(ratesSet.contains("lraD")) rates.add("lraD");
        if(ratesSet.contains("nlraS")) rates.add("nlraS");
        if(ratesSet.contains("nlraD")) rates.add("nlraD");

        if(ratesSet.isEmpty()) rates.add("lraS");

        return rates;
    }

    private int readOccupancies(List<String> rates) {
        if(rates.size() == 1) return 1;
        if(rates.size() == 4) return 2;
        final String rate1 = rates.get(0);
        final String rate2 = rates.get(1);
        return rate1.charAt(rate1.length()-1) == rate2.charAt(rate2.length()-1) ? 1 : 2;
    }

    private List<String> readRatesKeys(Map<String, String> response) {
        final Pattern rateKeyPattern = Pattern.compile("^(N)?LRA_S[1-5]_RT[1-3]_(SGL|DBL)$");
        return response.keySet().stream()
                .filter(rateKeyPattern.asPredicate())
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> readSeasons(Set<String> seasons, Map<String, String> response) {
        /**
         * "seasons" : [
         { "id" : 1, "start" : "2017-01-01", "end" : "2017-03-31" }
         ],
         */
        final List<Map<String, Object>> seasonsConfig = new ArrayList<>();
        if(seasons.isEmpty()){
            final Map<String, Object> seasonConfig = new HashMap<>();
            final String seasonId = "1";
            seasonConfig.put("id", RbMapUtils.readInteger(seasonId, 0));
            seasonConfig.put("start", response.getOrDefault(String.format("SEASON%sSTART", seasonId), ""));
            seasonConfig.put("end", response.getOrDefault(String.format("SEASON%sEND", seasonId), ""));
            seasonsConfig.add(seasonConfig);
        } else {
            for (String season : seasons) {
                final Map<String, Object> seasonConfig = new HashMap<>();
                final String seasonId = season.substring(1);
                seasonConfig.put("id", RbMapUtils.readInteger(seasonId, 0));
                seasonConfig.put("start", response.get(String.format("SEASON%sSTART", seasonId)));
                seasonConfig.put("end", response.get(String.format("SEASON%sEND", seasonId)));
                seasonsConfig.add(seasonConfig);
            }
            Collections.sort(seasonsConfig, (m1, m2) -> ((Integer) m1.get("id")).compareTo((Integer) m2.get("id")));
        }
        return seasonsConfig;
    }

    private List<Map<String, Object>> readRoomTypes(Set<String> roomTypes, Map<String, String> response) {
        /**
         * "roomTypes" : [
         { "id" : 1, "name" : "King Size", "count" : 24},
         ]
         */
        final List<Map<String, Object>> roomTypesConfig = new ArrayList<>();
        if(roomTypes.isEmpty()){
            final Map<String, Object> roomTypeConfig = new HashMap<>();
            final String roomTypeId = "1";
            roomTypeConfig.put("id", RbMapUtils.readInteger(roomTypeId, 0));
            roomTypeConfig.put("name", response.getOrDefault(String.format("ROOMTYPE%sDEFINE", roomTypeId), ""));
            roomTypeConfig.put("count", RbMapUtils.readInteger(response.getOrDefault(String.format("ROOMTYPE%sNUMBER", roomTypeId), "0"), 0));
            roomTypesConfig.add(roomTypeConfig);
        } else {
            for(String roomType : roomTypes){
                final Map<String, Object> roomTypeConfig = new HashMap<>();
                final String roomTypeId = roomType.substring(2);
                roomTypeConfig.put("id", RbMapUtils.readInteger(roomTypeId, 0));
                roomTypeConfig.put("name", response.get(String.format("ROOMTYPE%sDEFINE", roomTypeId)));
                roomTypeConfig.put("count", RbMapUtils.readInteger(response.get(String.format("ROOMTYPE%sNUMBER", roomTypeId)), 0));
                roomTypesConfig.add(roomTypeConfig);
            }
            Collections.sort(roomTypesConfig, (m1, m2) -> ((Integer)m1.get("id")).compareTo((Integer)m2.get("id")));
        }
        return roomTypesConfig;
    }


    private List<String> readAmenities(Map<String, String> response) {
        return Arrays.asList("ec", "prk", "bf", "ft", "ia", "wf", "as");
//        final List<String> amenitiesConfig = new ArrayList<>();
//
//        if(shouldAddAmenity(response.get("EARLYCK_FEE"), "0")) amenitiesConfig.add("ec");
//        if(shouldAddAmenity(response.get("PARK_FEE"), "0")) amenitiesConfig.add("prk");
//        amenitiesConfig.add("bf");
//        if(shouldAddAmenity(response.get("FITNESS_FEE_ON"), "0")) amenitiesConfig.add("ft");
//        if(shouldAddAmenity(response.get("HSIA_FEE"), "0")) amenitiesConfig.add("ia");
//        if(shouldAddAmenity(response.get("WIRELESS_FEE"), "0")) amenitiesConfig.add("wf");
//        if(shouldAddAmenity(response.get("AIRTRANS_FEE"), "0")) amenitiesConfig.add("as");
//
//        return amenitiesConfig;
    }

    private boolean shouldAddAmenity(String responseValue, String valueForNotAvailable) {
        return responseValue != null && !responseValue.equalsIgnoreCase(valueForNotAvailable);
    }

    private String readCurrency(Map<String, String> response) {
        final String currency = response.get("RATE_CURR");
        return currency == null || currency.isEmpty() ? "USD" : currency;
    }
}
