package net.readybid.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class ActionResponse {

    public long time;
    public String status;
    public Map<String, Object> data;

    public ActionResponse() {
        this.time = new Date().getTime();
        data = new HashMap<>();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public ActionResponse finalizeAction() {
        this.time = new Date().getTime() - this.time;
        this.status = "OK";
        return this;
    }

    public ActionResponse finalizeAction(String status) {
        this.time = new Date().getTime() - this.time;
        this.status = status;
        return this;
    }

    public ActionResponse finalizeAction(Map<String, Object> data) {
        this.data = data;
        return finalizeAction();
    }

    public ActionResponse finalizeAction(String key, Object value) {
        data.put(key, value);
        return finalizeAction();
    }
}