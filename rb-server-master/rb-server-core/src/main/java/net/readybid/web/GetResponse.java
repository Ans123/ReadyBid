package net.readybid.web;

import net.readybid.app.core.transaction.exceptions.NotFoundException;

import java.util.Date;

/**
 * Created by DejanK on 9/6/2016.
 *
 */
public class GetResponse<T, S extends ViewModel<T>> {

    public long time;
    public S data;
    public String status;

    public GetResponse() {
        this.time = new Date().getTime();
    }

    public GetResponse<T, S> finalizeResult(S s) {
        if(s == null) throw new NotFoundException();
        data = s;
        status = "OK";
        time = new Date().getTime() - time;
        return this;
    }

    public GetResponse<T, S> finalizeResult(T t, ViewModelFactory<T, S> factory) {
        return finalizeResult(factory.createView(t));
    }
}