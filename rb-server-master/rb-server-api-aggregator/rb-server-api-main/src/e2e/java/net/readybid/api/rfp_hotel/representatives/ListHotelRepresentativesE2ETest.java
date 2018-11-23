package net.readybid.api.rfp_hotel.representatives;


import net.readybid.api.AuthorizationHelperService;
import net.readybid.entity_factories.AccountTestFactory;
import net.readybid.entity_factories.AuthenticatedUserImplFactory;
import net.readybid.app.MongoDbHelperService;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;


@DisplayName("[E2E] List Hotel Representatives ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class ListHotelRepresentativesE2ETest {

    private static final String URL_HOTEL = "/rfps/hotel/representatives/hotel/%s";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private HttpHeaders headers;
    private HttpEntity entity;
    private String hotelId;

    private Supplier<ResponseEntity<String>> mut = () -> restTemplate
            .exchange(String.format(URL_HOTEL, hotelId), HttpMethod.GET, entity, String.class);


    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
        mongoDbHelperService.drop();
    }

    @AfterEach
    void teardown(){
        mongoDbHelperService.drop();
    }

    @DisplayName("as Guest should fail")
    @Test
    void returns401IfNotAuthenticated() {
        entity = new HttpEntity(headers);
        hotelId = RbRandom.idAsString();

        final ResponseEntity<String> response = mut.get();

        assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @DisplayName("as User ")
    @Nested
    class AsAuthenticatedUser {

        private static final String ENTITY_WITHOUT_REPRESENTATIVES_ID = "56014c0a9537801174120000";
        private static final String ENTITY_ID = "56014c0a9537801174120001";

        private AuthenticatedUser user;

        @BeforeEach
        void setUp() throws IOException {
            mongoDbHelperService.load("ListHotelRepresentatives.json");
            user = AuthenticatedUserImplFactory.random(AccountTestFactory.random(EntityType.COMPANY, "56014c0a9537801174120003", Permission.BUYER_UPDATE));
            authorizationHelperService.authorize(headers, user);
            entity = new HttpEntity(headers);
            hotelId = ENTITY_ID;
        }

        @DisplayName("should return empty List when there are no representatives")
        @Test
        void shouldReturnEmptyListWhenNoRepresentatives() throws Exception {
            hotelId = ENTITY_WITHOUT_REPRESENTATIVES_ID;

            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            JSONAssert.assertEquals("{ data: [], count: 0 } ", response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should list Hotel Representative. Hotel Contact, Chain Representative and Chain Contact for Hotel")
        @Test
        void shouldReturnTheList() throws Exception {
            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String expectedStr = "{ data: [ " +
                    "{ id: '59df7063bb832a05ecde0001', isUser: true, userId: '59ad5164bb832a251cde0001', entityId: '56014c0a9537801174120001', accountId: '59b152fbbb832a069cd20001', firstName: 'Simple Hotel Representative', lastName: 'Test 1', emailAddress: 'hotelRep_1@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'Hotel REP not Used in Bids', accountName: 'HOTEL Under Test', accountType: 'HOTEL'}, " +
                    "{ id: '59df7063bb832a05ecde0002', isUser: true, userId: '59ad5164bb832a251cde0002', entityId: '56014c0a9537801174120003', accountId: '59b152fbbb832a069cd20003', firstName: 'Simple Chain Representative', lastName: 'Test 2', emailAddress: 'chainRep_1@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'Chain REP not Used in Bids', accountName: 'HOTEL CHAIN Under Test', accountType: 'CHAIN'}, " +
                    "{ id: '59df7063bb832a05ecde0003', isUser: false, entityId: '56014c0a9537801174120001', accountId: '59b152fbbb832a069cd20001', firstName: 'Simple Hotel Contact', lastName: 'Test 3', emailAddress: 'hotelContact_1@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'Hotel Contact', accountName: 'Bid Account Name', accountType: 'HOTEL'}, " +
                    "{ id: '59df7063bb832a05ecde0004', isUser: false, entityId: '56014c0a9537801174120003', accountId: '59b152fbbb832a069cd20003', firstName: 'Simple Chain Contact', lastName: 'Test 4', emailAddress: 'chainContact_1@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'Chain Contact', accountName: 'Bid Account Name', accountType: 'CHAIN'}," +
                    "{} " +
                    "], count: 5}";
            JSONAssert.assertEquals(expectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should list only ACTIVE Users")
        @Test
        void shouldListOnlyActiveUsers() throws Exception {
            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String notEexpectedStr = "{ data: [ { id: '59df7063bb832a05ecde0005'} ] }";
            JSONAssert.assertNotEquals(notEexpectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should list only Contacts from SENT Bids")
        @Test
        void shouldListOnlyContactsFromSentBids() throws Exception {
            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String notExpectedStr = "{ data: [ { id: '59df7063bb832a05ecde0006'} ] }";
            JSONAssert.assertNotEquals(notExpectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should not list Contact if Representative with same Email Address and Account exists")
        @Test
        void shouldNotListContactIfRepresentativeWithSameEmailAddressAndAccountExists() throws Exception {
            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String expectedStr = "{ data: [ { id: '59df7063bb832a05ecde0001'}, {id: '59df7063bb832a05ecde0008'}, {}, {}, {} ] }";
            final String notExpectedStr = "{ data: [ { id: '59df7063bb832a05ecde0007'} ] }";
            JSONAssert.assertEquals(expectedStr, response.getBody(), JSONCompareMode.LENIENT);
            JSONAssert.assertNotEquals(notExpectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should list only most recent Contacts with same Email Address and Account")
        @Test
        void shouldListOnlyMostRecentContactsWithSameEmailAddressAndAccount() throws Exception {
            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String notExpectedStr = "{ data: [ { id: '59df7063bb832a05ecd00000'}, { id: '59df7063bb832a05ecd00001'} ] }";
            JSONAssert.assertNotEquals(notExpectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }
    }
}