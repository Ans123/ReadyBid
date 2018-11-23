package net.readybid.entities.agency.db;

import net.readybid.elasticsearch.AbstractEsCodec;
import net.readybid.entities.agency.web.AgencySearchResultView;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class AgencySearchResultViewEsCodec extends AbstractEsCodec<AgencySearchResultView> {

    @Override
    public AgencySearchResultView decode(Map source) {
        final AgencySearchResultView v = new AgencySearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);

        return v;
    }
}
