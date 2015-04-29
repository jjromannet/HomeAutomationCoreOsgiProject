package net.jjroman.homeautomation.osgi.configservice.api;

import java.util.Map;

/**
 * Configuration service provides key - value storage for configuration keys. It introduces concept of namespaces
 * to separate configurations of different consumers.
 *
 * If in one namespace there are many keys like for instance coalburner can have water tank temp and maximum standby
 * time. Service consumer may request one config key then config may be updated by the user and then consumer may
 * request another key which in this key will be from different set so it may lead to uncertain behaviour. This is why
 * Getting all keys from certain namespace is recommended at all time!
 *
 * Created by Jan on 17/03/2015.
 */
public interface ConfigService {
    /**
     * This method has been removed as it may be inconsistent - this may happen when config are updated in meantime
     * between geting two keys from same namespace use @see getConfigValuesForNamespace to get immuatable
     * set of configs, that are consistent.
     * @param namespace namespace of configuration needed
     * @param key particular key needed
     * @return String value of configured key
     *
     * String getConfigValueByNamespaceAndKey(String namespace, String key);
     *
     */

    /**
     * this method is thread safe and transactional, all returned keys are from one config set it is not possible to
     * get mixed values  see docs for the interface
     * @param namespace namespace of configuration needed
     * @return all keys belong to the configuration
     */
    Map<String, String> getConfigValuesForNamespace(String namespace);

    /**
     * Thread safe method to get more than one namespace keys at once
     * @param namespaces multiple namespaces of configurations needed
     * @return all keys belong to the configuration grouped by configuration in first level of map
     */
    Map<String, Map<String, String>> getConfigValuesForNamespaces(Iterable<String> namespaces);


    Map<String, String> subscribeToConfigChanges(ConfigChangeConsumer consumer, ConfigNamespace namespace);
}
