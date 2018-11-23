package net.readybid.api.rfp_hotel.bid;

import com.mongodb.client.MongoDatabase;
import net.readybid.api.AuthorizationHelperService;
import net.readybid.app.MongoDbHelperService;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.email.EmailTemplateAssert;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.InvitationCollection;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.invitation.InvitationImpl;
import net.readybid.auth.invitation.InvitationImplAssert;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entity_factories.AccountTestFactory;
import net.readybid.entity_factories.AuthenticatedUserImplFactory;
import net.readybid.test_utils.RbRandom;
import net.readybid.utils.RbMapUtils;
import org.bson.Document;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.in;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("[E2E] Send Bids")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class SendBidsE2ETest {

    private static final String URL = "/rfps/hotel/bids/send";
    private static final String CHAIN_EMAIL_TEMPLATE = "/email-templates/hotel-rfp-bid-received-for-chain.html";
    private static final String HOTEL_EMAIL_TEMPLATE = "/email-templates/hotel-rfp-bid-received-for-hotel.html";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private MongoDatabase mongoDb;

    @Autowired
    private EmailService emailServiceMock;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private final static String DB_DATA = "SendBidsE2ETest.json";

    private HttpHeaders headers;
    private HotelRfpSendBidsWebRequest request;
    private HttpEntity<HotelRfpSendBidsWebRequest> entity;

    private Supplier<ResponseEntity<String>> mut = () -> restTemplate
            .exchange(URL, HttpMethod.POST, entity, String.class);


    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
        request = new HotelRfpSendBidsWebRequest();
        request.bids = Arrays.asList(RbRandom.idAsString(), RbRandom.idAsString());
        request.ignoreDueDate = true;
    }

    @Nested
    @DisplayName("Success")
    class Success{

        private static final String HOTEL_1_BID_ID = "5a0c8569bb832a2128f20010";
        private static final String HOTEL_2_BID_ID = "5a0c8569bb832a2128f20020";
        private static final String CHAIN_1_A_BID_ID = "5a0c8569bb832a2128f20030";
        private static final String CHAIN_1_B_BID_ID = "5a0c8569bb832a2128f20040";
        private static final String CHAIN_2_A_BID_ID = "5a0c8569bb832a2128f20050";

        private static final String ERROR_BID_DELETED = "5a0c8569bb832a2128f20060";
        private static final String ERROR_BID_WITHOUT_CONTACT = "5a0c8569bb832a2128f20070";
        private static final String ERROR_BID_STATUS_CHANGED = "5a0c8569bb832a2128f20080";

        private static final String HOTEL_EMAIL_SUBJECT = "New RFP Received from \"%s\"";
        private static final String CHAIN_EMAIL_SUBJECT = "New RFP Received from \"%s\"";


        @BeforeEach
        void setup() throws IOException {
            prepareDb();

            authenticateRequest();
        }

        @AfterEach
        void teardown(){
            mongoDbHelperService.drop();
        }

        @DisplayName("when Bid Supplier Contact is Empty, Saves supplied Contact as Bid Supplier Contact and returns updated Bid")
        @Test
        void savesContactIntoBidWithoutContact() throws JSONException {
            request.bids = Arrays.asList(HOTEL_1_BID_ID, HOTEL_2_BID_ID, CHAIN_1_A_BID_ID, CHAIN_1_B_BID_ID, CHAIN_2_A_BID_ID,
                    ERROR_BID_DELETED, ERROR_BID_WITHOUT_CONTACT, ERROR_BID_STATUS_CHANGED);

            final ResponseEntity<String> response = mut.get();
            assertSame(HttpStatus.OK, response.getStatusCode());

            final String responseBody = response.getBody();
            final ArgumentCaptor<List<EmailTemplate>> emailsCaptor = ArgumentCaptor.forClass(List.class);
            verify(emailServiceMock, times(1)).send(emailsCaptor.capture());
            final List<EmailTemplate> emails = emailsCaptor.getValue();

            assertHotelBid(responseBody, emails, HOTEL_1_BID_ID);
            assertHotelBid(responseBody, emails, HOTEL_2_BID_ID);
            assertChainBids(responseBody, emails, CHAIN_1_A_BID_ID, CHAIN_1_B_BID_ID);
            assertChainBids(responseBody, emails, CHAIN_2_A_BID_ID);
            assertErrorNotFound(responseBody, emails, ERROR_BID_DELETED);
            assertErrorNoContact(responseBody, ERROR_BID_WITHOUT_CONTACT);
            assertErrorBidStatusChanged(responseBody, emails, ERROR_BID_STATUS_CHANGED);
        }

        private void assertHotelBid(String responseBody, List<EmailTemplate> emails, String... bidsIds) throws JSONException {
            final List<Document> bids = assertThatSentBidsAreSaved(bidsIds);
            final InvitationImpl invitation = assertThatInvitationIsCreatedForBidSupplierContact(bids, bidsIds);
            assertThatEmailIsSentToHotel(emails, invitation, bids);
            assertThatResponseContainsSuccessReportWithBid(responseBody, bids);
        }

        private void assertChainBids(String responseBody, List<EmailTemplate> emails, String... bidsIds) throws JSONException {
            final List<Document> bids = assertThatSentBidsAreSaved(bidsIds);
            final InvitationImpl invitation = assertThatInvitationIsCreatedForBidSupplierContact(bids, bidsIds);
            assertThatEmailIsSentToChain(emails, invitation, bids);
            assertThatResponseContainsSuccessReportWithBid(responseBody, bids);
        }

        private void assertErrorNotFound(String responseBody, List<EmailTemplate> emails, String errorBidId) throws JSONException {
            assertThatBidStatusIsNotChangedInDatabase(getBid(errorBidId), "DELETED");
            assertThatEmailIsNotSentForError(emails);
            assertThatResponseContainsErrorReportWithBid(responseBody, errorBidId,
                    String.format("{ status: 'NOT_FOUND', tObject: { $bidId:'%s', $status:'DELETED' } }", errorBidId));
        }

        private void assertErrorNoContact(String responseBody, String errorBidId) throws JSONException {
            assertThatBidStatusIsNotChangedInDatabase(getBid(errorBidId), "CREATED");
            assertThatResponseContainsErrorReportWithBid(responseBody, errorBidId,
                    String.format("{ status: 'ERROR', tObject: { $bidId:'%s', $status:'CREATED' } }", errorBidId));
        }

        private void assertErrorBidStatusChanged(String responseBody, List<EmailTemplate> emails, String errorBidId) throws JSONException {
            assertThatBidStatusIsNotChangedInDatabase(getBid(errorBidId), "RESPONDED");
            assertThatEmailIsNotSentForError(emails);
            assertThatResponseContainsErrorReportWithBid(responseBody, errorBidId,
                    String.format("{ status: 'STATUS_CHANGED', tObject: { $bidId:'%s', $status:'RESPONDED' } }", errorBidId));
        }

        private void assertThatResponseContainsErrorReportWithBid(String responseBody, String errorBidId, String expectedResponse) throws JSONException {
            final StringBuilder expectedBidsBuilder = new StringBuilder("");
            for(String bidId : request.bids){
                expectedBidsBuilder.append(", ");
                expectedBidsBuilder.append(bidId.equals(errorBidId) ? expectedResponse : " {}");
            }

            final String expectedResponseJson = "{ data: [  " + expectedBidsBuilder.delete(0, 2).toString() + "  ]}";
            JSONAssert.assertEquals(expectedResponseJson, responseBody, JSONCompareMode.LENIENT);
        }

        private void assertThatBidStatusIsNotChangedInDatabase(Document bid, String expectedStatus) throws JSONException {
            JSONAssert.assertEquals(String.format("{state: {status: '%s'} }", expectedStatus), bid.toJson(), JSONCompareMode.LENIENT);
        }

        private void assertThatEmailIsSentToHotel(List<EmailTemplate> emails, InvitationImpl invitation, List<Document> bids) {
            final Document bid = bids.get(0);
            assertSingleEmailSent(emails, bid, HOTEL_EMAIL_TEMPLATE);
            final EmailTemplate email = getEmail(emails, invitation);

            EmailTemplateAssert.that(email)
                    .hasHtmlTemplate(HOTEL_EMAIL_TEMPLATE)
                    .hasReceiverEmailAddress(RbMapUtils.getString(bid, SUPPLIER_CONTACT_EMAIL_ADDRESS))
                    .hasReceiverName(RbMapUtils.getString(bid, SUPPLIER_CONTACT_FULL_NAME))
                    .hasSubject(String.format(HOTEL_EMAIL_SUBJECT,RbMapUtils.getString(bid, BUYER_CONTACT_COMPANY_NAME)))
                    .hasEmptyCC();
        }

        private void assertThatEmailIsSentToChain(List<EmailTemplate> emails, InvitationImpl invitation, List<Document> bids) {
            final Document bid = bids.get(0);
            assertSingleEmailSent(emails, bid, CHAIN_EMAIL_TEMPLATE);
            final EmailTemplate email = getEmail(emails, invitation);

            EmailTemplateAssert.that(email)
                    .hasHtmlTemplate(CHAIN_EMAIL_TEMPLATE)
                    .hasReceiverEmailAddress(RbMapUtils.getString(bid, SUPPLIER_CONTACT_EMAIL_ADDRESS))
                    .hasReceiverName(RbMapUtils.getString(bid, SUPPLIER_CONTACT_FULL_NAME))
                    .hasSubject(String.format(CHAIN_EMAIL_SUBJECT,RbMapUtils.getString(bid, BUYER_CONTACT_COMPANY_NAME)))
                    .hasEmptyCC();
        }

        private void assertSingleEmailSent(List<EmailTemplate> emails, Document bid, String template){
            final String supplierEmailAddress = RbMapUtils.getString(bid, SUPPLIER_CONTACT_EMAIL_ADDRESS);

            assertEquals(1L, emails.stream().filter(e ->
                    e.getHtmlTemplateName().equals(template) && e.getReceiver().getAddress().equals(supplierEmailAddress)
            ).count());
        }

        private void assertThatEmailIsNotSentForError(List<EmailTemplate> emails){
            assertNull(emails.stream().filter(e -> e.getReceiver().getAddress().equals("error@test.com"))
                    .findAny().orElse(null));
        }

        private void assertThatResponseContainsSuccessReportWithBid(String responseBody, List<Document> bids) throws JSONException {
            final StringBuilder expectedBidsBuilder = new StringBuilder("");
            for(String bidId : request.bids){
                final Document bid = bids.stream().filter( b -> bidId.equals(RbMapUtils.getString(b, ID))).findAny().orElse(null);
                expectedBidsBuilder.append(bid == null ? ", {}"
                        : String.format(", { status: 'OK', tObject: {$bidId:'%s', $status:'SENT', rfp: { specifications: {sentDate: '%s'}} } }",
                        bid.get(ID), RbMapUtils.getString(bid, SENT_DATE)));
            }

            final String expectedResponse = "{ data: [ " + expectedBidsBuilder.delete(0, 2).toString() + "]}";
            JSONAssert.assertEquals(expectedResponse, responseBody, JSONCompareMode.LENIENT);
        }

        private EmailTemplate getEmail(List<EmailTemplate> emails, InvitationImpl invitation) {
            final String invitationToken = invitation.getToken();
            return emails.stream().filter( e -> invitationToken.equals(e.getId()) )
                    .findFirst().orElse(null);
        }

        private InvitationImpl assertThatInvitationIsCreatedForBidSupplierContact(List<Document> bids, String... bidsIds) {
            final List<InvitationImpl> invitations = mongoDb.getCollection(InvitationCollection.COLLECTION_NAME, InvitationImpl.class)
                    .find(in(InvitationCollection.TARGET_ID, oid(Arrays.asList(bidsIds)))).into(new ArrayList<>());
            assertEquals(1, invitations.size());
            final InvitationImpl invitation = invitations.get(0);
            assertCreatedInvitationForBidSupplierContact(invitation, bids);
            return invitation;
        }

        private void assertCreatedInvitationForBidSupplierContact(InvitationImpl invitation, List<Document> bids){
            final Document bidUsedForInvitation = bids.stream()
                    .filter(b -> invitation.getTargetId().equals(b.getObjectId(ID))).findFirst().orElse(null);

            InvitationImplAssert.that(invitation)
                    .hasFirstName(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_FIRST_NAME))
                    .hasLastName(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_LAST_NAME))
                    .hasEmailAddress(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_EMAIL_ADDRESS))
                    .hasPhone(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_PHONE))
                    .hasJobTitle(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_JOB_TITLE))
                    .hasAccountId(oid(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_COMPANY_ACCOUNT_ID)))
                    .hasAccountName(RbMapUtils.getString(bidUsedForInvitation, SUPPLIER_CONTACT_COMPANY_NAME))
                    .hasTargetId(oid(RbMapUtils.getString(bidUsedForInvitation, ID)));
        }

        private Document getBid(String bidId) {
            return mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME).find(byId(oid(bidId))).first();
        }

        private List<Document> assertThatSentBidsAreSaved(String... bidsIds) throws JSONException {
            final List<Document> bids =  mongoDb.getCollection(HotelRfpBidCollection.COLLECTION_NAME)
                    .find(byIds(Arrays.asList(bidsIds))).into(new ArrayList<>());
            for (Document bid : bids) { assertThatSentBidIsSaved(bid); }
            return bids;
        }

        private void assertThatSentBidIsSaved(Document bid) throws JSONException {
            JSONAssert.assertEquals(String.format(
                    "{state: {status: 'SENT'}, rfp: { specifications: { sentDate: %s}} }", LocalDate.now()
            ), bid.toJson(), JSONCompareMode.LENIENT);
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
            entity = new HttpEntity<>(request, headers);
        }
    }
}
