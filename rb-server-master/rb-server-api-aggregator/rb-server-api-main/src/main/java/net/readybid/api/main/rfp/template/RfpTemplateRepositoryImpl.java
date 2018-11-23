package net.readybid.api.main.rfp.template;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfp.template.RfpTemplateImpl;
import net.readybid.rfp.template.RfpTemplateListItemViewModel;
import net.readybid.utils.ListResult;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.include;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpTemplateRepositoryImpl implements RfpTemplateRepository {

    private static final String RFP_TEMPLATES_COLLECTION = "RfpTemplate";

    private final MongoCollection<RfpTemplateListItemViewModel> rfpTemplateSearch;
    private final MongoCollection<RfpTemplateImpl> rfpTemplate;

    @Autowired
    public RfpTemplateRepositoryImpl(MongoDatabase defaultMongoDatabase) {
        this.rfpTemplateSearch = defaultMongoDatabase.getCollection(RFP_TEMPLATES_COLLECTION, RfpTemplateListItemViewModel.class);
        this.rfpTemplate = defaultMongoDatabase.getCollection(RFP_TEMPLATES_COLLECTION, RfpTemplateImpl.class);
    }


    @Override
    public ListResult<RfpTemplateListItemViewModel> listRfpTemplates(String rfpType) {
        final Bson filter = eq("type", rfpType);

        final List<RfpTemplateListItemViewModel> data = rfpTemplateSearch.find(filter)
                .projection(include("name", "type", "description"))
                .into(new ArrayList<>());
        final long total = rfpTemplateSearch.count(filter);

        return new ListResult<>(data, total);
    }

    @Override
    public RfpTemplate getRfpTemplate(String id) {
        return rfpTemplate.find(byId(id)).first();
    }
}
