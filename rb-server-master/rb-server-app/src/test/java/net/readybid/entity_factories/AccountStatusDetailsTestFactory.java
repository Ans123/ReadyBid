package net.readybid.entity_factories;

import net.readybid.auth.account.core.AccountStatus;
import net.readybid.auth.account.core.AccountStatusDetails;
import net.readybid.test_utils.RbRandom;

public class AccountStatusDetailsTestFactory {

    private AccountStatusDetailsTestFactory(){}

    public static AccountStatusDetails random() {

        final AccountStatusDetails statusDetails = new AccountStatusDetails();

        statusDetails.setAt(RbRandom.date());
        statusDetails.setBy(BasicUserDetailsImplFactory.random());
        statusDetails.setValue(RbRandom.randomEnum(AccountStatus.class));

        return statusDetails;
    }
}
