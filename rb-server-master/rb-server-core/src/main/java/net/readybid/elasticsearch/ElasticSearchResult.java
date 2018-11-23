package net.readybid.elasticsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public class ElasticSearchResult<T> {
    public long total;
    public boolean timedOut;
    public long took;
    public List<T> items;

    public Map<String, Object> toMap(){
        final Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("timedOut", timedOut);
        result.put("took", took);
        result.put("items", items);

        return result;
    }
}
