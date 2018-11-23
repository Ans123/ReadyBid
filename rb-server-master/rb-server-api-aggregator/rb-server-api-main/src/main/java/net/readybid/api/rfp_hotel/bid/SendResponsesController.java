package net.readybid.api.rfp_hotel.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.entities.PrimitivesHelper;
import net.readybid.app.use_cases.rfp_hotel.bid.SetDraftResponsesHandler;
import net.readybid.app.use_cases.rfp_hotel.bid.SetResponsesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.validators.Ids;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class SendResponsesController {

    private final BidAccessControlService bidAccessControlService;
    private final SetDraftResponsesHandler setDraftResponsesHandler;
    private final SetResponsesHandler setResponsesHandler;

    @Autowired
    public SendResponsesController(
            BidAccessControlService bidAccessControlService,
            SetDraftResponsesHandler setDraftResponsesHandler,
            SetResponsesHandler setResponsesHandler
    ) {
        this.bidAccessControlService = bidAccessControlService;
        this.setDraftResponsesHandler = setDraftResponsesHandler;
        this.setResponsesHandler = setResponsesHandler;
    }

    @RbResponseView
    @PostMapping(value = "{bidId}/response/draft")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setDraftResponse(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid SaveResponseWebRequest webRequest,
            @CurrentUser AuthenticatedUser currentUser
    ){
        bidAccessControlService.updateAsSupplier(bidId);
        return setDraftResponsesHandler.setResponse(bidId, webRequest.getResponse(), currentUser);
    }

    @RbResponseView
    @PostMapping(value = "responses/draft")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setDraftResponses(
            @RequestBody @Valid SaveResponsesWebRequest webRequest,
            @CurrentUser AuthenticatedUser currentUser
    ){
        bidAccessControlService.updateAsSupplier(webRequest.bidsIds);
        return setDraftResponsesHandler.setResponses(webRequest.bidsIds, webRequest.getResponses(), currentUser);
    }

    @RbResponseView
    @PostMapping(value = "{bidId}/response")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setResponse(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid SaveResponseWebRequest webRequest,
            @CurrentUser AuthenticatedUser currentUser
    ){
        final boolean ignoreErrors = PrimitivesHelper.unboxSafely(webRequest.ignoreErrors);
        bidAccessControlService.updateAsSupplier(bidId);
        return setResponsesHandler.setResponse(bidId, webRequest.getResponse(), ignoreErrors, currentUser);
    }

    @RbResponseView
    @PostMapping(value = "responses")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setResponses(
            @RequestBody @Valid SendResponsesWebRequest webRequest,
            @CurrentUser AuthenticatedUser currentUser
    ){
        final boolean ignoreErrors = PrimitivesHelper.unboxSafely(webRequest.ignoreErrors);
        bidAccessControlService.updateAsSupplier(webRequest.bidsIds);
        return setResponsesHandler.setResponses(webRequest.bidsIds, ignoreErrors, currentUser);
    }

    @SuppressWarnings("WeakerAccess")
    public static class SaveResponsesWebRequest {

        @NotNull
        @Ids
        public List<String> bidsIds;

        @NotNull
        @HotelRfpBidsResponses
        public List<Map<String, Object>> responses;

        public List<Map<String, String>> getResponses() {
            return responses.stream().map(SaveResponseWebRequest::convertAnswersToString).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class SaveResponseWebRequest {

        @NotNull
        @HotelRfpBidsResponse
        public Map<String, Object> response;

        public Boolean ignoreErrors;

        public Map<String, String> getResponse() {
            return convertAnswersToString(response);
        }

        private static Map<String, String> convertAnswersToString(Map<String,Object> answers) {
            final Map<String, String> validatedAnswers = new HashMap<>(answers.size());
            final BiConsumer<String, Object> filterAndConvert = createFilter(validatedAnswers);
            answers.forEach(filterAndConvert);
            return validatedAnswers;
        }

        private static BiConsumer<String, Object> createFilter(Map<String, String> validatedAnswers) {
            return (key, value) -> {
                final String v = value == null ? null : String.valueOf(value);
                if(isAcceptable(key) && isAcceptable(v)) validatedAnswers.put(key, v);
            };
        }

        private static boolean isAcceptable(String s) {
            return s != null && !s.isEmpty();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class SendResponsesWebRequest {

        @NotNull
        @Ids
        public List<String> bidsIds;

        public Boolean ignoreErrors;
    }
}
