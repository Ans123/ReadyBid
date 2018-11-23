package net.readybid.rfp.template;


import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 9/6/2016.
 *
 */
public class RfpTemplateListItemViewModel implements ViewModel<RfpTemplateListItem> {

    public static final ViewModelFactory<RfpTemplateListItem, RfpTemplateListItemViewModel> FACTORY = RfpTemplateListItemViewModel::new;

    public String id;
    public String name;
    public String type;
    public String description;

    public RfpTemplateListItemViewModel() {}

    @Deprecated
    public RfpTemplateListItemViewModel(RfpTemplateListItem rfpTemplateListItem) {
        id = rfpTemplateListItem.id;
        name = rfpTemplateListItem.name;
        type = rfpTemplateListItem.type;
        description = rfpTemplateListItem.description;
    }
}
