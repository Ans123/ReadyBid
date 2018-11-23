package net.readybid.api.rfp_hotel.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.bid.DeleteHotelRfpBidHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class DeleteBidsController {

    private final BidAccessControlService bidAccessControlService;
    private final DeleteHotelRfpBidHandler deleteBidHandler;

    @Autowired
    public DeleteBidsController(
            BidAccessControlService bidAccessControlService,
            DeleteHotelRfpBidHandler deleteBidHandler
    ) {
        this.bidAccessControlService = bidAccessControlService;
        this.deleteBidHandler = deleteBidHandler;
    }

    @RbResponseView
    @PostMapping(value = "delete")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel deleteBids(
            @Valid @RequestBody BidsActionRequest deleteBidsRequest,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        final List<String> bidsIds = deleteBidsRequest.bids;
        bidAccessControlService.updateAsBuyer(bidsIds);
        return deleteBidHandler.deleteBids(bidsIds, currentUser);
    }
}
