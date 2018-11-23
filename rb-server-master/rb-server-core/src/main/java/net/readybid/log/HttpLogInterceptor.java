package net.readybid.log;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by DejanK on 12/14/2016.
 *
 */
@Component
public class HttpLogInterceptor extends HandlerInterceptorAdapter{

    public static final String $$_EXECUTION_TIMER = "$$executionStartTime";
//    private final MongoOperations mongo;

//    @Autowired
//    public HttpLogInterceptor(MongoOperations mongo) {
//        this.mongo = mongo;
//    }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler) throws Exception {
        httpServletRequest.setAttribute($$_EXECUTION_TIMER, new Date().getTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if(!httpServletRequest.getRequestURI().equals("/error")){
            final HttpRequestDocument requestDocument = new HttpRequestDocument(httpServletRequest, httpServletResponse,
                    (long) httpServletRequest.getAttribute($$_EXECUTION_TIMER), e);

            // todo: async insert
            // todo: check default write concern
            // todo: Needs to replace dots in maps (bids query throws exception because of dots in map keys)
            // mongo.insert(requestDocument);
        }
    }
}
