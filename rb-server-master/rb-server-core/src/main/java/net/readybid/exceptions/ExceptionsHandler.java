package net.readybid.exceptions;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.mongodb.RbDuplicateKeyException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2015.
 * todo: add logging
 */
public class ExceptionsHandler {

    private static final String NOT_ALLOWED = "Not Allowed";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String NOT_FOUND = "Not Found";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String CONFLICT = "Unable to complete request";
    private static final String TOO_MANY_REQUEST = "Too many requests";
    private static final String UNRECOVERABLE = "Unrecoverable error encountered";

    public Map<String, Object> notAllowedExceptionHandler(NotAllowedException exception){
        return createResponse(NOT_ALLOWED);
    }

    public Map<String, Object> unauthorizedExceptionHandler(UnauthorizedException exception){
        final String message = exception.getMessage();
        return createResponse(message == null || message.isEmpty() ? UNAUTHORIZED : message);
    }

    public Map notFoundExceptionHandler(NotFoundException exception){
        final Map<String, Object> response = createResponse(NOT_FOUND);
        final String exceptionMessage = exception.getMessage();

        if(exceptionMessage != null && !exceptionMessage.isEmpty())
            response.put("message", exceptionMessage);

        return response;
    }

    public Map badRequestExceptionHandler(BadRequestException exception) {
        final String message = exception.getMessage();

        Map<String, Object> response = createResponse(message == null || message.isEmpty() ? BAD_REQUEST : message);
        if(exception.id != null && !exception.id.isEmpty()) response.put("id", exception.id);
        if(exception.value != null && !exception.value.isEmpty()) response.put("value", exception.value);

        return response;
    }

    public Map bruteForceExceptionHandler(BruteForceException e){
        Map<String, Object> response = createResponse(TOO_MANY_REQUEST);
        return response;
    }

    public Map notAuthenticatedExceptionHandler(NotAuthenticatedException e){
        Map<String, Object> response = createResponse(NOT_FOUND);
        return response;
    }

    public Map unrecoverableExceptionHandler(UnrecoverableException e){
        Map<String, Object> response = createResponse(UNRECOVERABLE);
        return response;
    }

    public Map maxAttemptsReachedExceptionHandler(MaxAttemptsReachedException e){
        Map<String, Object> response = createResponse(UNRECOVERABLE);
        return response;
    }

//    @ExceptionHandler(BruteForceException.class)
//    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
//    @ResponseBody
//    public Map<String, String> BruteForceExceptionHandler(BruteForceException exception) {
//        Map<String, String> response = new HashMap<>();
//        response.put("status", "429");
//        response.put("message", "Too many requests");
//        return response;
//    }
//

    public Map unableToCompleteRequestExceptionHandler(UnableToCompleteRequestException exception){
        Map<String, Object> response = createResponse(CONFLICT);
        response.put("message", exception.message);
        return response;
    }

    private Map<String, Object> createResponse(String value) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", value);
        return response;
    }

    public Map duplicateKeyExceptionHandler(RbDuplicateKeyException exception) {
        return createResponse(exception.errorCode);
    }
}
