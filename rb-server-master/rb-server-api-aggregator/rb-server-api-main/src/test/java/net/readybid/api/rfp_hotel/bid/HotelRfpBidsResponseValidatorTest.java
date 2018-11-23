package net.readybid.api.rfp_hotel.bid;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HotelRfpBidsResponseValidatorTest {

    private final static int QUESTION_MAX_LENGTH = 100;
    private final static int ANSWER_MAX_LENGTH = 10000;

    private HotelRfpBidsResponseValidator sut;

    @BeforeEach
    void setup(){
        sut = new HotelRfpBidsResponseValidator();
    }

    @Nested
    class IsValidTest{

        // method arguments
        private Map<String, Object> response;
        private final ConstraintValidatorContext context = null;

        // method result
        private boolean result;

        // method
        private Runnable mut = () -> result = sut.isValid(response, context);

        @Test
        void whenResponseIsNull(){
            response = null;
            mut.run();
            isResponseInvalid();
        }

        @Test
        void whenResponseIsAnEmptyMap(){
            response = new HashMap<>();
            mut.run();
            isResponseValid();
        }


        @Test
        void whenResponseContainsAnswersOfMaxSizeOrLess(){
            response = createResponseWithQuestionsAndAnswersOfMaxSizeOrLess();
            mut.run();
            isResponseValid();
        }

        @Test
        void whenResponseContainsAnswersWithNullQuestion(){
            response = createResponseWithNullQuestion();
            mut.run();
            isResponseInvalid();
        }

        @Test
        void whenResponseContainsAnswersWithNullAnswer(){
            response = createResponseWithNullAnswer();
            mut.run();
            isResponseValid();
        }

        @Test
        void whenResponseContainsAnswersOverMaxSize(){
            response = createResponseWithAnswerOverMaxSize();
            mut.run();
            isResponseInvalid();
        }

        @Test
        void whenResponsesContainQuestionOverMaxSize(){
            response = createResponseWithQuestionOverMaxSize();
            mut.run();
            isResponseInvalid();
        }

        private void isResponseValid() {
            assertTrue(result);
        }

        private void isResponseInvalid() {
            assertFalse(result);
        }
    }

    static Map<String, Object> createResponseWithQuestionsAndAnswersOfMaxSizeOrLess(){
        final Map<String, Object> response = new HashMap<>();
        response.put(RbRandom.alphanumeric(QUESTION_MAX_LENGTH), RbRandom.alphanumeric(ANSWER_MAX_LENGTH));
        response.put(RbRandom.alphanumeric(QUESTION_MAX_LENGTH, true), RbRandom.alphanumeric(ANSWER_MAX_LENGTH, true));
        return response;
    }

    static Map<String, Object> createResponseWithNullQuestion(){
        final Map<String, Object> response = createResponseWithQuestionsAndAnswersOfMaxSizeOrLess();
        response.put(null, RbRandom.alphanumeric(ANSWER_MAX_LENGTH));
        return response;
    }

    static Map<String, Object> createResponseWithNullAnswer(){
        final Map<String, Object> response = createResponseWithQuestionsAndAnswersOfMaxSizeOrLess();
        response.put(RbRandom.alphanumeric(QUESTION_MAX_LENGTH), null);
        return response;
    }

    static Map<String, Object> createResponseWithAnswerOverMaxSize(){
        final Map<String, Object> response = createResponseWithQuestionsAndAnswersOfMaxSizeOrLess();
        response.put(RbRandom.alphanumeric(QUESTION_MAX_LENGTH), RbRandom.alphanumeric(ANSWER_MAX_LENGTH+1, true));
        return response;
    }

    static Map<String, Object> createResponseWithQuestionOverMaxSize(){
        final Map<String, Object> response = createResponseWithQuestionsAndAnswersOfMaxSizeOrLess();
        response.put(RbRandom.alphanumeric(QUESTION_MAX_LENGTH+1, true), RbRandom.alphanumeric(ANSWER_MAX_LENGTH));
        return response;
    }
}