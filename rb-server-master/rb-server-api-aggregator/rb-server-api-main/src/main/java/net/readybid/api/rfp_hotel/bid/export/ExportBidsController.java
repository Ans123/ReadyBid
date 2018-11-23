package net.readybid.api.rfp_hotel.bid.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.use_cases.rfp_hotel.bid.HotelRfpBidReportFactory;
import net.readybid.app.use_cases.rfp_hotel.bid.implementation.reports.HotelRfpFinalAgreementReport;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.file_export.FileExportView;
import net.readybid.file_export.common.FileExportService;
import net.readybid.validators.Ids;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rfps/hotel/bids/")
public class ExportBidsController {

    private final BidAccessControlService bidAccessControlService;
    private final HotelRfpBidReportFactory reportFactory;
    private final FileExportService fileExportService;
    private final ObjectMapper mapper;

    @Autowired
    public ExportBidsController(
            BidAccessControlService bidAccessControlService,
            HotelRfpBidReportFactory reportFactory,
            FileExportService fileExportService,
            ObjectMapper mapper
    ) {
        this.bidAccessControlService = bidAccessControlService;
        this.reportFactory = reportFactory;
        this.fileExportService = fileExportService;
        this.mapper = mapper;
    }

    @PostMapping(value = "export")
    @ResponseStatus(HttpStatus.OK)
    public void exportBids(
            @Valid @RequestBody ExportBidsRequest exportBidsRequest,
            @CurrentUser AuthenticatedUser currentUser,
            HttpServletResponse response
    ) throws IOException {
        bidAccessControlService.readAsAny(exportBidsRequest.bids);
        fileExportService.setResponse(response, getFileExportView(exportBidsRequest, currentUser));
    }

    private FileExportView getFileExportView(ExportBidsRequest exportBidsRequest, AuthenticatedUser currentUser) {
        switch (exportBidsRequest.name){
            case "HOTEL_RFP:BID_MANAGER:EXCEL":
                return new BidManagerExportExcelView.Builder()
                        .setBidsIds(exportBidsRequest.bids)
                        .setOptions(exportBidsRequest.options)
                        .setReportFactory(reportFactory)
                        .setMapper(mapper)
                        .setCurrentUser(currentUser)
                        .createBidManagerExportExcelView();
            case "HOTEL_RFP:GBTA:EXCEL":
                return new GbtaExportExcelView(getGbtaAnswers(exportBidsRequest));
            case "HOTEL_RFP:GBTA:CSV":
                return new GbtaExportCsvView(getGbtaAnswers(exportBidsRequest));
            case "HOTEL_RFP:FINAL_AGREEMENT:EXCEL":
                return generateFinalAgreementExport(exportBidsRequest);
            default:
                throw new NotFoundException();
        }
    }

    private FileExportView generateFinalAgreementExport(ExportBidsRequest exportBidsRequest) {
        final boolean includeUnsent = exportBidsRequest.options != null && Boolean.parseBoolean(String.valueOf(exportBidsRequest.options.get("includeUnsent")));
        final RbListViewModel<HotelRfpFinalAgreementReport> reports =
                reportFactory.getHotelRfpFinalAgreementReports(exportBidsRequest.bids, includeUnsent);
        return new FinalAgreementsExportExcelView(reports.getData());
    }

    private RbViewModel getGbtaAnswers(ExportBidsRequest exportBidsRequest) {
        return reportFactory.getGbtaAnswers(exportBidsRequest.bids);
    }

    @SuppressWarnings("WeakerAccess")
    public static class ExportBidsRequest {

        @NotNull
        @Size(max = 50)
        public String name;

        @NotNull
        @Ids
        public List<String> bids;

        public Map<String, Object> options;
    }
}
