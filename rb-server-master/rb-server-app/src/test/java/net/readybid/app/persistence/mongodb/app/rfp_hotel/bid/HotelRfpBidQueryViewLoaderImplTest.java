package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.MongoDbHelperService;
import net.readybid.app.MongoIntegrationTestConfiguration;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewAssert;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRfpBidQueryViewRepositoryImpl;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.test_utils.RbRandom;
import org.bson.types.ObjectId;
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

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@DisplayName("[ INTEGRATION ] Hotel Rfp Bid Query View Loader Impl ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HotelRfpBidQueryViewLoaderImpl.class, HotelRfpBidQueryViewRepositoryImpl.class,
        MongoIntegrationTestConfiguration.class, MongoDbHelperService.class})
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@ActiveProfiles("default,integrationTest")
class HotelRfpBidQueryViewLoaderImplTest {

    @Autowired
    private HotelRfpBidQueryViewLoaderImpl sut;

    @Autowired
    private MongoDbHelperService mongo;

    @BeforeEach
    void setup() throws Exception{
        mongo.drop();
        mongo.load("HotelRfpBidQueryViewLoaderImplTest.json");
    }

    @Nested
    @DisplayName("Get Hotel Rfp Bid Query View Id")
    class GetHotelRfpBidQueryViewTest {
        private final static String RFP_ID = "5acdb5cb8a7431515418d832";
        private final Id BID_ID = new Id("5acdb5f18a7431515418d835");

        private String rfpId;
        private List<Id> bidsIds;
        private AuthenticatedUser currentUser;

        private final Supplier<List<HotelRfpBidQueryView>> mut = () -> sut.find(bidsIds, currentUser);

        @Test
        void returnsHotelAccountId(){
            rfpId = RFP_ID;
            bidsIds = Collections.singletonList(BID_ID);
            setCurrentUser();

            HotelRfpBidQueryViewAssert.that(mut.get().get(0))
                    .hasBidId(BID_ID)
                    .hasRfpId(rfpId)
                    .hasStatus(HotelRfpBidStateStatus.RESPONDED)
                    .hasHotelId("5910697917ec11611986414c")
                    .hasChainId("5a314578e6d310758894069b")
                    .assertSupplierContact(that -> {
                        that.contains("id", new ObjectId("5a74b4a78a7431566c2b0b04"))
                                .contains("firstName", "Dejan 123")
                                .contains("lastName", "Kosijer")
                                .contains("fullName", "Dejan 123 Kosijer")
                                .contains("emailAddress", "test@gmail.com")
                                .contains("phone", "+381000000000")
                                .contains("jobTitle", "test")
                                .contains("isUser", true)
                                .contains("profilePicture", "59ad5164bb832a251cde1eaa_d678ef00.jpg");
                    });
        }

        @Test
        void returnsEmptyListBidIsNotFound(){
            rfpId = RbRandom.idAsString();
            bidsIds = Collections.singletonList(RbRandom.id());
            setCurrentUser();

            final List<HotelRfpBidQueryView> views = mut.get();
            assertTrue(views.isEmpty());
        }

        private void setCurrentUser() {
            currentUser = mock(AuthenticatedUser.class);
            doReturn(RbRandom.randomEnum(EntityType.class)).when(currentUser).getAccountType();
        }
    }
}