package net.readybid.web;

import net.readybid.utils.ListResult;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by DejanK on 9/6/2016.
 *
 */
public class ListResponse<S extends ViewModel> {

    public long time;
    public long count;
    public List<S> data;
    public String status;

    public ListResponse() {
        this.time = new Date().getTime();
    }

    public ListResponse<S> finalizeResult(){
        time = new Date().getTime() - time;
        status = "OK";
        return this;
    }

    public ListResponse<S> finalizeResult(ListResult<S> listResult) {
        if(listResult != null){
            data = listResult.data;
            count = listResult.total;
        }
        return finalizeResult();
    }

    public ListResponse<S> finalizeResult(S result) {
        data = Collections.singletonList(result);
        count = 1;
        return finalizeResult();
    }

    public ListResponse<S> finalizeResult(List<S> result) {
        data = result;
        count = result.size();
        return finalizeResult();
    }
}