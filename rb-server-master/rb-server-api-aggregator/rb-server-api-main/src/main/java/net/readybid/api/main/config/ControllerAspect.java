package net.readybid.api.main.config;

import net.readybid.auth.authorization.AuthorizationService;
import net.readybid.auth.user.AuthenticatedUser;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by DejanK on 3/30/2017.
 *
 */
@Aspect
@Component
public class ControllerAspect {
    private final AuthorizationService authorizationService;

    @Autowired
    public ControllerAspect(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {}

    @Pointcut("execution(* *(..))")
    public void methodPointcut() {}

    @After("controllerBean() && methodPointcut() ")
    public void afterMethodInControllerClass() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication instanceof AuthenticatedUser){
            authorizationService.refreshAuthentication(response, (AuthenticatedUser) authentication);
        }
    }
}
