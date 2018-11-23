package net.readybid.api.main.rfp.template;

import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfp.template.RfpTemplateListItemViewModel;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpTemplateRepository {
    ListResult<RfpTemplateListItemViewModel> listRfpTemplates(String rfpType);

    RfpTemplate getRfpTemplate(String id);
}
