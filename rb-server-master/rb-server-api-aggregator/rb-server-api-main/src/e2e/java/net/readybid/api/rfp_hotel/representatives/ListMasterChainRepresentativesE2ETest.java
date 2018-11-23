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

import static org.junit.jupiter.api.Assertions.assertSame;


@DisplayName("[E2E] List Master Chain Representatives ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class ListMasterChainRepresentativesE2ETest {

    private static final String URL_CHAIN = "/rfps/hotel/representatives/chain/%s";

    @Autowired
    private MongoDbHelperService mongoDbHelperService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorizationHelperService authorizationHelperService;

    private HttpHeaders headers;

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
        final HttpEntity entity = new HttpEntity(headers);
        final String chainId = RbRandom.idAsString();

        final ResponseEntity<String> response = restTemplate
                .exchange(String.format(URL_CHAIN, chainId), HttpMethod.GET, entity, String.class);

        assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @DisplayName("as User ")
    @Nested
    class AsAuthenticatedUser {

        private AuthenticatedUser user;

        final String chainEntityId = "56014c0a9537801174120001";

        @BeforeEach
        void setUp(){
            user = AuthenticatedUserImplFactory.random(AccountTestFactory.random(EntityType.COMPANY, "56014c0a9537801174120003", Permission.BUYER_UPDATE));
            authorizationHelperService.authorize(headers, user);
        }

        @DisplayName("should return empty List when there are no representatives")
        @Test
        void shouldReturnEmptyListWhenNoRepresenatives() throws Exception {
            final HttpEntity entity = new HttpEntity(headers);


            final ResponseEntity<String> response = restTemplate
                    .exchange(String.format(URL_CHAIN, chainEntityId), HttpMethod.GET, entity, String.class);

            assertSame(HttpStatus.OK, response.getStatusCode());
            JSONAssert.assertEquals("{ data: [], count: 0 } ", response.getBody(), JSONCompareMode.LENIENT);
        }

        @DisplayName("should return the List with unique Active Chain Representatives and most recent unique by email Contacts from Sent Bids")
        @Test
        void shouldReturnTheList() throws Exception {
            mongoDbHelperService.load("ListMasterChainRepresentatives.json");
            final HttpEntity entity = new HttpEntity(headers);

            final ResponseEntity<String> response = restTemplate
                    .exchange(String.format(URL_CHAIN, chainEntityId), HttpMethod.GET, entity, String.class);

            assertSame(HttpStatus.OK, response.getStatusCode());
            final String expectedStr = "{ data: [ " +
                    "{ id: '59df7063bb832a05ecde0001', isUser: true , userId: '59ad5164bb832a251cde0001', entityId: '56014c0a9537801174120001', accountId: '59b152fbbb832a069cd20001', firstName: 'Test 1', lastName: 'Test 1', emailAddress: 'chainRep_1@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'CHAIN REP not Used in Bids', accountName: 'CHAIN 1', accountType: 'CHAIN'},  " +
                    "{ id: '59df7063bb832a05ecde0002', isUser: true },  " +
                    "{ id: '59df7063bb832a05ecde0003', isUser: true },  " +
                    "{ id: '5a4508dabb832a2258950001', isUser: false, entityId: '56014c0a9537801174120001', accountId: '59b152fbbb832a069cd20001', firstName: 'Chain Rep From Bid', lastName: '10', emailAddress: 'chainRepFromBid@gmail.com', phone: '1234567', profilePicture: 'picture.jpg', jobTitle: 'Bid Contact', accountName: 'CHAIN 1', accountType: 'CHAIN' },  " +
                    "{ id: '5a4508dabb832a2258950003', isUser: false }  " +
                    "], count: 5}";
            JSONAssert.assertEquals(expectedStr, response.getBody(), JSONCompareMode.LENIENT);
        }
    }

}