package net.readybid.elasticsearch;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public abstract class AbstractEsCodec<T> implements EsCodec<T> {

    @Override
    public abstract T decode(Map source);

    protected String getIfNotNull(Object o) {
        return o == null ? null : o.toString();
    }

    protected String getFullAddress(Map source) {
        final Object o = source.get("location");
        if(o != null && o instanceof Map){
            final Map cMap = (Map) o;
            return getIfNotNull(cMap.get("fullAddress"));
        } else {
            return null;
        }
    }
}
