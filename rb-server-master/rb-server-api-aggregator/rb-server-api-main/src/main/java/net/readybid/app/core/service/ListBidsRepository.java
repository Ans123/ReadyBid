package net.readybid.app.core.service;

import java.util.Map;

public interface ListBidsRepository {
    Map<String,Long> getBidsCountPerDestinationForRfp(String rfpId);
}
