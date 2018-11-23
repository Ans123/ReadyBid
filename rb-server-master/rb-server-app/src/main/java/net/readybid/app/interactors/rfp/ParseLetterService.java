package net.readybid.app.interactors.rfp;

import java.util.Map;

public interface ParseLetterService {

    String parse(String finalAgreementTemplate, Map<String, String> model);
}
