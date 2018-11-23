package net.readybid.api.main.config;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.exceptions.*;
import net.readybid.mongodb.RbDuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by DejanK on 3/16/2017.
 *
 */
@ControllerAdvice
public class MainApiControllersAdvice extends ExceptionsHandler{

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }


    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Map<String, Object> notAllowedExceptionHandler(NotAllowedException exception){
        return super.notAllowedExceptionHandler(exception);
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

    @ExceptionHandler(UnrecoverableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map unrecoverableExceptionHandler(UnrecoverableException exception){
        return super.unrecoverableExceptionHandler(exception);
    }

    @ExceptionHandler(MaxAttemptsReachedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map maxAttemptsReachedExceptionHandler(MaxAttemptsReachedException exception){
        return super.maxAttemptsReachedExceptionHandler(exception);
    }

    @ExceptionHandler(RbDuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map duplicateKeyExceptionHandler(RbDuplicateKeyException exception){
        return super.duplicateKeyExceptionHandler(exception);
    }
}
