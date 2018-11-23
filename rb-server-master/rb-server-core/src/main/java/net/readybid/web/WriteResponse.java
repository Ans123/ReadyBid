package net.readybid.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class WriteResponse {

    public long time;
    public String status;
    public Object data;

    @JsonIgnore
    public final HttpServletResponse servletResponse;

    @JsonIgnore
    private final String urlFormat;

    public WriteResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this(servletRequest.getRequestURL().append("/%s").toString(), servletResponse);
    }

    public WriteResponse(String urlFormat, HttpServletResponse servletResponse) {
        this.time = new Date().getTime();
        this.servletResponse = servletResponse;
        this.urlFormat = urlFormat;
    }

    public WriteResponse finalizeResponse(Object data, String createdId) {
        status = "OK";
        this.data = data;
        servletResponse.addHeader("location", String.format(urlFormat, createdId));
        this.time = new Date().getTime() - this.time;
        return this;
    }

    public WriteResponse finalizeResponse(Object data, ObjectId createdId) {
        return finalizeResponse(data, String.valueOf(createdId));
    }

    public WriteResponse finalizeResponse(ObjectId createdId) {
        return finalizeResponse(null, String.valueOf(createdId));
    }

    public WriteResponse finalizeResponse(String id) {
        return finalizeResponse(null, id);
    }
}