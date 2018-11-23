package net.readybid.api.rfp_hotel.bid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.*;

import static net.readybid.api.rfp_hotel.bid.HotelRfpBidsResponseValidatorTest.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HotelRfpBidsResponsesValidatorTest {

    private HotelRfpBidsResponsesValidator sut;

    @BeforeEach
    void setup(){
        sut = new HotelRfpBidsResponsesValidator();
    }

    @Nested
    class IsValidTest{

        // method arguments
        private List<Map<String, Object>> listOfResponses;
        private final ConstraintValidatorContext context = null;

        // method result
        private boolean result;

        // method
        private Runnable mut = () -> result = sut.isValid(listOfResponses, context);

        @Test
        void whenResponsesAreNull(){
            listOfResponses = null;
            mut.run();
            areResponsesValid();
        }

        @Test
        void whenResponsesAreEmpty(){
            listOfResponses = new ArrayList<>();
            mut.run();
            areResponsesValid();
        }

        @Test
        void whenResponsesContainEmptyMap(){
            listOfResponses = Arrays.asList(new HashMap<>(), new HashMap<>());
            mut.run();
            areResponsesValid();
        }

        @Test
        void whenResponsesContainNullMap(){
            listOfResponses = Arrays.asList(new HashMap<>(), null);
            mut.run();
            areResponsesInvalid();
        }

        @Test
        void whenResponsesContainAnswersOfMaxSizeOrLess(){
            listOfResponses = Arrays.asList(new HashMap<>(), createResponseWithQuestionsAndAnswersOfMaxSizeOrLess());
            mut.run();
            areResponsesValid();
        }

        @Test
        void whenResponsesContainAnswersWithNullQuestion(){
            listOfResponses = Arrays.asList(new HashMap<>(), createResponseWithNullQuestion());
            mut.run();
            areResponsesInvalid();
        }

        @Test
        void whenResponsesContainAnswersWithNullAnswer(){
            listOfResponses = Arrays.asList(new HashMap<>(), createResponseWithNullAnswer());
            mut.run();
            areResponsesValid();
        }

        @Test
        void whenResponsesContainAnswersOverMaxSize(){
            listOfResponses = Arrays.asList(new HashMap<>(), createResponseWithAnswerOverMaxSize());
            mut.run();
            areResponsesInvalid();
        }

        @Test
        void whenResponsesContainQuestionOverMaxSize(){
            listOfResponses = Arrays.asList(new HashMap<>(), createResponseWithQuestionOverMaxSize());
            mut.run();
            areResponsesInvalid();
        }

        private void areResponsesValid() {
            assertTrue(result);
        }

        private void areResponsesInvalid() {
            assertFalse(result);
        }
    }
}