package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid.response;

import net.readybid.app.MongoDbHelperService;
import net.readybid.app.MongoIntegrationTestConfiguration;
import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRfpBidRepositoryImpl;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[ INTEGRATION ] Hotel Rfp Bid Response Loader Impl")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HotelRfpBidResponseLoaderImpl.class, HotelRfpBidRepositoryImpl.class,
        MongoIntegrationTestConfiguration.class, MongoDbHelperService.class})
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@ActiveProfiles("default,integrationTest")
class HotelRfpBidResponseLoaderImplTest {

    @Autowired
    private HotelRfpBidResponseLoader sut;

    @Autowired
    private MongoDbHelperService mongo;

    @Nested
    @DisplayName("Get Bids With Questionnaire And Response Context")
    class GetBidsWithQuestionnaireAndResponseContextTest {

        private List<String> bidsIds = Arrays.asList("5a672dc09f4c2f50b876876e");

        private final Supplier<List<? extends HotelRfpBid>> mut = () -> sut.getBidsWithQuestionnaireHotelIdResponseContextFields(bidsIds);

        @BeforeEach
        void setup() throws Exception{
            mongo.drop();
            mongo.load("HotelRfpBidResponseLoaderImplTest-GetBidsWithQuestionnaireAndResponseContext.json");
        }

        @Test
        @DisplayName("loads Questionnaire Form without exception")
        void test(){
            final List<? extends HotelRfpBid> result = mut.get();

            final HotelRfpBid bid = result.get(0);
            assertNotNull(bid);
            assertEquals("2019-01-01", bid.getProgramStartDate().toString());
            assertEquals("2019-12-31", bid.getProgramEndDate().toString());
            assertNotNull(bid.getQuestionnaire().getModel());
        }
    }
}