package net.readybid.api.main.account;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.account.web.AccountViewModel;
import net.readybid.web.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DejanK on 9/4/2017.
 *
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public GetResponse<Account, AccountViewModel> getAccount(
            @PathVariable(value = "accountId") String accountId
    ) {
        final GetResponse<Account, AccountViewModel> response = new GetResponse<>();
        final Account account = accountService.getAccount(accountId);
        return response.finalizeResult(account, AccountViewModel.FACTORY);
    }
}