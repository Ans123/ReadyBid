package net.readybid.api.rfp_hotel.bid.supplier.contact;

import com.mongodb.client.MongoDatabase;
import net.readybid.api.AuthorizationHelperService;
import net.readybid.app.MongoDbHelperService;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection;
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

import static com.mongodb.client.model.Filters.eq;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.oid;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[E2E] Create Bid Supplier Contact")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class CreateBidSupplierContactE2ETest {

    private static final String URL = "/rfps/hotel/bids/%s/supplier/contact/create";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private MongoDatabase mongoDb;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private final static String DB_DATA = "CreateBidSupplierContactE2ETest.json";

    private HttpHeaders headers;
    private HttpEntity<CreateHotelRfpSupplierContactWebRequest> entity;
    private CreateHotelRfpSupplierContactWebRequest request;
    private String bidId;

    private Supplier<ResponseEntity<String>> mut = () -> restTemplate
            .exchange(String.format(URL, bidId), HttpMethod.POST, entity, String.class);

    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
    }

    @Nested
    @DisplayName("Create Hotel Contact")
    class CreateHotelContactTest{
        private final static String TYPE = "HOTEL";

        @BeforeEach
        void setup() throws IOException {
            prepareDb();
            bidId = "5a0c8569bb832a2128f20000";
            authenticateRequest(CreateHotelRfpSupplierContactWebRequestFactory.random(TYPE));
        }

        @AfterEach
        void teardown(){
            mongoDbHelperService.drop();
        }

        @Nested
        @DisplayName("Success")
        class SuccessTest{

            @Nested
            @DisplayName("When Hotel Account Exists")
            class WhenHotelAccountExistsTest{

                @DisplayName("when Bid Supplier Contact is Empty, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithoutContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20000";

                    final ResponseEntity<String> response = mut.get();

                    assertSuccess(response, createCompany());
                }

                @DisplayName("when Bid Supplier Contact Exists, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20010";

                    final ResponseEntity<String> response = mut.get();

                    assertSuccess(response, createCompany());
                }

                private Document createCompany(){
                    return new Document("type", request.accountType )
                            .append("accountId", new ObjectId("59b152fbbb832a069cd20001"))
                            .append("entityId", new ObjectId("56014c0a9537801174120001"))
                            .append("name", "HOTEL ACCOUNT")
                            .append("website", "http://hotel.com")
                            .append("emailAddress", "hotel_1@gmail.com")
                            .append("phone", "1234567")
                            .append("logo", "logo.jpg")
                            .append("location", createLocation());
                }
            }
        }

        @Nested
        @DisplayName("Failure")
        class FailureTest{

            @DisplayName("fails if not authorized")
            @Test
            void failsIfNotAuthorized() {
                assertFailureWhenNotAuthorized(TYPE);
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
        }
    }

    @Nested
    @DisplayName("Create Chain Contact")
    class CreateChainContactTest{
        private final static String TYPE = "CHAIN";

        @BeforeEach
        void setup() throws IOException {
            prepareDb();
            bidId = "5a0c8569bb832a2128f20100";
            authenticateRequest(CreateHotelRfpSupplierContactWebRequestFactory.random(TYPE));
        }

        @AfterEach
        void teardown(){
            mongoDbHelperService.drop();
        }

        @Nested
        @DisplayName("Success")
        class SuccessTest{

            @Nested
            @DisplayName("When Chain Account Exists")
            class WhenChainAccountExistsTest {

                @DisplayName("when Bid Supplier Contact is Empty, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithoutContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20100";

                    final ResponseEntity<String> response = mut.get();

                    assertSuccess(response, createCompany());
                }

                @DisplayName("when Bid Supplier Contact Exists, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20110";

                    final ResponseEntity<String> response = mut.get();

                    assertSuccess(response, createCompany());
                }

                private Document createCompany() {
                    return new Document("type", request.accountType )
                            .append("accountId", new ObjectId("59b152fbbb832a069cd20002"))
                            .append("entityId", new ObjectId("5a314aebe6d3107588940001"))
                            .append("name", "CHAIN ACCOUNT")
                            .append("website", "http://chain.com")
                            .append("emailAddress", "chain_1@gmail.com")
                            .append("phone", "1234567")
                            .append("logo", "logo.jpg")
                            .append("location", createLocation());
                }
            }

            @Nested
            @DisplayName("When Chain Account Does Not Exist")
            class WhenChainAccountDoesNotExistTest {

                @DisplayName("when Bid Supplier Contact is Empty, Creates Account, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithoutContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20120";

                    final ResponseEntity<String> response = mut.get();

                    assertCreatedAccount();
                    assertSuccess(response, createCompany());
                }

                @DisplayName("when Bid Supplier Contact Exists, Creates Account, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
                @Test
                void savesContactIntoBidWithContact() throws JSONException {
                    bidId = "5a0c8569bb832a2128f20130";

                    final ResponseEntity<String> response = mut.get();

                    assertCreatedAccount();
                    assertSuccess(response, createCompany());
                }

                private void assertCreatedAccount() throws JSONException {
                    final Document account = mongoDb.getCollection(AccountCollection.COLLECTION_NAME)
                            .find(eq(AccountCollection.ENTITY_ID, oid("5a314aebe6d31075889406a1"))).first();
                    JSONAssert.assertEquals(
                            "{ _id: {},  entityId: { $oid: '5a314aebe6d31075889406a1'}, name:'Test Chain Entity', type: 'CHAIN'}",
                            account.toJson(), JSONCompareMode.LENIENT);
                }

                private Document createCompany() {
                    return new Document("type", request.accountType )
                            .append("entityId", new ObjectId("5a314aebe6d31075889406a1"))
                            .append("name", "Test Chain Entity");
                }
            }
        }

        @Nested
        @DisplayName("Failure")
        class FailureTest{

            @DisplayName("fails if not authorized")
            @Test
            void failsIfNotAuthorized() {
                assertFailureWhenNotAuthorized(TYPE);
            }

            @DisplayName("returns error with updated bid if bid is deleted")
            @Test
            void returnsErrorIfBidIsDeletedWithUpdatedBid() throws JSONException {
                bidId = "5a0c8569bb832a2128f20140";
                assertErrorOnDeletedBid();
            }

            @DisplayName("returns error with updated bid if bid state has changed")
            @Test
            void returnsErrorIfBidStateHasChangedWithUpdatedBid() throws JSONException {
                bidId = "5a0c8569bb832a2128f20150";
                assertErrorOnBidStatusChanged();
            }
        }
    }

    @Nested
    @DisplayName("Request Errors")
    class RequestErrorsTest{
        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();

            authenticateRequest(CreateHotelRfpSupplierContactWebRequestFactory.random());
        }

        @Test
        void failsIfNoType() {
            request.accountType = null;
            assertBadRequest();
        }

        private void assertBadRequest() {
            entity = new HttpEntity<>(request, headers);
            final ResponseEntity<String> response = mut.get();
            assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @DisplayName("if type is not CHAIN or HOTEL")
        @Test
        void failsIfTypeIsNotChainOrHotel() {
            request.accountType = "";
            assertBadRequest();
        }

        @DisplayName("if no first name")
        @Test
        void failsIfNoFirstName() {
            request.firstName = null;
            assertBadRequest();
        }

        @DisplayName("if first name is empty")
        @Test
        void failsIfFirstNameEmpty() {
            request.firstName = "";
            assertBadRequest();
        }

        @DisplayName("if first name is too long")
        @Test
        void failsIfFirstNameTooLong() {
            request.firstName = RbRandom.alphanumeric(51, true);
            assertBadRequest();
        }

        @DisplayName("if no last name")
        @Test
        void failsIfNoLastName() {
            request.lastName = null;
            assertBadRequest();
        }

        @DisplayName("if last name is empty")
        @Test
        void failsIfLastNameEmpty() {
            request.lastName = "";
            assertBadRequest();
        }

        @DisplayName("if last name is too long")
        @Test
        void failsIfLastNameTooLong() {
            request.lastName = RbRandom.alphanumeric(51, true);
            assertBadRequest();
        }

        @DisplayName("if no email address")
        @Test
        void failsIfNoEmailAddress() {
            request.emailAddress = null;
            assertBadRequest();
        }

        @DisplayName("if email address is empty")
        @Test
        void failsIfEmailAddressIsEmpty() {
            request.emailAddress = "";
            assertBadRequest();
        }

        @DisplayName("if email address is invalid")
        @Test
        void failsIfEmailAddressIsInvalid() {
            request.emailAddress = RbRandom.name();
            assertBadRequest();
        }

        @DisplayName("if phone too long")
        @Test
        void failsIfPhoneTooLong() {
            request.phone = RbRandom.alphanumeric(21, true);
            assertBadRequest();
        }

        @DisplayName("if job title too long")
        @Test
        void failsIfjobTitleTooLong() {
            request.phone = RbRandom.alphanumeric(101, true);
            assertBadRequest();
        }
    }

    @Nested
    @DisplayName("Authenticated Errors")
    class AuthenticatedErrorsTest{

        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();

            authenticateRequest(CreateHotelRfpSupplierContactWebRequestFactory.random());
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

    private void authenticateRequest(CreateHotelRfpSupplierContactWebRequest request){
        this.request = request;
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

    private void assertFailureWhenNotAuthorized(String type){
        headers = new HttpHeaders();
        final AuthenticatedUser user = AuthenticatedUserImplFactory.random(AccountTestFactory.random());
        authorizationHelperService.authorize(headers, user);
        entity = new HttpEntity<>(CreateHotelRfpSupplierContactWebRequestFactory.random(type), headers);

        final ResponseEntity<String> response = mut.get();

        assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private void assertReturnedUpdatedBid(ResponseEntity<String> response) throws JSONException {
        final String body = response.getBody();
        final String expectedResponse = String.format("{ data: [" +
                "{ status: 'OK', tObject: {$bidId:'%s', $rfpId:'4a0c8569bb832a2128f20000', $status:'CREATED', $supplierContact:{} } }" +
                "], count:1}", bidId);
        JSONAssert.assertEquals(expectedResponse, body, JSONCompareMode.LENIENT);
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

    private void assertSavedContact(Document company) throws JSONException {
        final Document bid = mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(bidId)).first();
        final Object supplierContactId = RbMapUtils.getObject(bid, "supplier.contact.id");
        assertNotNull(supplierContactId);
        final String expectedJson = generateExpectedSavedSupplierContact(company);
        JSONAssert.assertEquals(expectedJson, bid.toJson(), JSONCompareMode.LENIENT);
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

    private String generateExpectedSavedSupplierContact(Document expectedCompany) {
        final Document supplierContact = new Document("firstName", request.firstName )
                .append("lastName", request.lastName)
                .append("fullName", String.format("%s %s", request.firstName, request.lastName))
                .append("emailAddress", request.emailAddress.toLowerCase())
                .append("phone", request.phone)
                .append("jobTitle", request.jobTitle)
                .append("isUser", false)
                .append("company", expectedCompany);


        final String jsonTemplate = "{supplier: {contact: %s }}";
        return String.format(jsonTemplate, supplierContact.toJson());
    }
}