package net.readybid.entities.hotel.db;

import net.readybid.elasticsearch.AbstractEsCodec;
import net.readybid.entities.chain.HotelChainType;
import net.readybid.entities.chain.HotelChainViewModel;
import net.readybid.entities.hotel.web.HotelSearchResultView;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class HotelSearchResultViewEsCodec extends AbstractEsCodec<HotelSearchResultView> {

    @Override
    public HotelSearchResultView decode(Map source) {
        final HotelSearchResultView v = new HotelSearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);
        v.chain = getChain(source.get("chain"));

        return v;
    }

    private HotelChainViewModel getChain(Object o) {
        if(o != null && o instanceof Map){
            final Map cMap = (Map) o;
            final HotelChainViewModel c = new HotelChainViewModel();

            c.id = getIfNotNull(cMap.get("id"));
            c.name = getIfNotNull(cMap.get("name"));
            c.code = getIfNotNull(cMap.get("code"));

            final String subtype = getIfNotNull(cMap.get("subtype"));
            c.subtype = subtype == null ? null : HotelChainType.valueOf(subtype);
            c.masterChain = getChain(cMap.get("master"));

            return c;
        } else {
            return null;
        }
    }
}
