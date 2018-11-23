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
import java.util.function.Supplier;

import static net.readybid.mongodb.RbMongoFilters.byId;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[E2E] Set Bid Supplier Contact")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class SetBidSupplierContactE2ETest {

    private static final String URL = "/rfps/hotel/bids/%s/supplier/contact/set";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private MongoDatabase mongoDb;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private final static String DB_DATA = "SetBidSupplierContactE2ETest.json";

    private HttpHeaders headers;
    private HttpEntity<SetHotelRfpSupplierContactWebRequest> entity;
    private SetHotelRfpSupplierContactWebRequest request;
    private String bidId;

    private Supplier<ResponseEntity<String>> mut = () -> restTemplate
            .exchange(String.format(URL, bidId), HttpMethod.POST, entity, String.class);

    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
        request = new SetHotelRfpSupplierContactWebRequest();
        request.userAccountId = "5a311464bb832a1ed806a000";
    }

    @Nested
    @DisplayName("Set Contact")
    class SetContactTest{

        @BeforeEach
        void setup() throws IOException {
            prepareDb();
            bidId = "5a0c8569bb832a2128f20000";
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
                bidId = "5a0c8569bb832a2128f20000";

                final ResponseEntity<String> response = mut.get();

                assertSuccess(response, createHotel());
            }

            @DisplayName("when Bid Supplier Contact Exists, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
            @Test
            void savesContactIntoBidWithContact() throws JSONException {
                bidId = "5a0c8569bb832a2128f20010";

                final ResponseEntity<String> response = mut.get();

                assertSuccess(response, createHotel());
            }

            @DisplayName("Saves Chain Rep as Bid Supplier Contact and returns updated Bid")
            @Test
            void savesChainRepAsBidSupplierContactAndReturnsUpdatedBid() throws JSONException {
                bidId = "5a0c8569bb832a2128f20000";
                request.userAccountId = "5a311464bb832a1ed806a010";

                final ResponseEntity<String> response = mut.get();

                assertSuccess(response, createChain());
            }

            private Document createHotel(){
                return new Document("type", "HOTEL" )
                        .append("accountId", new ObjectId("5a311464bb832a1ed806a000"))
                        .append("entityId", new ObjectId("56014c0a9537801174122000"))
                        .append("name", "HOTEL NAME")
                        .append("website", "http://hotel.com")
                        .append("emailAddress", "hotel@hotel.com")
                        .append("phone", "18189977000")
                        .append("logo", "hotel.jpg")
                        .append("location", createLocation());
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

            @DisplayName("fails if not authorized")
            @Test
            void failsIfNotAuthorized() {
                assertFailureWhenNotAuthorized();
            }

            @DisplayName("returns error if UserAccount is not found")
            @Test
            void returnsErrorIfUserAccountIsNotFoundWithUpdatedBid() throws JSONException {
                request.userAccountId = RbRandom.idAsString();
                bidId = "5a0c8569bb832a2128f20000";
                assertErrorOnSupplierContactCreation();
            }

            @DisplayName("returns error if bid is deleted with updated bid")
            @Test
            void returnsErrorIfBidIsDeletedWithUpdatedBid() throws JSONException {
                bidId = "5a0c8569bb832a2128f20020";
                assertErrorOnDeletedBid();
            }

            @DisplayName("returns error if bid state has changed with updated bid")
            @Test
            void returnsErrorIfBidStateHasChangedWithUpdatedBid() throws JSONException {
                bidId = "5a0c8569bb832a2128f20030";
                assertErrorOnBidStatusChanged();
            }

            @DisplayName("returns error if EntityId From Bid And UserAccount Is Not Equal")
            @Test
            void returnsErrorIfEntityIdFromBidAndUserAccountIsNotEqual() throws JSONException {
                bidId = "5a0c8569bb832a2128f20040";
                assertErrorOnSupplierContactCreation();
            }
        }
    }

    @Nested
    @DisplayName("Request Errors")
    class RequestErrorsTest{
        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();

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

        private void assertBadRequest() {
            entity = new HttpEntity<>(request, headers);
            final ResponseEntity<String> response = mut.get();
            assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Authenticated Errors")
    class AuthenticatedErrorsTest{

        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();

            authenticateRequest();
        }

        @Test
        void failsIfBidIsNotFound() {
            final ResponseEntity<String> response = mut.get();
            assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        assertSavedContact(expectedCompany);
        assertReturnedUpdatedBid(response);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    private void assertReturnedUpdatedBid(ResponseEntity<String> response) throws JSONException {
        final String body = response.getBody();
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'OK', tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{} } }" +
                "], count:1}", bidId);
        JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
    }

    private void assertFailureWhenNotAuthorized(){
        headers = new HttpHeaders();
        final AuthenticatedUser user = AuthenticatedUserImplFactory.random(AccountTestFactory.random());
        authorizationHelperService.authorize(headers, user);
        entity = new HttpEntity<>(SetHotelRfpSupplierContactWebRequestFactory.random(), headers);

        final ResponseEntity<String> response = mut.get();

        assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private void assertErrorOnSupplierContactCreation() throws JSONException {
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'ERROR', message: \"Supplier Contact couldn't be Created\", tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED' } }" +
                "], count:1}", bidId);
        assertError(expectedResponse);
    }

    private void assertErrorOnDeletedBid() throws JSONException {
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'NOT_FOUND', message: \"Bid with id %s wasn't found\", tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'DELETED' } }" +
                "], count:1}", bidId, bidId);
        assertError(expectedResponse);
    }

    private void assertErrorOnBidStatusChanged() throws JSONException {
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'STATUS_CHANGED', message: \"Bid Status was changed to RESPONDED\", tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'RESPONDED' } }" +
                "], count:1}", bidId);
        assertError(expectedResponse);
    }

    private void assertError(String expectedResponse) throws JSONException {
        final ResponseEntity<String> response = mut.get();

        final String body = response.getBody();
        JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
        assertContactNotSaved();
    }

    private void assertContactNotSaved(){
        final Document bid = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(bidId)).first();
        final Object supplierContact = RbMapUtils.getObject(bid, "supplier.contact");
        assertNull(supplierContact);
    }

    private void assertSavedContact(Document expectedCompany) throws JSONException {
        final Document bid = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(bidId)).first();
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
