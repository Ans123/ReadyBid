package net.readybid.api.main.rfp.template;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfp.template.RfpTemplateListItemViewModel;
import net.readybid.app.gate.api.rfps.RfpTemplateViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
@RestController
@RequestMapping(value = "/rfps/templates")
public class RfpTemplateController {

    public final RfpTemplateFacade rfpTemplatesFacade;

    @Autowired
    public RfpTemplateController(RfpTemplateFacade rfpTemplatesFacade) {
        this.rfpTemplatesFacade = rfpTemplatesFacade;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ListResponse<RfpTemplateListItemViewModel> listRfpTemplates(
            @RequestParam(value = "rfp-type", required = true) String rfpType
    ) {
        final ListResponse<RfpTemplateListItemViewModel> listResponse = new ListResponse<>();
        final ListResult<RfpTemplateListItemViewModel> rfpTemplates = rfpTemplatesFacade.listRfpTemplates(rfpType);
        return listResponse.finalizeResult(rfpTemplates);
    }

    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET)
    public GetResponse<RfpTemplate, RfpTemplateViewModel> getRfpTemplatePreview(
            @PathVariable(value = "id") String id,
            @CurrentUser AuthenticatedUser user
    ) {
        final GetResponse<RfpTemplate, RfpTemplateViewModel> getResponse = new GetResponse<>();
        final RfpTemplate rfpTemplate = rfpTemplatesFacade.getRfpTemplatePreview(id, user);
        return getResponse.finalizeResult(rfpTemplate, RfpTemplateViewModel.FACTORY);
    }
}