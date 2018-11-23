package net.readybid.api.main.rfp.letter;

public interface AnswersHelper {

    String createPeriod(Object start, Object end);

    boolean readYesNo(Object yesOrNo);

    String readAmount(Object amount, String defaultValue);

    boolean readAsFalse(Object obj, String falseValue);

    String readMixed(Object fixedOrPercentage, Object amount, String defaultValue);
}
