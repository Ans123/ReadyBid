package net.readybid.app.interactors.rfp.implementation;

import net.readybid.app.interactors.rfp.ParseLetterService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseLetterServiceImpl implements ParseLetterService {

    @Override
    public String parse(String letterTemplate, Map<String, String> model) {
        Pattern pattern = Pattern.compile("\\{\\{.+?\\}\\}");
        Matcher matcher = pattern.matcher(letterTemplate);

        int pos = 0;
        StringBuilder letter = new StringBuilder();
        while (matcher.find(pos))
        {
            letter.append(letterTemplate, pos, matcher.start());
            String found = matcher.group();
            String key = found.substring(2, found.length() - 2);
            letter.append(model.get(key));
            pos = matcher.end();
        }
        letter.append(letterTemplate, pos, letterTemplate.length());
        return letter.toString();
    }
}
