package net.readybid.api.contact_us;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.app.use_cases.contact_us.*;
import net.readybid.web.RbViewModel;
import net.readybid.web.RbViewModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("ContactUsController ")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ContactUsController.class)
@ActiveProfiles("default,test")
class ContactUsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ContactMessagesHandler contactMessagesHandler;

    private JacksonTester<ContactUsRequest> jsonContactUsRequest;
    private JacksonTester<RbViewModel> jsonRbViewModel;
    private JacksonTester<CustomRfpMessageRequest> jsonCustomRfpMessageRequest;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, mapper);
    }

    @Test
    void handlesNewContactUsMessageTest() throws Exception {
        final ZonedDateTime creationMark = ZonedDateTime.now();
        final ContactUsRequest request = ContactUsRequestFactory.random();
        final ArgumentCaptor<ContactUsMessage> messageCaptor = ArgumentCaptor.forClass(ContactUsMessage.class);

        final MockHttpServletResponse response = mockMvc.perform(
                post("/contact-us")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonContactUsRequest.write(request).getJson())
        ).andReturn().getResponse();

        verify(contactMessagesHandler, times(1)).handleContactUsMessage(messageCaptor.capture());
        ContactUsMessageAssert.that(messageCaptor.getValue())
                .hasName(request.name)
                .hasEmailAddress(request.emailAddress)
                .hasMessage(request.message)
                .hasCreatedAtAfter(creationMark);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertEquals(jsonRbViewModel.write(RbViewModels.ACTION_SUCCESS).getJson(), response.getContentAsString());
    }

    @Test
    void handlesNewCustomRfpMessageTest() throws Exception {
        final ZonedDateTime creationMark = ZonedDateTime.now();
        final CustomRfpMessageRequest request = CustomRfpMessageRequestFactory.random();
        final ArgumentCaptor<CustomRfpMessage> messageCaptor = ArgumentCaptor.forClass(CustomRfpMessage.class);

        final MockHttpServletResponse response = mockMvc.perform(
                post("/custom-rfp-message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonCustomRfpMessageRequest.write(request).getJson())
        ).andReturn().getResponse();

        verify(contactMessagesHandler, times(1)).handleCustomRfpMessage(messageCaptor.capture());
        CustomRfpMessageAssert.that(messageCaptor.getValue())
                .hasFirstName(request.firstName)
                .hasLastName(request.lastName)
                .hasEmailAddress(request.emailAddress)
                .hasPhone(request.phone)
                .hasCompany(request.company)
                .hasMessage(request.message)
                .hasCreatedAtAfter(creationMark);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertEquals(jsonRbViewModel.write(RbViewModels.ACTION_SUCCESS).getJson(), response.getContentAsString());
    }
}