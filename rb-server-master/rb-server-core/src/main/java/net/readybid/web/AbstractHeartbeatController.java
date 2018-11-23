package net.readybid.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/17/2017.
 *
 */
public abstract class AbstractHeartbeatController {

    protected Map<String,String> getHeartbeat()
    {
        final Map<String,String> map = new HashMap<>();
        map.put("heartbeat","true");
        return map;
    }
}
