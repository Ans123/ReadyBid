package net.readybid.web;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Aspect
@Configuration
public class RbViewAspects {

    @Around("@annotation(RbResponseView)")
    public RbViewModel wrapInView(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final Object r = joinPoint.proceed();
        final RbViewModel vm = (RbViewModel) r;
        return new Response(vm, stopWatch);
    }

    static class Response implements RbViewModel {
        public long time;
        private Object data;
        private Long count;

        private Response(RbViewModel vm, StopWatch stopWatch){
            data = vm.getData();
            count = vm.getCount();

            stopWatch.stop();
            time = stopWatch.getLastTaskTimeMillis();
        }

        @Override
        public Object getData(){
            return data;
        }

        @Override
        public Long getCount() {
            return count;
        }
    }
}
