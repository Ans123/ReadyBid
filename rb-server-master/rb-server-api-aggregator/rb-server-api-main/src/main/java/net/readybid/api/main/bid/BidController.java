package net.readybid.api.main.bid;

import net.readybid.api.main.pdf.PdfService;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.gate.api.rfps.EditQuestionnaireView;
import net.readybid.app.gate.api.rfps.QuestionnaireView;
import net.readybid.app.gate.api.rfps.hotel.HotelRfpBidResponseView;
import net.readybid.app.gate.api.rfps.hotel.HotelRfpBidView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.contact.RfpContactViewModel;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.specifications.UpdateDueDateRequest;
import net.readybid.rfp.specifications.UpdateProgramPeriodRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpBidNegotiationsViewModel;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import net.readybid.rfphotel.letter.LetterViewModel;
import net.readybid.rfphotel.letter.UpdateLetterTemplateRequest;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.HotelRfpSupplierViewModel;
import net.readybid.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
@RestController
@RequestMapping(value = "/rfps/bids")
public class BidController {

    private final BidFacade bidFacade;
    private final PdfService pdfService;

    @Autowired
    public BidController(BidFacade bidFacade, PdfService pdfService) {
        this.bidFacade = bidFacade;
        this.pdfService = pdfService;
    }

    @RbResponseView
    @PostMapping(value = "/query")
    public RbViewModel queryBids(@Valid @RequestBody BidsQueryRequest bidsQueryRequest,   @CurrentUser AuthenticatedUser user )
    {
        return new RbListViewModel<>(bidFacade.queryBids(bidsQueryRequest, user));
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public GetResponse<HotelRfpBid, HotelRfpBidView> getBidPreview(
            @RequestParam(value = "token") String token
    ) {
        final GetResponse<HotelRfpBid, HotelRfpBidView> response = new GetResponse<>();
        final HotelRfpBid bid = bidFacade.getTokenBidPreview(token);
        return response.finalizeResult(new HotelRfpBidView(bid));
    }

    @RequestMapping(value = "/{bidId}/preview", method = RequestMethod.GET)
    public GetResponse<HotelRfpBid, HotelRfpBidView> getBid(
            @PathVariable(value = "bidId") String bidId,
            @RequestParam(value = "placeholders", defaultValue = "0", required = false) String placeholders,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        final GetResponse<HotelRfpBid, HotelRfpBidView> response = new GetResponse<>();
        final HotelRfpBid bid = bidFacade.getBidWithPreviews(bidId, placeholders.equals("1"));
        return response.finalizeResult(new HotelRfpBidView(bid, currentUser));
    }

    @RequestMapping(value = "/{bidId}/specifications/due-date", method = RequestMethod.PUT)
    public ActionResponse updateBidDueDate(
            @RequestBody @Valid UpdateDueDateRequest updateDueDateRequest,
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();

        bidFacade.updateBidDueDate(bidId, updateDueDateRequest.dueDate, user);
        return actionResponse.finalizeAction();
    }

    @RequestMapping(value = "/{bidId}/specifications/program-period", method = RequestMethod.PUT)
    public ActionResponse updateBidProgramPeriod(
            @RequestBody @Valid UpdateProgramPeriodRequest updateProgramPeriodRequest,
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();
        updateProgramPeriodRequest.validate();
        bidFacade.updateBidProgramPeriod(bidId, updateProgramPeriodRequest.programStartDate, updateProgramPeriodRequest.programEndDate, user);
        return actionResponse.finalizeAction();
    }

    @RequestMapping(value = "/{bidId}/cover-letter/template", method = RequestMethod.GET)
    public GetResponse<String, LetterViewModel> getBidCoverLetterTemplate(
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final GetResponse<String, LetterViewModel> getResponse = new GetResponse<>();

        final String coverLetter = bidFacade.getBidCoverLetterTemplate(bidId, user);
        return getResponse.finalizeResult(coverLetter, LetterViewModel.FACTORY);
    }

    @RequestMapping(value = "/{bidId}/cover-letter/template", method = RequestMethod.PUT)
    public WriteResponse updateBidCoverLetterTemplate(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid UpdateLetterTemplateRequest updateLetterTemplateRequest,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        bidFacade.updateBidCoverLetterTemplate(bidId, updateLetterTemplateRequest.getSanitizedTemplate(), user);
        return writeResponse.finalizeResponse(null, bidId);
    }

    @RequestMapping(value = "/{bidId}/questionnaire/model", method = RequestMethod.GET)
    public GetResponse<Questionnaire, EditQuestionnaireView> getBidQuestionnaireModel(
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final GetResponse<Questionnaire, EditQuestionnaireView> getResponse = new GetResponse<>();

        final Questionnaire questionnaire = bidFacade.getBidQuestionnaireModel(bidId, user);
        return  getResponse.finalizeResult(questionnaire, EditQuestionnaireView.FACTORY);
    }

    @RequestMapping(value = "/{bidId}/questionnaire/model", method = RequestMethod.PUT)
    public WriteResponse updateBidQuestionnaire(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid UpdateQuestionnaireModelRequest updateQuestionnaireRequest,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        bidFacade.updateBidQuestionnaire(bidId, updateQuestionnaireRequest, user);
        return writeResponse.finalizeResponse(null, bidId);
    }

    @Deprecated // 2017-09-05
    @RequestMapping(value = "/{bidId}/supplier/contact", method = RequestMethod.GET)
    public GetResponse<RfpContact, RfpContactViewModel> getBidSupplierContact(
            @PathVariable(value = "bidId") String bidId
    ) {
        final GetResponse<RfpContact, RfpContactViewModel> response = new GetResponse<>();

        final RfpContact contact = bidFacade.getBidSupplierContact(bidId);
        return response.finalizeResult(contact, RfpContactViewModel.FACTORY);
    }

    @RequestMapping(value = "/{bidId}/response", method = RequestMethod.GET)
    public GetResponse<HotelRfpBid, HotelRfpBidResponseView> getBidResponse(
            @PathVariable(value = "bidId") String bidId
    ) {
        final GetResponse<HotelRfpBid, HotelRfpBidResponseView> response = new GetResponse<>();
        final HotelRfpBid bid = bidFacade.getBidResponse(bidId);
        return response.finalizeResult(bid, HotelRfpBidResponseView.FACTORY);
    }

    @RbResponseView
    @GetMapping(value = "/{bidId}/response-draft")
    public RbViewModel getBidResponseWithDraft(
            @PathVariable(value = "bidId") String bidId
    ) {
        final Questionnaire questionnaire = bidFacade.getBidQuestionnaireWithResponseDraft(bidId);
        return new RbViewModel() {
            @Override
            public Object getData() {
                return new QuestionnaireView(questionnaire);
            }
        };
    }

    @RequestMapping(value = "/{bidId}/negotiations", method = RequestMethod.GET)
    public GetResponse<HotelRfpNegotiations, HotelRfpBidNegotiationsViewModel> getBidNegotiations(
            @PathVariable(value = "bidId") String bidId
    ) {
        final GetResponse<HotelRfpNegotiations, HotelRfpBidNegotiationsViewModel> response = new GetResponse<>();
        final HotelRfpNegotiations negotiations = bidFacade.getBidNegotiations(bidId);
        return response.finalizeResult(negotiations, HotelRfpBidNegotiationsViewModel.FACTORY);
    }

    @RbResponseView
    @RequestMapping(value = "/{bidId}/negotiations", method = RequestMethod.POST)
    public RbViewModel addNegotiation(
            @PathVariable(value = "bidId") String bidId,
            @Valid @RequestBody NegotiationRequest negotiationRequest,
            @CurrentUser AuthenticatedUser user
    ) {
        return bidFacade.addBidNegotiations(bidId, negotiationRequest, user);
    }

    @RbResponseView
    @RequestMapping(value = "/{bidId}/negotiations/{negotiationId}", method = RequestMethod.PUT)
    public RbViewModel updateNegotiation(
            @PathVariable(value = "bidId") String bidId,
            @PathVariable(value = "negotiationId") String negotiationId,
            @Valid @RequestBody NegotiationRequest negotiationRequest,
            @CurrentUser AuthenticatedUser user
    ) {
        return bidFacade.updateBidNegotiations(bidId, negotiationId, negotiationRequest, user);
    }

    @RbResponseView
    @RequestMapping(value = "/{bidId}/negotiations/finalize", method = RequestMethod.POST)
    public RbViewModel addNegotiationAndFinalizeNegotiations(
            @PathVariable(value = "bidId") String bidId,
            @Valid @RequestBody NegotiationRequest negotiationRequest,
            @CurrentUser AuthenticatedUser user
    ) {
        return bidFacade.addAndFinalizeNegotiations(bidId, negotiationRequest, user);
    }

    @RbResponseView
    @RequestMapping(value = "/{bidId}/negotiations/{negotiationId}/finalize", method = RequestMethod.PUT)
    public RbViewModel updateNegotiationAndFinalizeNegotiations(
            @PathVariable(value = "bidId") String bidId,
            @PathVariable(value = "negotiationId") String negotiationId,
            @Valid @RequestBody NegotiationRequest negotiationRequest,
            @CurrentUser AuthenticatedUser user
    ) {
        return bidFacade.updateAndFinalizeNegotiations(bidId, negotiationId, negotiationRequest, user);
    }

    @RequestMapping(value = "/{bidId}/supplier", method = RequestMethod.GET)
    public GetResponse<HotelRfpSupplier, HotelRfpSupplierViewModel> getBidSupplier(
            @PathVariable(value = "bidId") String bidId
    ) {
        final GetResponse<HotelRfpSupplier, HotelRfpSupplierViewModel> response = new GetResponse<>();
        final HotelRfpSupplier supplier = bidFacade.getBidSupplier(bidId);
        return response.finalizeResult(supplier, HotelRfpSupplierViewModel.FACTORY);
    }

    @RequestMapping(value = "/{bidId}/final-agreement/pdf", method = RequestMethod.GET, produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public FileSystemResource getFinalAgreementAsPdf(
            @PathVariable(value = "bidId") String bidId
    ) {
        final String finalAgreementHtml = bidFacade.getFinalAgreement(bidId);
        final File file = pdfService.create(finalAgreementHtml);
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/{bidId}/final-agreement/send", method = RequestMethod.PUT)
    public GetResponse<HotelRfpBid, HotelRfpBidView> sendFinalAgreement(
            @PathVariable(value = "bidId") String bidId,
            @CurrentUser AuthenticatedUser user
    ) {
        final GetResponse<HotelRfpBid, HotelRfpBidView> response = new GetResponse<>();

        final HotelRfpBid updatedBid = bidFacade.sendFinalAgreement(bidId, user);
        return response.finalizeResult(new HotelRfpBidView(updatedBid, user));
    }

    @RequestMapping(value = "/{bidId}/final-agreement/template", method = RequestMethod.GET)
    public GetResponse<String, LetterViewModel> getBidFinalAgreementTemplate(
            @PathVariable(value = "bidId") String bidId
    ) {
        final GetResponse<String, LetterViewModel> getResponse = new GetResponse<>();

        final String template = bidFacade.getBidFinalAgreementTemplate(bidId);
        return getResponse.finalizeResult(template, LetterViewModel.FACTORY);
    }

    @RequestMapping(value = "/{bidId}/final-agreement/template", method = RequestMethod.PUT)
    public WriteResponse updateBidFinalAgreementTemplate(
            @PathVariable(value = "bidId") String bidId,
            @RequestBody @Valid UpdateLetterTemplateRequest updateLetterTemplateRequest,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);

        bidFacade.updateBidFinalAgreementTemplate(bidId, updateLetterTemplateRequest, user);
        return writeResponse.finalizeResponse(null, bidId);
    }
}