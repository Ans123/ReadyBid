package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
class HotelRfpBidResponseStateProducer {

    private static final Pattern RATE_GRID_SEASONS_PATTERN = Pattern.compile("^(?:SEASON|LRA_S|NLRA_S|GOVT_S)([1-5])(?:START|END|_RT.{5})$");
    private static final Pattern RATE_GRID_ROOM_TYPES_PATTERN = Pattern.compile("^(?:LRA_S|NLRA_S|GOVT_S)(?:[1-5])_RT([1-3])(?:.{4})$");
    private static final Pattern BLACKOUT_DATES_PATTERN = Pattern.compile("^(?:BD)(10|[1-9])(?:_.*)$");
    private static final Pattern BLACKOUT_DATES_ROOM_TYPES_PATTERN = Pattern.compile("^(?:BD)(?:10|[1-9])_RT([1-3])(?:_.*)$");
    private static final Pattern LENGTH_OF_STAY_PATTERN = Pattern.compile("^(?:N?LRA_S._RT._L|LOS)([2-4])(?:_ES_.{3}|M.{2}_ES)$");
    private static final Pattern EXTENDED_STAY_SEASONS_PATTERN = Pattern.compile("^(?:N?LRA_S|SEASON)([1-5])(?:_RT._L._ES_.{3}|START_ES|END_ES)$");
    private static final Pattern EXTENDED_STAY_ROOM_TYPES_PATTERN = Pattern.compile("^(?:N?LRA_S)(?:[1-5])_RT([1-3])(?:_L._ES_.{3}|START_ES|END_ES)$");
    private static final Pattern GROUP_SEASONS_PATTERN = Pattern.compile("^(?:GROUP_S)([1-5])(?:_10-50|_51-100)$");

    List<QuestionnaireConfigurationItem> recreateState(Map<String, String> answers) {
        final List<String> answersIds = answers.keySet().stream().filter(id -> answerNotNullOrEmpty(answers, id))
                .collect(Collectors.toList());

        final List<QuestionnaireConfigurationItem> state = new ArrayList<>();
        state.add(calculateRatesTableState(answersIds));
        state.add(calculateBlackoutsTableState(answersIds));
        state.add(calculateExtendedRatesTableState(answersIds));
        state.add(calculateGroupsTableState(answersIds));
        return state;
    }

    private static boolean answerNotNullOrEmpty(Map<String, String> answers, String id){
        final String answer = answers.get(id);
        return !(answer == null || answer.isEmpty());
    }

    private static QuestionnaireConfigurationItem calculateGroupsTableState(List<String> answersIds) {
        final Map<String, Object> data = new HashMap<>();
        data.put("season", generateListOfOneTo(calculateMaxInAnswersFor(GROUP_SEASONS_PATTERN, answersIds)));
        return new QuestionnaireConfigurationItem("GRR", data);
    }

    private static QuestionnaireConfigurationItem calculateExtendedRatesTableState(List<String> answersIds) {
        final Map<String, Object> data = new HashMap<>();
        data.put("season", generateListOfOneTo(calculateMaxInAnswersFor(EXTENDED_STAY_SEASONS_PATTERN, answersIds)));
        data.put("lengthOfStay", generateListOf(2, calculateMaxInAnswersFor(LENGTH_OF_STAY_PATTERN, answersIds, 2)));
        data.put("roomType", generateListOfOneTo(calculateMaxInAnswersFor(EXTENDED_STAY_ROOM_TYPES_PATTERN, answersIds)));
        return new QuestionnaireConfigurationItem("ESRT", data);
    }

    private static QuestionnaireConfigurationItem calculateBlackoutsTableState(List<String> answersIds) {
        final Map<String, Object> data = new HashMap<>();
        data.put("blackoutDate", generateListOfOneTo(calculateMaxInAnswersFor(BLACKOUT_DATES_PATTERN, answersIds)));
        data.put("roomType", generateListOfOneTo(calculateMaxInAnswersFor(BLACKOUT_DATES_ROOM_TYPES_PATTERN, answersIds)));
        return new QuestionnaireConfigurationItem("BFDT", data);
    }

    private static QuestionnaireConfigurationItem calculateRatesTableState(List<String> answersIds) {
        final Map<String, Object> data = new HashMap<>();
        data.put("season", generateListOfOneTo(calculateMaxInAnswersFor(RATE_GRID_SEASONS_PATTERN, answersIds)));
        data.put("roomType", generateListOfOneTo(calculateMaxInAnswersFor(RATE_GRID_ROOM_TYPES_PATTERN, answersIds)));
        return new QuestionnaireConfigurationItem("RT", data);
    }

    private static int calculateMaxInAnswersFor(Pattern pattern, List<String> answersIds, int defaultValue){
        return answersIds.stream()
                .map(a -> {
                    final Matcher m = pattern.matcher(a);
                    return m.matches() ? m.group(1) : null;
                })
                .filter(Objects::nonNull)
                .map(Integer::parseInt)
                .max(Integer::compareTo)
                .orElse(defaultValue);
    }

    private static int calculateMaxInAnswersFor(Pattern pattern, List<String> answersIds){
        return calculateMaxInAnswersFor(pattern, answersIds, 1);
    }

    private static List<Integer> generateListOf(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

    private static List<Integer> generateListOfOneTo(int end) {
        return generateListOf(1, end);
    }
}
