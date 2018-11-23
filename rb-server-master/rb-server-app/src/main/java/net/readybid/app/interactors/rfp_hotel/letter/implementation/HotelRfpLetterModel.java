package net.readybid.app.interactors.rfp_hotel.letter.implementation;

import java.util.Map;

interface HotelRfpLetterModel {
    Map<String, String> getModel();
    boolean usePlaceholders();
}