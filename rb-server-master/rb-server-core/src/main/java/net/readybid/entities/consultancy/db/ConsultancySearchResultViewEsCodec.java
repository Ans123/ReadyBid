package net.readybid.entities.consultancy.db;

import net.readybid.elasticsearch.AbstractEsCodec;
import net.readybid.entities.consultancy.web.ConsultancySearchResultView;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class ConsultancySearchResultViewEsCodec extends AbstractEsCodec<ConsultancySearchResultView> {

    @Override
    public ConsultancySearchResultView decode(Map source) {
        final ConsultancySearchResultView v = new ConsultancySearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);

        return v;

    }
}
