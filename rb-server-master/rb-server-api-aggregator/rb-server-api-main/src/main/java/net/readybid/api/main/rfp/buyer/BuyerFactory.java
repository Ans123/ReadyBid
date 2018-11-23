package net.readybid.api.main.rfp.buyer;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.rfp.buyer.Buyer;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface BuyerFactory {

    Buyer createBuyer(AuthenticatedUser user, Entity entity);
}
