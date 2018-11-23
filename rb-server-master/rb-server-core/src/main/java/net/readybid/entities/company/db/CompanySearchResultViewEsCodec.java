package net.readybid.entities.company.db;

import net.readybid.elasticsearch.AbstractEsCodec;
import net.readybid.entities.company.web.CompanySearchResultView;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class CompanySearchResultViewEsCodec extends AbstractEsCodec<CompanySearchResultView>{
    @Override
    public CompanySearchResultView decode(Map source) {
        final CompanySearchResultView v = new CompanySearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);

        return v;
    }
}
