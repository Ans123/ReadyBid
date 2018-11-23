package net.readybid.api.main.rfp.core;

import net.readybid.api.main.rfp.hotel.traveldestination.update.UpdateTravelDestinationFilterWebRequest;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.gate.api.rfps.EditQuestionnaireView;
import net.readybid.app.gate.api.rfps.RfpViewModel;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.rfp.core.CreateRfpRequest;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.specifications.UpdateDueDateRequest;
import net.readybid.rfp.specifications.UpdateProgramPeriodRequest;
import net.readybid.rfp.specifications.UpdateRfpNameRequest;
import net.readybid.rfphotel.CreateBidRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.letter.LetterViewModel;
import net.readybid.rfphotel.letter.UpdateLetterTemplateRequest;
import net.readybid.web.ActionResponse;
import net.readybid.web.GetResponse;
import net.readybid.web.WriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
@RestController
@RequestMapping(value = "/rfps")
public class RfpController {

    private final RfpFacade rfpFacade;

    @Autowired
    public RfpController(RfpFacade rfpFacade) { this.rfpFacade = rfpFacade; }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public WriteResponse createRfp(
            @RequestBody @Valid CreateRfpRequest createRfpRequest,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        final Rfp rfp = rfpFacade.createRfpFromTemplate(createRfpRequest.id, user);

        final Map<String, String> data = new HashMap<>();
        final String id = String.valueOf(rfp.getId());
        data.put("name", rfp.getName());
        data.put("id", id);
        return writeResponse.finalizeResponse(data, id);
    }

    @RequestMapping(value = "/{rfpId}/preview", method = RequestMethod.GET)
    public GetResponse<Rfp, RfpViewModel> getRfp(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final GetResponse<Rfp, RfpViewModel> getResponse = new GetResponse<>();

        final Rfp rfp = rfpFacade.getRfpWithPreviews(rfpId);
        return getResponse.finalizeResult(rfp, RfpViewModel.FACTORY);
    }

    @RequestMapping(value = "/{rfpId}/set-default-filter", method = RequestMethod.PUT)
    public ActionResponse setDefaultFilter(
            @RequestBody @Valid UpdateTravelDestinationFilterWebRequest requestData,
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final ActionResponse ac = new ActionResponse();
        rfpFacade.updateFilter(rfpId, requestData.getModel(""));
        return ac.finalizeAction();
    }

    @RequestMapping(value = "/{rfpId}/specifications/name", method = RequestMethod.PUT)
    public ActionResponse updateRfpName(
            @RequestBody @Valid UpdateRfpNameRequest updateRfpNameRequest,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();

        rfpFacade.updateRfpName(rfpId, updateRfpNameRequest.name, user);
        return actionResponse.finalizeAction();
    }

    @RequestMapping(value = "/{rfpId}/specifications/due-date", method = RequestMethod.PUT)
    public ActionResponse updateRfpDueDate(
            @RequestBody @Valid UpdateDueDateRequest updateDueDateRequest,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();

        rfpFacade.updateRfpDueDate(rfpId, updateDueDateRequest.dueDate, user);
        return actionResponse.finalizeAction();
    }

    @RequestMapping(value = "/{rfpId}/specifications/program-period", method = RequestMethod.PUT)
    public ActionResponse updateRfpProgramPeriod(
            @RequestBody @Valid UpdateProgramPeriodRequest updateProgramPeriodRequest,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();
        updateProgramPeriodRequest.validate();
        rfpFacade.updateRfpProgramPeriod(rfpId, updateProgramPeriodRequest.programStartDate, updateProgramPeriodRequest.programEndDate, user);
        return actionResponse.finalizeAction();
    }

    @RequestMapping(value = "/{rfpId}/cover-letter", method = RequestMethod.GET)
    public GetResponse<String, LetterViewModel> getRfpCoverLetterTemplate(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final GetResponse<String, LetterViewModel> getResponse = new GetResponse<>();

        final String coverLetter = rfpFacade.getRfpCoverLetterTemplate(rfpId);
        return  getResponse.finalizeResult(coverLetter, LetterViewModel.FACTORY);
    }

    @RequestMapping(value = "/{rfpId}/cover-letter", method = RequestMethod.PUT)
    public WriteResponse updateRfpCoverLetterTemplate(
            @PathVariable(value = "rfpId") String rfpId,
            @RequestBody @Valid UpdateLetterTemplateRequest updateLetterTemplateRequest,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        rfpFacade.updateRfpCoverLetterTemplate(rfpId, updateLetterTemplateRequest.getSanitizedTemplate());
        return writeResponse.finalizeResponse(null, rfpId);
    }

    @RequestMapping(value = "/{rfpId}/questionnaire", method = RequestMethod.GET)
    public GetResponse<Questionnaire, EditQuestionnaireView> getRfpQuestionnaireModel(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final GetResponse<Questionnaire, EditQuestionnaireView> getResponse = new GetResponse<>();

        final Questionnaire questionnaire = rfpFacade.getQuestionnaireModel(rfpId);
        return  getResponse.finalizeResult(questionnaire, EditQuestionnaireView.FACTORY);
    }

    @RequestMapping(value = "/{rfpId}/questionnaire", method = RequestMethod.PUT)
    public WriteResponse updateRfpQuestionnaire(
            @PathVariable(value = "rfpId") String rfpId,
            @RequestBody @Valid UpdateQuestionnaireModelRequest updateQuestionnaireRequest,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        rfpFacade.updateRfpQuestionnaire(rfpId, updateQuestionnaireRequest);
        return writeResponse.finalizeResponse(null, rfpId);
    }

    @RequestMapping(value = "/{rfpId}/bids", method = RequestMethod.POST)
    public WriteResponse createBid(
            @Valid @RequestBody CreateBidRequest createBidRequest,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser currentUser,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        final HotelRfpBid bid = rfpFacade.createBid(rfpId, createBidRequest, currentUser);
        final Map<String, Object> data = new HashMap<>();
        data.put("id", bid.getId());
        data.put("state", Collections.singletonMap("status", bid.getStateStatus()));
        return writeResponse.finalizeResponse(data, bid.getId());
    }

    @RequestMapping(value = "/{rfpId}/bids/{bidId}", method = RequestMethod.DELETE)
    public ActionResponse deleteBid(
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();
        final HotelRfpBid bid = rfpFacade.deleteBid(rfpId, bidId, user);
        if(bid == null){
            throw new NotFoundException();
        } else {
            final Map<String, Object> data = new HashMap<>();
            data.put("id", bid.getId());
            data.put("state", Collections.singletonMap("status", bid.getStateStatus()));
            return actionResponse.finalizeAction(data);
        }
    }

    @RequestMapping(value = "/{rfpId}/final-agreement/template", method = RequestMethod.GET)
    public GetResponse<String, LetterViewModel> getRfpFinalAgreementTemplate(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final GetResponse<String, LetterViewModel> getResponse = new GetResponse<>();

        final String coverLetter = rfpFacade.getRfpFinalAgreementTemplate(rfpId);
        return  getResponse.finalizeResult(coverLetter, LetterViewModel.FACTORY);
    }

    @RequestMapping(value = "/{rfpId}/final-agreement/template", method = RequestMethod.PUT)
    public WriteResponse updateRfpFinalAgreementTemplate(
            @PathVariable(value = "rfpId") String rfpId,
            @RequestBody @Valid UpdateLetterTemplateRequest updateLetterTemplateRequest,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        rfpFacade.updateRfpFinalAgreementTemplate(rfpId, updateLetterTemplateRequest);
        return writeResponse.finalizeResponse(null, rfpId);
    }
}