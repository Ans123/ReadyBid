package net.readybid.app.entities.email;

import net.readybid.test_utils.RbAbstractAssert;
import net.readybid.test_utils.RbMapAssert;

import java.util.function.Consumer;

public class EmailTemplateAssert extends RbAbstractAssert<EmailTemplateAssert, EmailTemplate> {

    public static EmailTemplateAssert that(EmailTemplate actual) {
        return new EmailTemplateAssert(actual);
    }

    private EmailTemplateAssert(EmailTemplate actual) {
        super(actual, EmailTemplateAssert.class);
    }

    public EmailTemplateAssert hasReceiverEmailAddress(Object expected) {
        assertFieldEquals("receiver email address", expected, actual.getReceiver().getAddress());
        return this;
    }

    public EmailTemplateAssert hasReceiverName(Object expected) {
        assertFieldEquals("receiver name", expected, actual.getReceiver().getPersonal());
        return this;
    }

    public EmailTemplateAssert hasSubject(Object expected) {
        assertFieldEquals("subject", expected, actual.getSubject());
        return this;
    }

    public EmailTemplateAssert hasHtmlTemplate(Object expected) {
        assertFieldEquals("HTML template", expected, actual.getHtmlTemplateName());
        return this;
    }

    public EmailTemplateAssert hasEmptyCC() {
        assertFieldNull("CC", actual.getCC());
        return this;
    }

    public EmailTemplateAssert modelAssert(Consumer<RbMapAssert<String, String>> consumer) {
        consumer.accept(RbMapAssert.that(actual.getModel()));
        return this;
    }
}