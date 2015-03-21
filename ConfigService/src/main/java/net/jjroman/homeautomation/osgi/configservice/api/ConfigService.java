package net.jjroman.homeautomation.osgi.configservice.api;

/**
 * Configuration service provides key - value storage for configuration keys. It introduces concept of namespaces
 * to separate configurations of different consumers.
 * Created by Jan on 17/03/2015.
 */
public interface ConfigService {
    String getConfigValueByNamespaceAndKey(String namespace, String key);
}
