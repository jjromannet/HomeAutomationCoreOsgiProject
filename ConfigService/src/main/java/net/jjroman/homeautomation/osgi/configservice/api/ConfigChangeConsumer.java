package net.jjroman.homeautomation.osgi.configservice.api;

import java.util.Map;

/**
 * Created by Jan on 24/04/2015.
 */
public interface ConfigChangeConsumer {

    void updateConfig(Map<String, String> newImmutableMap);
}
