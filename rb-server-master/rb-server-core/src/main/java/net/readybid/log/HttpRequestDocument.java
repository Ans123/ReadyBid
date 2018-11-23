package net.readybid.log;

import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 12/14/2016.
 *
 */
public class HttpRequestDocument {

    public ObjectId id;
    public String host;
    public String request;
    public String method;
    public Date time;
    public Map requestData;

    public ObjectId userId;
    public String ipAddress;
    public String referrer;
    public String userAgent;

    public int status;
    public long responseTime;
    public String exceptionStack;

    public HttpRequestDocument(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long startTimeInMillis, Exception e) {
        id = new ObjectId();
        host = httpServletRequest.getLocalName();
        method = httpServletRequest.getMethod();
        request = httpServletRequest.getRequestURI();
        time = new Date();

//        final AuthUser authUser = (AuthUser) httpServletRequest.getUserPrincipal();
//        userId = authUser == null ? null : authUser.getId();
//
//        ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
//        ipAddress = ipAddress == null ? httpServletRequest.getRemoteAddr() : ipAddress;
//
//        referrer = httpServletRequest.getHeader(HttpHeaders.REFERER);
//        userAgent = httpServletRequest.getHeader(HttpHeaders.USER_AGENT);
//
//        status = httpServletResponse.getStatus();
//        responseTime = System.currentTimeMillis() - startTimeInMillis;
//
//        // todo: needs to be made more robust
//        // todo: needs to handle url params
//        if(e != null){
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                // todo: never save sign up and sign in data
//                requestData = mapper.readValue(httpServletRequest.getInputStream(), HashMap.class);
//                exceptionStack = ExceptionUtils.getStackTrace(e);
//                status = 500;
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
    }
}
