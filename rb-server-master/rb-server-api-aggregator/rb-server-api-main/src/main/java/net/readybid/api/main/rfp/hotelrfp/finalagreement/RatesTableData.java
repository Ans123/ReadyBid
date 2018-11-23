package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.api.main.rfp.letter.AnswersHelper;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
class RatesTableData {

    private static final String OCCUPANCY_SINGLE = "SGL";
    private static final String OCCUPANCY_SINGLE_LABEL = "Single";
    private static final String OCCUPANCY_DOUBLE = "DBL";
    private static final String OCCUPANCY_DOUBLE_LABEL = "Double";

    private static final String SEASON_START_PATTERN = "SEASON%sSTART";
    private static final String SEASON_END_PATTERN = "SEASON%sEND";
    private static final String RATE_KEY_PATTERN = "%s_S%s_RT%s_%s";

    public boolean dynamicExists;
    public String dynamicAmount;

    public List<String> rateNames = new ArrayList<>();
    public Set<String> occupancies = new LinkedHashSet<>();
    public List<String> occupancyNames = new ArrayList<>();
    public int seasonsCount;
    public int roomTypesCount;

    public List<Season> seasons = new ArrayList<>();

    private final AnswersHelper placeholderFactory;

    RatesTableData(AnswersHelper placeholderFactory) {
        this.placeholderFactory = placeholderFactory;
    }

    void readResponse(QuestionnaireConfigurationItem config, List<String> acceptedRates, QuestionnaireResponse response) {
        readDynamic(response.getAnswers());
        readRateGridState(config, acceptedRates);
        readRates(response.getAnswers(), acceptedRates);
    }

    private void readRates(Map<String, String> answers, List<String> acceptedRates) {
        for(int i = 1; i<= seasonsCount; i++){
            createSeason(answers, acceptedRates, i);
        }
    }

    private void readDynamic(Map<String, String> answers) {
        final String dynamicPricingKey = "DYNAMIC_PRICING";
        dynamicExists = answers.containsKey(dynamicPricingKey) && placeholderFactory.readYesNo(answers.get(dynamicPricingKey));
        if(dynamicExists){
            dynamicAmount = placeholderFactory.readAmount(answers.get("DYNAMIC_PCT_Discount"), "N/A");
        }
    }

    @SuppressWarnings("unchecked")
    private void readRateGridState(QuestionnaireConfigurationItem rateGridState, List<String> acceptedRates) {
        determineAvailableRates((List<String>) rateGridState.data.get("rate"), acceptedRates);
        determineAvailableOccupancies( (List<String>) rateGridState.data.get("occupancy"), acceptedRates);
        determineAvailableSeasons( ((List) rateGridState.data.get("season")).size(), acceptedRates);
        determineAvailableRoomTypes( ((List) rateGridState.data.get("roomType")).size(), acceptedRates);
    }

    private void determineAvailableOccupancies(List<String> occupancyConfig, List<String> acceptedRates) {
        if(acceptedRates == null || acceptedRates.isEmpty()){
            occupancies = new HashSet<>(occupancyConfig);
        } else {
            occupancies = occupancyConfig.stream().filter(o -> {
                final Pattern ratePattern = Pattern.compile(String.format(".*%s$", o));
                return acceptedRates.stream().anyMatch( ar -> ratePattern.matcher(ar).matches());
            }).collect(Collectors.toSet());

            if(occupancies.isEmpty())
                occupancies.add(occupancyConfig.get(0));
        }

        addOccupancyNamesPerRateAndOccupancy(rateNames.size(), occupancies);
    }

    private void addOccupancyNamesPerRateAndOccupancy(int size, Set<String> occupancies) {
        boolean containsSingle = occupancies.contains(OCCUPANCY_SINGLE);
        boolean containsDouble = occupancies.contains(OCCUPANCY_DOUBLE);
        for(;size > 0; size --){
            if(containsSingle) occupancyNames.add(OCCUPANCY_SINGLE_LABEL);
            if(containsDouble) occupancyNames.add(OCCUPANCY_DOUBLE_LABEL);
        }
    }

    private void determineAvailableRoomTypes(int roomTypesCount, List<String> acceptedRates) {
        this.roomTypesCount = roomTypesCount;
        if(!(acceptedRates == null || acceptedRates.isEmpty())){
            for(;this.roomTypesCount>1; this.roomTypesCount--){
                final Pattern p = Pattern.compile(String.format("^.{7,8}RT%s.{4}", this.roomTypesCount));
                if(acceptedRates.stream().anyMatch( ar -> p.matcher(ar).matches())) return;
            }
        }
    }

    private void determineAvailableSeasons(int seasonsCount, List<String> acceptedRates) {
        this.seasonsCount = seasonsCount;
        if(!(acceptedRates == null || acceptedRates.isEmpty())){
            for(;this.seasonsCount>1; this.seasonsCount--){
                final Pattern p = Pattern.compile(String.format(".{4,5}S%s.{8}", this.seasonsCount));
                if(acceptedRates.stream().anyMatch( ar -> p.matcher(ar).matches())) return;
            }
        }
    }

    private void determineAvailableRates(List<String> rateConfig, List<String> acceptedRates) {
        if(acceptedRates == null || acceptedRates.isEmpty()){
            rateNames = new ArrayList<>(rateConfig);
        } else {
            final List<String> rates = rateConfig.stream().filter(r -> {
                final Pattern ratePattern = Pattern.compile(String.format("^%s.*", r));
                return acceptedRates.stream().anyMatch(ar -> ratePattern.matcher(ar).matches());
            }).collect(Collectors.toList());

            rateNames = rates.isEmpty() ? rateConfig.subList(0, 1) : rates;
        }
    }

    private void createSeason(Map<String, String> answers, List<String> acceptedRates, int ordinal) {
        final Season season = new Season();

        season.ordinal = ordinal;
        season.period = placeholderFactory.createPeriod(
                answers.get(String.format(SEASON_START_PATTERN, ordinal)),
                answers.get(String.format(SEASON_END_PATTERN, ordinal))
        );
        if(season.period == null) {
            season.period = "Not Available";
        }

        for(int i = 1; i<=roomTypesCount; i++){
            final Season.RoomType roomType = season.new RoomType();
            roomType.ordinal = i;

            for(String rateLabel : rateNames){
                for(String occupancy : occupancies){
                    final String rateId = String.format(RATE_KEY_PATTERN, rateLabel, ordinal, i, occupancy);
                    final Object amount = acceptedRates == null || acceptedRates.isEmpty() || acceptedRates.contains(rateId) ? answers.get(rateId) : null;
                    roomType.rates.add(placeholderFactory.readAmount(amount, "N/A"));
                }
            }

            season.roomTypes.add(roomType);
        }
        seasons.add(season);
    }

    public class Season {
        public int ordinal;

        public String period;
        public List<RoomType> roomTypes = new ArrayList<>();

        public class RoomType {
            public int ordinal;
            public List<String> rates = new ArrayList<>();
        }
    }
}
