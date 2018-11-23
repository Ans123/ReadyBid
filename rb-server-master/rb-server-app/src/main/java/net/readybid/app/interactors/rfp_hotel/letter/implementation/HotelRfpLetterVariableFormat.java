package net.readybid.app.interactors.rfp_hotel.letter.implementation;

enum HotelRfpLetterVariableFormat {
    TEXT((key, value) -> String.format("<span class=\"inserted %s\">%s</span>", key, value)),
    LOGO((key, value) -> String.format("<img src=\"/images/logos/%s\" class=\"inserted %s insertedLogo\"/>", value, key));

    private final HotelRfpLetterVariableFormatFn fn;

    HotelRfpLetterVariableFormat(HotelRfpLetterVariableFormatFn fn) {
        this.fn = fn;
    }

    public String apply(String key, String value) {
        return fn.apply(key, value);
    }

    private interface HotelRfpLetterVariableFormatFn{
        String apply(String key, String modelValue);
    }
}