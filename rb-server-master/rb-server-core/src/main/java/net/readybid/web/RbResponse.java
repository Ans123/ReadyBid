package net.readybid.web;

import org.springframework.util.StopWatch;

public class RbResponse {

    public long time;
    public Object data;
    public String status;
    public Long count;

    public RbResponse(RbViewModelFactory factory) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final RbViewModel vm = factory.create();
        data = vm.getData();
        count = vm.getCount();
        status = "OK";

        stopWatch.stop();
        time = stopWatch.getLastTaskTimeMillis();
    }
}
