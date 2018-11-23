package net.readybid.api.rfp_hotel.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.bid.HotelRfpAcceptRatesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.exceptions.BadRequestException;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static net.readybid.app.entities.rfp.questionnaire.form.section.QuestionnaireHotelRfpRateGrid.RATE_PATTERN;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class AcceptRatesController {

    private final BidAccessControlService bidAccessControlService;
    private final HotelRfpAcceptRatesHandler handler;

    @Autowired
    public AcceptRatesController(BidAccessControlService bidAccessControlService, HotelRfpAcceptRatesHandler handler) {
        this.bidAccessControlService = bidAccessControlService;
        this.handler = handler;
    }

    @RbResponseView
    @PostMapping(value = "{bidId}/accept")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel sendBidsToSelectedSupplierContact(
            @PathVariable(name = "bidId") String bidId,
            @RequestBody @Valid HotelRfpAcceptRatesRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        bidAccessControlService.updateAsBuyer(bidId);
        return handler.acceptRates(bidId, request.getValidatedAcceptedRates(), currentUser);
    }

    public static class HotelRfpAcceptRatesRequest {

        @NotNull
        public List<String> acceptedRates;

        List<String> getValidatedAcceptedRates(){
            if(acceptedRates.isEmpty() || containsInvalidValues(acceptedRates))
                throw new BadRequestException();
            return acceptedRates;
        }

        private static boolean containsInvalidValues(List<String> acceptedRates) {
            for(String ar : acceptedRates){
                if(ar == null || ar.isEmpty() || !RATE_PATTERN.matcher(ar).matches()) return true;
            }
            return false;
        }
    }
}