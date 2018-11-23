package net.readybid.api.rfp_hotel.bid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("[E2E] Send Bids")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class SendBidsToSelectedContactE2ETest {



    /**
     *
     * TODO:
     *
     * 10 Send to Multiple Hotels (2) and Chains (2)
     *
     * 10.10 Checks:
     * 10.10.5 Buyer has permission to Update on all Bids
     * 10.10.10 Bid Due Date for all Bids and Ignore Due Date flag
     *
     * 10.20. Update Bids
     * 10.20.10 filters: bidsIds, Bid Status == CREATED or SENT, Bid has Supplier
     * 10.20.20 updates: Bid Status to SENT, Bid Sent Date
     *
     * 10.30 Return Action Report with Updated Bids
     * 10.30.10 Success if Bid is in SENT Status with expected statusAt and expected supplierId
     * 10.30.20 Bid deleted
     * 10.30.30 Bid state changed
     * 10.30.40 Bid without supplier (?? supplier deleted ??)
     *
     * 10.35 Create BM Views for Chain Reps !! skipped for now !!
     * 10.35.10 We need to think about this... problem are Chain Contacts, they are not users when bid is sent to them
     *
     * 10.40 Send Emails to Success Bids
     * 10.40.10 Send every bid to every hotel
     * 10.40.20 Send grouped bids by RFP to every chain rep
     *
     * 20 Send to 1 Hotel
     *
     * 30 Send to 1 Chain
     *
     * Errors:
     * Fail on any not authorized
     *
     */


}
