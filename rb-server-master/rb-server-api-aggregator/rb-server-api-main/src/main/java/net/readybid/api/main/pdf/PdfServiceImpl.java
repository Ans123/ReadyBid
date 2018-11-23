package net.readybid.api.main.pdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.exceptions.MaxAttemptsReachedException;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.exceptions.UnrecoverableException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {

    private static final int MAX_TRIES = 2;

    private final ObjectMapper objectMapper;
    private final String jsReportUrl;
    private final String htmlToPdfTemplateId;
    private final String jsReportAuthorization;

    @Autowired
    public PdfServiceImpl(ObjectMapper objectMapper, Environment env) {
        this.objectMapper = objectMapper;
        jsReportUrl = env.getRequiredProperty("js-report.url");
        htmlToPdfTemplateId = env.getRequiredProperty("js-report.template.html-to-pdf");
        final String credentials = env.getRequiredProperty("js-report.credentials");
        byte[] encodedBytes = Base64.encodeBase64(credentials.getBytes());
        jsReportAuthorization = "Basic " + new String(encodedBytes);
    }

    @Override
    public File create(String html) {
        Map<String, String> templateSettings = new HashMap<>();
        Map<String, String> data = new HashMap<>();

        templateSettings.put("shortid", htmlToPdfTemplateId);
        data.put("html", cleanHtml(html));

        final String requestJson = createRequest(templateSettings, data);

        return sendRequestWithRetries(requestJson, MAX_TRIES);
    }

    private String cleanHtml(String html) {
        return html
                .replaceAll("[\\u2013\\u2014\\u2015]", "-")
                .replaceAll("[\\u2017]", "_")
                .replaceAll("[\\u2018\\u2019\\u201b\\u2032]", "'")
                .replaceAll("[\\u201a]", ",")
                .replaceAll("[\\u201c\\u201d\\u201e\\u2033]", "\"")
                .replaceAll("[\\u2026]", "...")
                .replaceAll("[\\u00A0]","");
    }

    private File sendRequestWithRetries(String requestJson, int maxTries) {
        if(maxTries > 0){
            try{
                maxTries--;
                return sendRequest(requestJson);
            } catch (UnableToCompleteRequestException e){
                return sendRequestWithRetries(requestJson, maxTries);
            }
        } else {
            throw new MaxAttemptsReachedException();
        }
    }

    private File sendRequest(String requestJson) {
        final RestTemplate rt = newRestTemplate();
        final HttpEntity<String> entity = newHttpEntity(requestJson);
        try {
            final ResponseEntity<byte[]> response = rt.exchange(jsReportUrl, HttpMethod.POST, entity, byte[].class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new UnableToCompleteRequestException(String.format("JsReport server response status code: %s", response.getStatusCode()));
            }
            return writeToTemporaryFile(response);
        } catch (Exception e) {
            throw new UnrecoverableException(e);
        }
    }

    private File writeToTemporaryFile(ResponseEntity<byte[]> response) throws IOException {
        File tmpFile = File.createTempFile("temporary", "pdf");
        Files.write(tmpFile.toPath(), response.getBody());
        return tmpFile;
    }

    private HttpEntity<String> newHttpEntity(String requestJson) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Authorization", jsReportAuthorization);
        return new HttpEntity<>(requestJson, headers);
    }

    private RestTemplate newRestTemplate() {
        final RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return rt;
    }

    private String createRequest(Map<String, String> templateSettings, Map<String, String> data) {
        final Map<String, Object> request = new HashMap<>();
        request.put("template", templateSettings);
        request.put("data", data);

        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new UnrecoverableException(e);
        }
    }
}
