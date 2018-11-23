package net.readybid.api.main.rfp.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.template.RfpTemplate;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpFactory {
    Rfp createRfp(RfpTemplate template, AuthenticatedUser user, Entity entity);
}
