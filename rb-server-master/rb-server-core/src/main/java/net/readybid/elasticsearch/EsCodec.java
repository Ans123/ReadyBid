package net.readybid.elasticsearch;

import java.util.Map;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface EsCodec<T> {

    T decode(Map source);
}
