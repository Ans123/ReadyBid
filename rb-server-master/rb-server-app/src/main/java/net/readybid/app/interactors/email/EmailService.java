package net.readybid.app.interactors.email;

import net.readybid.app.entities.email.EmailTemplate;

import java.util.List;

public interface EmailService {
    void send(EmailTemplate emailTemplate);

    void send(List<EmailTemplate> emailTemplates);
}
