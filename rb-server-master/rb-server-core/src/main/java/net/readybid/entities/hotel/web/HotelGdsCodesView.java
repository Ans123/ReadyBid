package net.readybid.entities.hotel.web;

import net.readybid.entities.chain.HotelChain;

import java.util.Map;

public class HotelGdsCodesView {

    private static final String CODE_PATTERN = "%s %s";

    public String readyBidCode;
    public String sabreCode;
    public String amadeusCode;
    public String apolloCode;
    public String worldspanCode;
    public String internalCode;

    HotelGdsCodesView(Map<String, String> answers, HotelChain chain) {
        if(chain != null && answers != null){
            final String chainCode = chain.getCode();
            readyBidCode = formatCode(chainCode, answers.get("PROPCODE"));
            sabreCode = formatCode(answers, "SABRE_PROPCODE", "SABRE_CHAINCODE", chainCode);
            amadeusCode = formatCode(answers, "AMADEUS_PROPCODE", "AMADEUS_CHAINCODE", chainCode);
            apolloCode = formatCode(answers, "APOLLO_PROPCODE", "APOLLO_CHAINCODE", chainCode);
            worldspanCode = formatCode(answers, "WORLDSPAN_PROPCODE", "WORLDSPAN_CHAINCODE", chainCode);
            internalCode = formatCode(null, answers.get("INTERNALHOTELCODE"));
        }
    }

    private String formatCode(Map<String, String> answers, String propCodeField, String chainCodeField, String defaultChainCode) {
        return formatCode(answers.getOrDefault(chainCodeField, defaultChainCode), answers.get(propCodeField));
    }

    private String formatCode(String chainCode, String propertyCode){
        if(propertyCode == null || propertyCode.isEmpty()) return null;
        if(chainCode == null || chainCode.isEmpty()) return propertyCode;
        return String.format(CODE_PATTERN, chainCode, propertyCode);
    }
}
