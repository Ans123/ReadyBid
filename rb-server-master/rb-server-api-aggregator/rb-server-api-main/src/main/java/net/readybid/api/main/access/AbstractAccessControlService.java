package net.readybid.api.main.access;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.app.interactors.authentication.user.gate.CurrentUserProvider;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.exceptions.NotAllowedException;

import java.util.List;

/**
 * Created by DejanK on 4/7/2017.
 *
 */
public abstract class AbstractAccessControlService {

    private final CurrentUserProvider currentUserProvider;

    protected AbstractAccessControlService(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    protected void check(String id, PermissionsCheck permissionsCheck){
        final InvolvedAccounts involvedAccounts = getInvolvedCompanies(id);
        final AuthenticatedUser user = getCurrentUser();
        if(user == null || !(permissionsCheck.check(user, involvedAccounts)))
            throw new NotAllowedException();
    }

    protected void check(List<String> ids, PermissionsCheck permissionsCheck){
        final AuthenticatedUser user = getCurrentUser();
        if(user == null) throw new NotAllowedException();

        final List<InvolvedAccounts> companies = getCompanies(ids);
        if(companies == null || companies.isEmpty() || companies.size() != ids.size()) throw new NotAllowedException();

        for(InvolvedAccounts a : companies){
            if(a == null || !(permissionsCheck.check(user, a))) throw new NotAllowedException();
        }
    }

    protected void check(PermissionsCheck permissionsCheck) {
        final AuthenticatedUser user = getCurrentUser();
        if(user == null || !(permissionsCheck.check(user, InvolvedAccounts.getNullCompanies())))
            throw new NotAllowedException();
    }

    protected InvolvedAccounts getInvolvedCompanies(String id){
        final InvolvedAccounts companies = getCompanies(id);
        if(companies == null || companies.isEmpty()) throw new NotFoundException();
        return companies;
    }

    protected AuthenticatedUser getCurrentUser(){
        return currentUserProvider.get();
    }

    protected abstract InvolvedAccounts getCompanies(String id);

    protected abstract List<InvolvedAccounts> getCompanies(List<String> ids);
}
