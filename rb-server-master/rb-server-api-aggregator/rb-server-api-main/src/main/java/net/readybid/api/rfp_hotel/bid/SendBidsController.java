package net.readybid.api.rfp_hotel.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.bid.SendHotelRfpBidHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class SendBidsController {

    private final BidAccessControlService bidAccessControlService;
    private final SendHotelRfpBidHandler sendBidHandler;

    public SendBidsController(BidAccessControlService bidAccessControlService, SendHotelRfpBidHandler sendBidHandler) {
        this.bidAccessControlService = bidAccessControlService;
        this.sendBidHandler = sendBidHandler;
    }

    @RbResponseView
    @PostMapping(value = "send")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel sendBids(
            @RequestBody @Valid HotelRfpSendBidsWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        return sendBids(request, bidsIds ->
                sendBidHandler.send(bidsIds, currentUser, request.ignoreDueDate));
    }

    @RbResponseView
    @PostMapping(value = "send/to/new")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel sendBidsToNewSupplierContact(
            @RequestBody @Valid HotelRfpSendBidsToNewContactWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        return sendBids(request, bidsIds ->
                sendBidHandler.sendToNewContact(bidsIds, request.toSupplierContact(), currentUser, request.ignoreDueDate));
    }

    @RbResponseView
    @PostMapping(value = "send/to/{userAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel sendBidsToSelectedSupplierContact(
            @PathVariable (name = "userAccountId") String userAccountId,
            @RequestBody @Valid HotelRfpSendBidsWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        return sendBids(request, bidsIds ->
                sendBidHandler.sendToSelectedContact(bidsIds, userAccountId, currentUser, request.ignoreDueDate));
    }

    private RbViewModel sendBids(HotelRfpSendBidsWebRequest request, Function<List<String>, RbViewModel> handler){
        bidAccessControlService.updateAsBuyer(request.bids);
        return handler.apply(request.bids);
    }
}
