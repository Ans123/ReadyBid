package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.bid.SetHotelRfpSupplierContactHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class HotelRfpSupplierContactController {

    private final BidAccessControlService bidAccessControlService;
    private final SetHotelRfpSupplierContactHandler setHotelRfpSupplierContactHandler;

    @Autowired
    public HotelRfpSupplierContactController(
            BidAccessControlService bidAccessControlService,
            SetHotelRfpSupplierContactHandler setHotelRfpSupplierContactHandler
    ) {
        this.bidAccessControlService = bidAccessControlService;
        this.setHotelRfpSupplierContactHandler = setHotelRfpSupplierContactHandler;
    }

    @RbResponseView
    @PostMapping(value = "{bidId}/supplier/contact/create")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel createSupplierContact(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid CreateHotelRfpSupplierContactWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
            ) {
        bidAccessControlService.updateAsBuyer(bidId);
        return setHotelRfpSupplierContactHandler.set(Collections.singletonList(bidId), request.toSupplierContact(), currentUser);
    }

    @RbResponseView
    @PostMapping(value = "{bidId}/supplier/contact/set")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setSupplierContact(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid SetHotelRfpSupplierContactWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        bidAccessControlService.updateAsBuyer(bidId);
        return setHotelRfpSupplierContactHandler.set(Collections.singletonList(bidId), request.userAccountId, currentUser);
    }

    @RbResponseView
    @PostMapping(value = "supplier/contact/create")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel createSupplierContacts(
            @RequestBody @Valid CreateHotelRfpSupplierContactsWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        bidAccessControlService.updateAsBuyer(request.bids);
        return setHotelRfpSupplierContactHandler.set(request.bids, request.toSupplierContact(), currentUser);
    }

    @RbResponseView
    @PostMapping(value = "supplier/contact/set")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setSupplierContacts(
            @RequestBody @Valid SetHotelRfpSupplierContactsWebRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        bidAccessControlService.updateAsBuyer(request.bids);
        return setHotelRfpSupplierContactHandler.set(request.bids, request.userAccountId, currentUser);
    }
}
