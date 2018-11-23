package net.readybid.mongodb;

import com.mongodb.*;
import net.readybid.exceptions.UnrecoverableException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

/**
 * Created by DejanK on 12/13/2016.
 *
 */
@Aspect
@Configuration
public class MongoAspects {

//    @Around("@annotation(RetryAsIdempotent)")
//    public Object retryAsIdempotent(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object returnObject = null;
//
//            try {
////                System.out.println("YourAspect's aroundAdvice's body is now executed Before yourMethodAround is called.");
//                returnObject = joinPoint.proceed();
//            } catch (TransientDataAccessException ex){
//                try {
//                    returnObject = joinPoint.proceed();
//                } catch (DuplicateKeyException ignore){}
//                // todo: NOTICE: RETRY WILL NOT THROW DuplicateKeyException AND INSERT RESULT MAY BE NULL EVEN IF INSERT IS SUCCESSFUL
//                // todo: Need to add support for Mongo JAVA driver and make it even more robust.
//            }
//
//        return returnObject;
//    }

    @Around("@annotation(MongoRetry)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        return runWithRetryOnNetworkFailure(joinPoint, true);
    }

    private Object runWithRetryOnNetworkFailure(ProceedingJoinPoint joinPoint, boolean retry) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (MongoSocketException | MongoNotPrimaryException | MongoNodeIsRecoveringException e){
            if(retry){
                return runWithRetryOnNetworkFailure(joinPoint, false);
            } else {
                throw new UnrecoverableException(e);
            }
        } catch (MongoWriteException | MongoCommandException e){
            // ignores duplicate key error on _id; presumes that Object has been saved in first attempt
            if(ErrorCategory.DUPLICATE_KEY.equals(ErrorCategory.fromErrorCode(e.getCode()))) {
                final RbDuplicateKeyException dke = new RbDuplicateKeyException(e);

                if(dke.isOnIdKey()){
                    return null;
                } else {
                    throw dke;
                }
            }
            throw e;
        }
    }

    @Around("@annotation(MongoMeasureTime)")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        System.out.println("time = " + stopWatch.getLastTaskTimeMillis());

        return result;
    }
}
