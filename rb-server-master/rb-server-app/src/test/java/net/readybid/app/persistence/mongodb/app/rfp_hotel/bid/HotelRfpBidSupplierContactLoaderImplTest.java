package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.MongoDbHelperService;
import net.readybid.app.MongoIntegrationTestConfiguration;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRfpBidRepositoryImpl;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRfpBidSetBidContactDataRepositoryImpl;
import net.readybid.test_utils.RbRandom;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("[ INTEGRATION ] Hotel Rfp Bid Supplier Contact Loader Impl ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HotelRfpBidSupplierContactLoaderImpl.class, HotelRfpBidRepositoryImpl.class, HotelRfpBidSetBidContactDataRepositoryImpl.class,
        MongoIntegrationTestConfiguration.class, MongoDbHelperService.class})
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@ActiveProfiles("default,integrationTest")
class HotelRfpBidSupplierContactLoaderImplTest {

    @Autowired
    private HotelRfpBidSupplierContactLoaderImpl sut;

    @Autowired
    private MongoDbHelperService mongo;

    private final static String BID_ID = "5a005ea1bb832a0b00c83df2";
    private final static String BID_WITH_DIFFERENT_ENTITY_ID = "5a005ea1bb832a0b00c83df3";

    private final List<String> bidsIds = new ArrayList<>();

    @BeforeEach
    void setup() throws Exception{
        mongo.drop();
        mongo.load("HotelRfpBidSupplierContactLoaderImplTest.json");
    }

    @Nested
    @DisplayName("Get Hotel Entity Id")
    class GetHotelEntityIdTest{

        private final Supplier<String> mut = () -> sut.getEntityId(EntityType.HOTEL, bidsIds);

        @Test
        void returnsHotelEntityId(){
            bidsIds.add(BID_ID);

            final String result = mut.get();

            assertEquals("59ad51a3bb832a27e8d38ff9", result);
        }

        @Test
        void returnsNullWhenBidIsNotFound(){
            bidsIds.add(RbRandom.idAsString());

            assertNull(mut.get());
        }

        @Test
        void returnsNullWhenBidsDoNotHaveSameEntityId(){
            bidsIds.add(BID_ID);
            bidsIds.add(BID_WITH_DIFFERENT_ENTITY_ID);

            assertNull(mut.get());
        }
    }

    @Nested
    @DisplayName("Get Master Chain Entity Id")
    class GetMasterChainEntityIdTest{

        private final Supplier<String> mut = () -> sut.getEntityId(EntityType.CHAIN, bidsIds);

        @Test
        void returnsMasterChainEntityId(){
            bidsIds.add(BID_ID);

            final String result = mut.get();

            assertEquals("59ad51a3bb832a27e8d38000", result);
        }

        @Test
        void throwsNotFoundExceptionIfBidIsNotFound(){
            bidsIds.add(RbRandom.idAsString());

            assertNull(mut.get());
        }

        @Test
        void returnsNullWhenBidsDoNotHaveSameEntityId(){
            bidsIds.add(BID_ID);
            bidsIds.add(BID_WITH_DIFFERENT_ENTITY_ID);

            assertNull(mut.get());
        }
    }
}