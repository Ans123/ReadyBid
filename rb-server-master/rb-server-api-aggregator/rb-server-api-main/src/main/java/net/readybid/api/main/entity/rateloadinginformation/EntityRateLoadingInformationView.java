package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class EntityRateLoadingInformationView implements ViewModel<EntityRateLoadingInformation> {

    public static final ViewModelFactory<EntityRateLoadingInformation, EntityRateLoadingInformationView> FACTORY = EntityRateLoadingInformationView::new;

    public ObjectId entityId;
    public String entityName;
    public List<RateLoadingInformationView> information;

    public EntityRateLoadingInformationView(EntityRateLoadingInformation entityRateLoadingInformation) {
        entityId = entityRateLoadingInformation.getEntityId();
        entityName = entityRateLoadingInformation.getEntityName();
        information = RateLoadingInformationView.FACTORY.createList(entityRateLoadingInformation.getInformation());
    }
}
