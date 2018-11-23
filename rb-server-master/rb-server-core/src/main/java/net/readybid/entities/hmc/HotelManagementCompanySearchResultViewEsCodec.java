package net.readybid.entities.hmc;

import net.readybid.elasticsearch.AbstractEsCodec;

import java.util.Map;

/**
 * Created by DejanK on 2/15/2017.
 */
public class HotelManagementCompanySearchResultViewEsCodec extends AbstractEsCodec<HotelManagementCompanyFacadeSearchResultView> {

    @Override
    public HotelManagementCompanyFacadeSearchResultView decode(Map source) {
        final HotelManagementCompanyFacadeSearchResultView v = new HotelManagementCompanyFacadeSearchResultView();

        v.id = getIfNotNull(source.get("id"));
        v.name = getIfNotNull(source.get("name"));
        v.fullAddress = getFullAddress(source);

        return v;
    }
}
