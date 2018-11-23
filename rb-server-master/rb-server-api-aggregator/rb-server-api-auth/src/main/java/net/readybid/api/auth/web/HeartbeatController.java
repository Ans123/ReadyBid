package net.readybid.api.auth.web;

import net.readybid.web.AbstractHeartbeatController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by stefan on 8/30/2016.
 *
 */
@RestController
public class HeartbeatController extends AbstractHeartbeatController {

    @RequestMapping(value = "/")
    public Map<String,String> getHeartbeat()
    {
        return super.getHeartbeat();
    }
}
