package net.readybid.api.rfp_hotel.rfp;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.rfp.HotelRfpCoverLetterHandler;
import net.readybid.web.HtmlInputSecurityPolicy;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequestMapping(value = "/rfps/hotel/")
public class HotelRfpCoverLetterController {

    private final RfpAccessControlService rfpAccessControlService;
    private final HotelRfpCoverLetterHandler hotelRfpCoverLetterHandler;

    public HotelRfpCoverLetterController(
            RfpAccessControlService rfpAccessControlService,
            HotelRfpCoverLetterHandler hotelRfpCoverLetterHandler
    ) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.hotelRfpCoverLetterHandler = hotelRfpCoverLetterHandler;
    }

    @RbResponseView
    @GetMapping(value = "{rfpId}/nam-cover-letter/template")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel getNamCoverLetterTemplate(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        rfpAccessControlService.update(rfpId);
        return hotelRfpCoverLetterHandler.getNamCoverLetterTemplate(rfpId);
    }

    @RbResponseView
    @PutMapping(value = "{rfpId}/nam-cover-letter/template")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setNamCoverLetterTemplate(
            @PathVariable(value = "rfpId") String rfpId,
            @Valid @RequestBody SetCoverLetterRequest request
            ) {
        rfpAccessControlService.update(rfpId);
        return hotelRfpCoverLetterHandler.setNamCoverLetterTemplate(rfpId, request.getSanitizedTemplate());
    }

    public static class SetCoverLetterRequest{

        @NotBlank
        @Size(max = 1000000)
        public String template;

        public String getSanitizedTemplate(){
            final String sanitized = HtmlInputSecurityPolicy.POLICY_DEFINITION.sanitize(template);
            return sanitized.replaceAll("\\{<!-- -->\\{", "{{");
        }
    }
}