package net.readybid.api.auth.config;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by DejanK on 3/16/2017.
 *
 */
@ControllerAdvice
public class AuthApiExceptionsHandler extends ExceptionsHandler{

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Map<String, Object> unauthorizedExceptionHandler(UnauthorizedException exception){
        return super.unauthorizedExceptionHandler(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map notFoundExceptionHandler(NotFoundException exception){
        return super.notFoundExceptionHandler(exception);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map badRequestExceptionHandler(BadRequestException exception) {
        return super.badRequestExceptionHandler(exception);
    }

    @ExceptionHandler(BruteForceException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public Map bruteForceExceptionHandler(BruteForceException e){
        return super.bruteForceExceptionHandler(e);
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map notAuthenticatedExceptionHandler(NotAuthenticatedException e){
        return super.notAuthenticatedExceptionHandler(e);
    }

    @ExceptionHandler(UnableToCompleteRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map unableToCompleteRequestExceptionHandler(UnableToCompleteRequestException exception){
        return super.unableToCompleteRequestExceptionHandler(exception);
    }
}
