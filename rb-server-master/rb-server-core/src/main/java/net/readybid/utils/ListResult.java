package net.readybid.utils;

import java.util.List;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
public class ListResult<T> {
    public List<T> data;
    public long total;

    public ListResult(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    public ListResult(List<T> data) {
        this(data, data.size());
    }
}
