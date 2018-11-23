package net.readybid.app.persistence.mongodb.app.rfp_hotel;


import net.readybid.app.MongoDbHelperService;
import net.readybid.app.MongoIntegrationTestConfiguration;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentativeAssert;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRfpRepresentativesRepositoryImpl;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[ INTEGRATION ] Hotel Rfp Representatives Loader ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HotelRfpRepresentativesLoaderImpl.class, HotelRfpRepresentativesRepositoryImpl.class,
        MongoIntegrationTestConfiguration.class, MongoDbHelperService.class})
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@ActiveProfiles("default,integrationTest")
class HotelRfpRepresentativesLoaderImplIntegrationTest {

    @Autowired
    private HotelRfpRepresentativesLoaderImpl sut;

    @Autowired
    private MongoDbHelperService mongo;

    @BeforeEach
    void setup() throws Exception{
        mongo.drop();
        mongo.load("HotelRfpRepresentativesLoaderImplIntegrationTest.json");
    }

    @AfterEach
    void teardown(){
        mongo.drop();
    }

    @DisplayName("Get Account Id For Entity ")
    @Nested
    class GetAccountIdForEntityTest{

        private final static String ENTITY_ID = "56014c0a9537801174120001";
        private final static String ACCOUNT_ID = "59b152fbbb832a069cd20001";

        private String entityId;
        private final Supplier<String> mut = () -> sut.getAccountIdForEntity(entityId);

        @Test
        void returnsAccountId(){
            entityId = ENTITY_ID;

            final String result = mut.get();

            assertEquals(ACCOUNT_ID, result);
        }
    }

    @DisplayName("Get Hotel and its MasterChain AccountsIds")
    @Nested
    class GetHotelAndItsMasterChainAccountsIds{

        private final static String CHAIN_ACCOUNT_ID = "59b152fbbb832a069cd20001";
        private final static String HOTEL_ENTITY_ID = "56014c0a9537801174120003";
        private final static String HOTEL_ACCOUNT_ID = "59b152fbbb832a069cd20011";

        private String hotelEntityId;
        private final Supplier<List<String>> mut = () -> sut.getHotelAndMasterChainAccountIdsForHotel(hotelEntityId);

        @Test
        void returnsAccountId(){
            hotelEntityId = HOTEL_ENTITY_ID;

            final List<String> result = mut.get();

            new ListAssert<>(result)
                    .contains(HOTEL_ACCOUNT_ID)
                    .contains(CHAIN_ACCOUNT_ID);
        }
    }

    @DisplayName("load Representatives for Accounts ")
    @Nested
    class GetRepresentativesForAccountsTest{

        private List<String> accountsIds = Arrays.asList("59b152fbbb832a069cd20001", "59b152fbbb832a069cd20011");

        private Supplier<List<HotelRfpRepresentative>> mut = () -> sut.getRepresentativesFromAccounts(accountsIds);

        @DisplayName("should return empty List when accountsIds is null")
        @Test
        void shouldReturnEmptyListWhenAccountsIdsIsNull(){
            accountsIds = null;

            final List<HotelRfpRepresentative> representatives = mut.get();

            assertTrue(representatives.isEmpty());
        }

        @DisplayName("should return empty List when No Representatives Are Found")
        @Test
        void shouldReturnEmptyListWhenNoRepresentativesAreFound(){
            mongo.drop();

            final List<HotelRfpRepresentative> representatives = mut.get();

            assertTrue(representatives.isEmpty());
        }

        @DisplayName("should return Representatives with saved data")
        @Test
        void shouldReturnRepresentativesWithSavedData(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            HotelRfpRepresentativeAssert.that(representatives.get(0))
                    .hasId("59df7063bb832a05ecde0001")
                    .hasUserId("59ad5164bb832a251cde0001")
                    .hasAccountId("59b152fbbb832a069cd20001")
                    .hasEntityId("56014c0a9537801174120001")
                    .hasAccountName("CHAIN 1")
                    .hasAccountType(EntityType.CHAIN)
                    .hasFirstName("Test 1")
                    .hasLastName("Test 1")
                    .hasEmailAddress("chainRep_1@gmail.com")
                    .hasPhone("1234567")
                    .hasProfilePicture("picture.jpg")
                    .hasJobTitle("CHAIN REP not Used in Bids")
                    .isUser(true);
        }

        @DisplayName("should return all Active Representatives")
        @Test
        void shouldReturnAllActiveRepresentatives(){
            final String ACCOUNT_1_REP_1_ID = "59df7063bb832a05ecde0001";
            final String ACCOUNT_1_REP_2_ID = "59df7063bb832a05ecde0002";
            final String ACCOUNT_1_REP_3_ID = "59df7063bb832a05ecde0003";
            final String INACTIVE_ACCOUNT_1_REP_ID = "59df7063bb832a05ecde0004";
            final String OTHER_ACCOUNT_REP_ID = "59df7063bb832a05ecde0005";
            final String ACCOUNT_2_REP_1_ID = "59df7063bb832a05ecde0006";

            final List<HotelRfpRepresentative> representatives = mut.get();

            assertEquals(4, representatives.size());
            final List<String> ids = representatives.stream().map(r -> r.id).collect(Collectors.toList());
            assertTrue(ids.contains(ACCOUNT_1_REP_1_ID));
            assertTrue(ids.contains(ACCOUNT_1_REP_2_ID));
            assertTrue(ids.contains(ACCOUNT_1_REP_3_ID));
            assertFalse(ids.contains(INACTIVE_ACCOUNT_1_REP_ID));
            assertFalse(ids.contains(OTHER_ACCOUNT_REP_ID));
            assertTrue(ids.contains(ACCOUNT_2_REP_1_ID));
        }
    }

    @DisplayName("load Contacts from Bids ")
    @Nested
    class LoadContactsFromBidsTest{
        private final static String OLDER_CHAIN_CONTACT_ID = "5a4508dabb832a2258950000";
        private final static String CHAIN_CONTACT_1_ID = "5a4508dabb832a2258950001";
        private final static String UNSENT_BID_CONTACT_ID = "5a4508dabb832a2258950002";
        private final static String CHAIN_CONTACT_2_ID = "5a4508dabb832a2258950003";
        private final static String OTHER_ACCOUNT_CONTACT_ID = "5a4508dabb832a2258950004";
        private final static String CHAIN_REP_DUPLICATE_ID = "5a4508dabb832a2258950005";
        private final static String HOTEL_CONTACT_ID = "5a4508dabb832a2258950006";
        private final static String CHAIN_CONTACT_WITH_HOTEL_CONTACT_EMAIL_ID = "5a4508dabb832a2258950006";


        private List<String> accountsIds = Arrays.asList("59b152fbbb832a069cd20001", "59b152fbbb832a069cd20011");

        private Supplier<List<HotelRfpRepresentative>> mut = () -> sut.getSupplierContactsFromBidsWithAccounts(accountsIds);

        @DisplayName("should return empty List when accountsIds is null")
        @Test
        void shouldReturnEmptyListWhenAccountsIdsIsNull(){
            accountsIds = null;

            final List<HotelRfpRepresentative> representatives = mut.get();

            assertTrue(representatives.isEmpty());
        }

        @DisplayName("should return empty List when No Contacts Are Found")
        @Test
        void shouldReturnEmptyListWhenNoRepresentativesAreFound(){
            mongo.drop();

            final List<HotelRfpRepresentative> representatives = mut.get();

            assertTrue(representatives.isEmpty());
        }

        @DisplayName("should return Contacts with saved data")
        @Test
        void shouldReturnRepresentativesWithSavedData(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            final HotelRfpRepresentative rep = representatives.stream().filter(r -> CHAIN_CONTACT_1_ID.equals(r.id)).findFirst().get();

            HotelRfpRepresentativeAssert.that(rep)
                    .hasId(CHAIN_CONTACT_1_ID)
                    .hasUserId(null)
                    .hasAccountId("59b152fbbb832a069cd20001")
                    .hasEntityId("56014c0a9537801174120001")
                    .hasAccountName("CHAIN 1")
                    .hasAccountType(EntityType.CHAIN)
                    .hasFirstName("Chain Contact 1")
                    .hasLastName("Test 2")
                    .hasEmailAddress("chainContact_1@gmail.com")
                    .hasPhone("1234567")
                    .hasProfilePicture("picture.jpg")
                    .hasJobTitle("Bid Contact")
                    .isUser(false);
        }

        @DisplayName("should return Contacts from All Accounts")
        @Test
        void shouldReturnContactsFromAllAccounts(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            final List<String> ids = representatives.stream().map(r -> r.id).collect(Collectors.toList());
            assertTrue(ids.contains(CHAIN_CONTACT_1_ID));
            assertTrue(ids.contains(CHAIN_CONTACT_2_ID));
            assertTrue(ids.contains(HOTEL_CONTACT_ID));
            assertFalse(ids.contains(OTHER_ACCOUNT_CONTACT_ID));
        }

        @DisplayName("should return only Contacts not Representatives")
        @Test
        void shouldReturnOnlyContactsNotRepresentatives(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            final List<String> ids = representatives.stream().map(r -> r.id).collect(Collectors.toList());

            assertFalse(ids.contains(CHAIN_REP_DUPLICATE_ID));
        }

        @DisplayName("should return only from Bids in SENT state")
        @Test
        void shouldReturnOnlyFromBidsInSentState(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            final List<String> ids = representatives.stream().map(r -> r.id).collect(Collectors.toList());

            assertFalse(ids.contains(UNSENT_BID_CONTACT_ID));
        }

        @DisplayName("should group Contacts with same Email Address and Account and return latest one")
        @Test
        void shouldGroupContactsWithSameEmailAddressAndTypeAndReturnLatestOne(){
            final List<HotelRfpRepresentative> representatives = mut.get();

            assertEquals(4, representatives.size());
            final List<String> ids = representatives.stream().map(r -> r.id).collect(Collectors.toList());
            assertFalse(ids.contains(OLDER_CHAIN_CONTACT_ID));
            assertTrue(ids.contains(CHAIN_CONTACT_1_ID));
            assertTrue(ids.contains(CHAIN_CONTACT_2_ID));
            assertTrue(ids.contains(HOTEL_CONTACT_ID));
            assertTrue(ids.contains(CHAIN_CONTACT_WITH_HOTEL_CONTACT_EMAIL_ID));
        }
    }
}