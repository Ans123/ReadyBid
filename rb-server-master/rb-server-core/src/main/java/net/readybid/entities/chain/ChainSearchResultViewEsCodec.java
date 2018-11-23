package net.readybid.entities.chain;

import net.readybid.elasticsearch.AbstractEsCodec;

import java.util.Map;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class ChainSearchResultViewEsCodec extends AbstractEsCodec<HotelChainSearchResultView> {

    @Override
    public HotelChainSearchResultView decode(Map source) {
        final HotelChainSearchResultView v = new HotelChainSearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);

        return v;
    }
}


