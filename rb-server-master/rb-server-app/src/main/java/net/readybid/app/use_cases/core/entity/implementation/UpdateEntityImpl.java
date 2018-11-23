package net.readybid.app.use_cases.core.entity.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.authentication.account.gate.AccountStorageManager;
import net.readybid.app.interactors.authentication.invitation.InvitationModificationService;
import net.readybid.app.interactors.core.entity.EntityModificationService;
import net.readybid.app.interactors.rfp.RfpModificationService;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import net.readybid.app.use_cases.core.entity.UpdateEntity;
import net.readybid.auth.account.core.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
class UpdateEntityImpl implements UpdateEntity {

    private final EntityModificationService entityModificationService;
    private final AccountStorageManager accountStorageManager;
    private final InvitationModificationService invitationModificationService;
    private final RfpModificationService rfpModificationService;

    @Autowired

    public UpdateEntityImpl(
            @Qualifier("entityModificationService") EntityModificationService entityModificationService,
            AccountStorageManager accountStorageManager, InvitationModificationService invitationModificationService,
            RfpModificationService rfpModificationService
    ) {
        this.entityModificationService = entityModificationService;
        this.accountStorageManager = accountStorageManager;
        this.invitationModificationService = invitationModificationService;
        this.rfpModificationService = rfpModificationService;
    }

    @Override
    public void setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request) {
        final Entity entity = entityModificationService.setBasicInformation(entityType, entityId, request);
        final Account account = accountStorageManager.updateBasicInformationFromEntity(entity);
        invitationModificationService.updateInvitationsWithAccount(account);
        rfpModificationService.updateEntityBasicInformation(entity, account);
    }

    @Override
    public String setLogo(EntityType entityType, String entityId, MultipartFile multipartFile) {
        final Entity entity = entityModificationService.updateLogo(entityType, entityId, multipartFile);
        final Account account = accountStorageManager.updateLogoFromEntity(entity);
        rfpModificationService.updateAccountLogo(account);
        return account.getLogo();
    }

    @Override
    public String setImage(EntityType entityType, String entityId, MultipartFile multipartFile) {
        final Entity entity = entityModificationService.updateImage(entityType, entityId, multipartFile);
        return entity.getImageUrl();
    }
}
