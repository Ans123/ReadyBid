package net.readybid.api.contact_us;

import net.readybid.app.use_cases.contact_us.ContactMessagesHandler;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import net.readybid.web.RbViewModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by DejanK on 12/12/2016.
 *
 */
@RestController
public class ContactUsController {

    private final ContactMessagesHandler contactMessagesHandler;

    @Autowired
    public ContactUsController(
            ContactMessagesHandler contactMessagesHandler
    ) {
        this.contactMessagesHandler = contactMessagesHandler;
    }

    @RbResponseView
    @PostMapping(value = "/contact-us")
    @ResponseStatus(HttpStatus.CREATED)
    public RbViewModel handleContactUsMessage(@RequestBody @Valid ContactUsRequest contactUsRequest) {
        contactMessagesHandler.handleContactUsMessage(contactUsRequest.toContactUsMessage());
        return RbViewModels.ACTION_SUCCESS;
    }

    @RbResponseView
    @PostMapping(value = "/custom-rfp-message")
    @ResponseStatus(HttpStatus.CREATED)
    public RbViewModel handleCustomRfpMessageRequest(@RequestBody @Valid CustomRfpMessageRequest customRfpMessageRequest) {
        contactMessagesHandler.handleCustomRfpMessage(customRfpMessageRequest.toCustomRfpMessage());
        return RbViewModels.ACTION_SUCCESS;
    }
}