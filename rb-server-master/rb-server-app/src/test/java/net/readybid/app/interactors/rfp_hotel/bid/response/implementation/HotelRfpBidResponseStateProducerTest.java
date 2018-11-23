package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelRfpBidResponseStateProducerTest {

    private HotelRfpBidResponseStateProducer sut;

    @BeforeEach
    void setup(){
        sut = new HotelRfpBidResponseStateProducer();
    }

    @Nested
    class RecreateStateTest{

        private Map<String, String> answers;
        private List<QuestionnaireConfigurationItem> result;

        private final Runnable mut = () -> result = sut.recreateState(answers);

        @Test
        void whenAnswersAreEmpty(){
            answers = new HashMap<>();
            mut.run();

            isRateGridState(1, 1);
            isBlackOutTableState(1, 1);
            isExtendedStayTableState(1, 1, 1);
            isGroupsTableState(1);
        }

        @Test
        void whenAnswersAreFull(){
            answers = generateFullAnswers();
            mut.run();

            isRateGridState(5, 3);
            isBlackOutTableState(10, 3);
            isExtendedStayTableState(5, 3, 3);
            isGroupsTableState(5);
        }

        @Test
        void whenAnswersAreRandom(){
            final int rateGridSeasons = RbRandom.randomInt(1,5);
            final int rateGridRoomTypes = RbRandom.randomInt(1,3);
            final int blackoutTableDates = RbRandom.randomInt(1,10);
            final int blackoutTableRoomTypes = RbRandom.randomInt(1,3);
            final int extendedStayTableSeasons = RbRandom.randomInt(1,5);
            final int extendedStayTableRoomTypes = RbRandom.randomInt(1,3);
            final int extendedStayTableLengthOfStays = RbRandom.randomInt(1,3);
            final int groupTableSeasons = RbRandom.randomInt(1,5);

            answers = new HashMap<>();
            answers.putAll(generateRateGrid(rateGridSeasons, rateGridRoomTypes));
            answers.putAll(generateBlackoutTable(blackoutTableDates, blackoutTableRoomTypes));
            answers.putAll(generateExtendedStayTable(extendedStayTableSeasons, extendedStayTableRoomTypes, extendedStayTableLengthOfStays));
            answers.putAll(generateGroupsTable(groupTableSeasons));

            mut.run();

            isRateGridState(rateGridSeasons, rateGridRoomTypes);
            isBlackOutTableState(blackoutTableDates, blackoutTableRoomTypes);
            isExtendedStayTableState(extendedStayTableSeasons, extendedStayTableRoomTypes, extendedStayTableLengthOfStays);
            isGroupsTableState(groupTableSeasons);
        }

        private void isRateGridState(int seasons, int roomTypes) {
            final QuestionnaireConfigurationItem rateGridConfig = getConfig("RT");
            assertState(rateGridConfig, "season", seasons);
            assertState(rateGridConfig, "roomType", roomTypes);
        }

        private void isBlackOutTableState(int blackoutDates, int roomTypes) {
            final QuestionnaireConfigurationItem blackoutTableConfig = getConfig("BFDT");
            assertState(blackoutTableConfig, "blackoutDate", blackoutDates);
            assertState(blackoutTableConfig, "roomType", roomTypes);
        }

        private void isExtendedStayTableState(int seasons, int roomTypes, int lengthOfStays) {
            final QuestionnaireConfigurationItem extendedStayTableConfig = getConfig("ESRT");
            assertState(extendedStayTableConfig, "season", seasons);
            assertState(extendedStayTableConfig, "roomType", roomTypes);
            assertState(extendedStayTableConfig, "lengthOfStay", lengthOfStays);
        }

        private void isGroupsTableState(int seasons) {
            assertState(getConfig("GRR"), "season", seasons);
        }

        private void assertState(QuestionnaireConfigurationItem config, String stateId, int expected){
            assertEquals(expected, ((List) config.data.get(stateId)).size(),
                    String.format("state invalid for config %s and state %s", config.id, stateId));
        }

        private QuestionnaireConfigurationItem getConfig(String configId){
            return result.stream().filter(i -> i.id.equals(configId)).findFirst().orElse(null);
        }

        private Map<String, String> generateFullAnswers() {
            final Map<String, String> answers = new HashMap<>();
            answers.putAll(generateRateGrid(5, 3));
            answers.putAll(generateBlackoutTable(10, 3));
            answers.putAll(generateExtendedStayTable(5, 3, 3));
            answers.putAll(generateGroupsTable(5));
            return answers;
        }

        private Map<String, String> generateRateGrid(int seasons, int roomTypes) {
            final Map<String, String> answers = new HashMap<>();
            for(int season = 1; season <= seasons; season++){
                answers.put(String.format("SEASON%sSTART", season), RbRandom.alphanumeric());
                answers.put(String.format("SEASON%sEND", season), RbRandom.alphanumeric());
                for(int roomType = 1; roomType <= roomTypes; roomType++){
                    answers.put(String.format("%s_S%s_RT%s_%s", randomRate(), season, roomType, randomOccupancy()), RbRandom.alphanumeric());
                }
            }
            return answers;
        }

        private Map<String, String> generateBlackoutTable(int dates, int roomTypes) {
            final Map<String, String> answers = new HashMap<>();
            for(int date = 1; date <= dates; date++){
                answers.put(String.format("BD%s_START", date), RbRandom.alphanumeric());
                answers.put(String.format("BD%s_END", date), RbRandom.alphanumeric());
                answers.put(String.format("BD%s_NAME", date), RbRandom.alphanumeric());
                for(int roomType = 1; roomType <= roomTypes; roomType++){
                    answers.put(String.format("BD%s_RT%s_%s", date, roomType, randomOccupancy()), RbRandom.alphanumeric());
                }
            }
            return answers;
        }

        private Map<String, String> generateExtendedStayTable(int seasons, int roomTypes, int lengthOfStays) {
            final Map<String, String> answers = new HashMap<>();
            for(int season = 1; season <= seasons; season++){
                answers.put(String.format("SEASON%sSTART_ES", season), RbRandom.alphanumeric());
                answers.put(String.format("SEASON%sEND_ES", season), RbRandom.alphanumeric());
                for(int roomType = 1; roomType <= roomTypes; roomType++){
                    for(int stay = 2; stay <= lengthOfStays+1; stay++) {
                        answers.put(String.format("%s_S%s_RT%s_L%s_ES_%s", randomEsRate(), season, roomType, stay, randomOccupancy()), RbRandom.alphanumeric());
                    }
                }
            }
            return answers;
        }

        private Map<String, String> generateGroupsTable(int seasons) {
            final Map<String, String> answers = new HashMap<>();
            for(int season = 1; season <= seasons; season++){
                answers.put(String.format("GROUP_S%s_10-50", season), RbRandom.alphanumeric());
                answers.put(String.format("GROUP_S%s_51-100", season), RbRandom.alphanumeric());
            }
            return answers;
        }

        private String randomOccupancy() {
            return RbRandom.randomFromArray(new String[]{"SGL", "DBL"});
        }

        private String randomRate() {
            return RbRandom.randomFromArray(new String[]{"LRA", "NLRA", "GOVT"});
        }

        private String randomEsRate() {
            return RbRandom.randomFromArray(new String[]{"LRA", "NLRA"});
        }
    }
}