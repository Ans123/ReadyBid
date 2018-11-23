package net.readybid.api.rfp_hotel.bid.supplier.contact;

import com.mongodb.client.MongoDatabase;
import net.readybid.api.AuthorizationHelperService;
import net.readybid.app.MongoDbHelperService;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entity_factories.AccountTestFactory;
import net.readybid.entity_factories.AuthenticatedUserImplFactory;
import net.readybid.test_utils.RbRandom;
import net.readybid.utils.RbMapUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byIds;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[E2E] Set Bid Supplier Contact On Multiple Bids")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class SetBidSupplierContactOnMultipleBidsE2ETest {

    private static final String URL = "/rfps/hotel/bids/supplier/contact/set";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private MongoDatabase mongoDb;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private final static String DB_DATA = "SetBidSupplierContactOnMultipleBidsE2ETest.json";
    private final static String CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID = "5a0c8569bb832a2128f20100";
    private final static String CREATED_WITH_SUPPLIER_WITH_ACCOUNT_BID_ID = "5a0c8569bb832a2128f20110";
    private final static String DELETED_BID_ID = "5a0c8569bb832a2128f20140";
    private final static String SENT_BID_ID = "5a0c8569bb832a2128f20150";
    private final static String UNAUTHORIZED_BID_ID = "5a0c8569bb832a2128f20160";
    private final static String BID_WITH_CHAIN_ID_NOT_MATCHING_USER_ENTITY_ID = "5a0c8569bb832a2128f20170";

    private HttpHeaders headers;
    private HttpEntity<SetHotelRfpSupplierContactsWebRequest> entity;
    private SetHotelRfpSupplierContactsWebRequest request;

    private Supplier<ResponseEntity<String>> mut = () -> restTemplate
            .exchange(URL, HttpMethod.POST, entity, String.class);

    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
        request = new SetHotelRfpSupplierContactsWebRequest();
        request.bids = Arrays.asList(RbRandom.idAsString(), RbRandom.idAsString());
        request.userAccountId = "5a311464bb832a1ed806a010";
    }

    @Nested
    @DisplayName("Set Contacts")
    class SetContactTest{

        @BeforeEach
        void setup() throws IOException {
            prepareDb();
            authenticateRequest();
        }

        @AfterEach
        void teardown(){
            mongoDbHelperService.drop();
        }

        @Nested
        @DisplayName("Success")
        class SuccessTest{

            @DisplayName("when Bid Supplier Contact is Empty, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
            @Test
            void savesContactIntoBidWithoutContact() throws JSONException {
                request.bids = Collections.singletonList(CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID);

                final ResponseEntity<String> response = mut.get();

                assertSuccess(response, createChain());
            }

            @DisplayName("when Bid Supplier Contact Exists, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
            @Test
            void savesContactIntoBidWithContact() throws JSONException {
                request.bids = Collections.singletonList(CREATED_WITH_SUPPLIER_WITH_ACCOUNT_BID_ID);

                final ResponseEntity<String> response = mut.get();

                assertSuccess(response, createChain());
            }

            @DisplayName("saves Supplied Contact into Bids and returns updated Bids in ActionReport")
            @Test
            void savesContactIntoBids() throws JSONException {
                request.bids = Arrays.asList(
                        CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID,
                        CREATED_WITH_SUPPLIER_WITH_ACCOUNT_BID_ID
                );

                final ResponseEntity<String> response = mut.get();

                assertSame(HttpStatus.OK, response.getStatusCode());

                final List<Document> bids = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME)
                        .find(byIds(request.bids)).into(new ArrayList<>());
                for(Document bid : bids){
                    final Object supplierContactId = RbMapUtils.getObject(bid, "supplier.contact.id");
                    assertNotNull(supplierContactId);
                    JSONAssert.assertEquals(generateExpectedSavedSupplierContact(createChain()), bid.toJson(), JSONCompareMode.LENIENT);
                }

                final String body = response.getBody();
                final String expectedResponse = String.format("{ data: [" +
                        "{ status: 'OK', tObject: {$bidId:'%1$s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{ id: '%3$s' } } }, " +
                        "{ status: 'OK', tObject: {$bidId:'%2$s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{ id: '%3$s' } } }" +
                        "], count:2}", CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID, CREATED_WITH_SUPPLIER_WITH_ACCOUNT_BID_ID,
                        request.userAccountId);
                JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
            }

            private Document createChain(){
                return new Document("type", "CHAIN" )
                        .append("accountId", new ObjectId("5a311464bb832a1ed806a010"))
                        .append("entityId", new ObjectId("56014c0a9537801174122010"))
                        .append("name", "CHAIN NAME")
                        .append("website", "http://chain.com")
                        .append("emailAddress", "chain@chain.com")
                        .append("phone", "18189977000")
                        .append("logo", "chain.jpg")
                        .append("location", createLocation());
            }
        }

        @Nested
        @DisplayName("Failure")
        class FailureTest{

            @DisplayName("fails if not Authorized on Any Bid")
            @Test
            void failsIfNotAuthorized() {
                request.bids = Arrays.asList( CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID,
                        CREATED_WITH_SUPPLIER_WITH_ACCOUNT_BID_ID, UNAUTHORIZED_BID_ID );

                final ResponseEntity<String> response = mut.get();

                assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
            }

            @DisplayName("returns errors in report")
            @Test
            void returnsErrorsInReportWithUpdatedBids() throws JSONException {
                request.bids = Arrays.asList( CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID, DELETED_BID_ID, SENT_BID_ID );

                final ResponseEntity<String> response = mut.get();

                assertSame(HttpStatus.OK, response.getStatusCode());
                final String body = response.getBody();
                final String expectedResponse = String.format("{ data: [" +
                        "{ status: 'OK', tObject: {$bidId:'%1$s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{} } }, " +
                        "{ status: 'NOT_FOUND', message: \"Bid with id %2$s wasn't found\", tObject: {$bidId:'%2$s', $rfpId:'4a0c8569bb832a2128f20000', $status:'DELETED' } }, " +
                        "{ status: 'STATUS_CHANGED', message: \"Bid Status was changed to SENT\", tObject: {$bidId:'%3$s', $rfpId:'4a0c8569bb832a2128f20000', $status:'SENT' } }" +
                        "], count:3}", CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID, DELETED_BID_ID, SENT_BID_ID);
                JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
            }


            @DisplayName("returns error if UserAccount is not found")
            @Test
            void returnsErrorIfUserAccountIsNotFoundWithUpdatedBid() throws JSONException {
                request.userAccountId = RbRandom.idAsString();
                request.bids = Collections.singletonList(CREATED_WITHOUT_SUPPLIER_WITH_ACCOUNT_BID_ID);

                assertErrorOnSupplierContactCreation();
            }

            @DisplayName("returns error if EntityId From Bid And UserAccount Is Not Equal")
            @Test
            void returnsErrorIfEntityIdFromBidAndUserAccountIsNotEqual() throws JSONException {
                request.bids = Collections.singletonList(BID_WITH_CHAIN_ID_NOT_MATCHING_USER_ENTITY_ID);

                assertErrorOnSupplierContactCreation();
            }
        }
    }

    @Nested
    @DisplayName("Request Errors")
    class RequestErrorsTest{
        @BeforeEach
        void setup(){
            request.bids = Collections.singletonList(RbRandom.idAsString());

            authenticateRequest();
        }

        @Test
        void failsIUserAccountIdIsNull() {
            request.userAccountId = null;
            assertBadRequest();
        }

        @Test
        void failsIfNoUserAccountId() {
            request.userAccountId = "";
            assertBadRequest();
        }

        @Test
        void failsIfUserAccountIdIsNotId() {
            request.userAccountId = RbRandom.alphanumeric(5, false);
            assertBadRequest();
        }

        @DisplayName("if bids list is null")
        @Test
        void failsIfBidsIsNull() {
            request.bids = null;
            assertBadRequest();
        }

        @DisplayName("if bids list is empty")
        @Test
        void failsIfBidsListIsEmpty() {
            request.bids = new ArrayList<>();
            assertForbidden();
        }

        @DisplayName("if bids list contains null")
        @Test
        void failsIfBidsListContainsNull() {
            request.bids = Arrays.asList(RbRandom.idAsString(), null, RbRandom.idAsString());
            assertBadRequest();
        }

        @DisplayName("if bids list contains empty string")
        @Test
        void failsIfBidsListContainsEmptyString() {
            request.bids = Arrays.asList(RbRandom.idAsString(), "", RbRandom.idAsString());
            assertBadRequest();
        }

        @DisplayName("if bids list contains non-id")
        @Test
        void failsIfBidsListContainsNonId() {
            request.bids = Arrays.asList(RbRandom.idAsString(), "any", RbRandom.idAsString());
            assertBadRequest();
        }

        private void assertForbidden() {
            assertStatus(HttpStatus.FORBIDDEN);
        }

        private void assertBadRequest() {
            assertStatus(HttpStatus.BAD_REQUEST);
        }

        private void assertStatus(HttpStatus status) {
            entity = new HttpEntity<>(request, headers);
            final ResponseEntity<String> response = mut.get();
            assertSame(status, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Unauthenticated Errors")
    class UnauthenticatedErrorsTest{
        @Test
        void returns401IfNotAuthenticated() {
            headers = new HttpHeaders();
            entity = new HttpEntity<>(request, headers);

            final ResponseEntity<String> response = mut.get();

            assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    private void prepareDb() throws IOException {
        mongoDbHelperService.load(DB_DATA);
    }

    private void authenticateRequest(){
        final String buyerCompanyAccountId = "59df7063bb832a05ecde0001";
        final Account account = AccountTestFactory
                .random(EntityType.COMPANY, buyerCompanyAccountId, Permission.BUYER_UPDATE);
        final AuthenticatedUser user = AuthenticatedUserImplFactory.random(account);
        authorizationHelperService.authorize(headers, user);
        entity = new HttpEntity<>(this.request, headers);
    }

    private void assertSuccess(ResponseEntity<String> response, Document expectedCompany) throws JSONException {
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertSavedContact(expectedCompany);
        assertReturnedUpdatedBid(response);
    }

    private void assertReturnedUpdatedBid(ResponseEntity<String> response) throws JSONException {
        final String body = response.getBody();
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'OK', tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{} } }" +
                "], count:1}", request.bids.get(0));
        JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
    }

    private void assertErrorOnSupplierContactCreation() throws JSONException {
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'ERROR', message: \"Supplier Contact couldn't be Created\", tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED' } }" +
                "], count:1}", request.bids.get(0));
        assertError(expectedResponse);
    }

    private void assertError(String expectedResponse) throws JSONException {
        final ResponseEntity<String> response = mut.get();

        final String body = response.getBody();
        JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
        assertContactNotSaved();
    }

    private void assertContactNotSaved(){
        final Document bid = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(request.bids.get(0))).first();
        final Object supplierContact = RbMapUtils.getObject(bid, "supplier.contact");
        assertNull(supplierContact);
    }

    private void assertSavedContact(Document expectedCompany) throws JSONException {
        final Document bid = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(request.bids.get(0))).first();
        final Object supplierContactId = RbMapUtils.getObject(bid, "supplier.contact.id");
        assertNotNull(supplierContactId);
        JSONAssert.assertEquals(generateExpectedSavedSupplierContact(expectedCompany), bid.toJson(), JSONCompareMode.LENIENT);
    }

    private Document createCoordinates(){
        return new Document("lat", 100D).append("lng", 100D);
    }

    private Document createLocation(){
        return new Document("address", createAddress())
                .append("fullAddress", "A FULL ADDRESS")
                .append("coordinates", createCoordinates());
    }

    private Document createAddress(){
        return new Document("address1", "A ADDRESS 1")
                .append("address2", "A ADDRESS 2")
                .append("city", "A CITY")
                .append("region", "A REGION")
                .append("zip", "A ZIP CODE")
                .append("country", "RS");
    }

    private String generateExpectedSavedSupplierContact(Document company) {
        final Document supplierContact = new Document("firstName", "UserFirst" )
                .append("lastName", "UserLast")
                .append("fullName", "UserFirst UserLast")
                .append("emailAddress", "user@gmail.com")
                .append("phone", "12345")
                .append("jobTitle", "HOTEL".equals(company.get("type")) ? "hotel rep" : "chain rep")
                .append("profilePicture", "picture.jpg")
                .append("isUser", true)
                .append("company", company);


        final String jsonTemplate = "{supplier: {contact: %s }}";
        return String.format(jsonTemplate, supplierContact.toJson());
    }
}
