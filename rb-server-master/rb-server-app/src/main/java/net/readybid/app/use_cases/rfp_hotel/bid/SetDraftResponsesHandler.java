package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbViewModel;

import java.util.List;
import java.util.Map;

public interface SetDraftResponsesHandler {

    RbViewModel setResponses(List<String> bidsIds, List<Map<String,String>> validatedResponses, AuthenticatedUser currentUser);

    RbViewModel setResponse(String bidId, Map<String, String> response, AuthenticatedUser currentUser);
}
