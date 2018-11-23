package net.readybid.entities.company.web;

import net.readybid.entities.company.core.Company;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.core.EntityView;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class CompanyView extends EntityView implements ViewModel<Company> {

    public static final ViewModelFactory<Company, CompanyView> FACTORY = CompanyView::new;

    public CompanyView(Entity entity) {
        super(entity);
    }
}
