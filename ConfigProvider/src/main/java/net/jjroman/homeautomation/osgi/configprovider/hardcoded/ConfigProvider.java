package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigChangeConsumer;
import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;

import java.util.*;

/**
 * Implementation of base/mock version of the configuration store. This version stores
 * fixed/hardcoded values.
 * Created by Jan on 17/03/2015.
 */
public class ConfigProvider implements ConfigService {

    private final Map<String, Map<String, String>> repo;

    public ConfigProvider(){
        Map<String, String> coalBurnerProperties = new HashMap<>();
        coalBurnerProperties.put("goto.active.temperature",  "55.00");
        coalBurnerProperties.put("goto.standby.temperature", "65.00");
        Map<String, Map<String, String>> tmpRepo = new HashMap<String, Map<String, String>>();
        tmpRepo.put("coalburner", coalBurnerProperties);

        Map<String, String> system = new HashMap<String, String>();
        system.put("test", "test");
        tmpRepo.put("system-test", system);
        tmpRepo.put("system-null", null);
        this.repo = Collections.unmodifiableMap(tmpRepo);
    }

    @Override
    public Map<String, String> getConfigValuesForNamespace(String namespace) {
        Map<String, String> returnMap = new HashMap<>();
        if(namespace == null){
            return returnMap;
        }
        if(repo.containsKey(namespace)){
            return Collections.unmodifiableMap(repo.get(namespace));
        }
        return returnMap;
    }

    @Override
    public Map<String, Map<String, String>> getConfigValuesForNamespaces(Iterable<String> namespaces) {
        Map<String, Map<String, String>> returnMap = new HashMap<>();
        if(namespaces != null) {
            for (String namespace : namespaces) {
                if (repo.containsKey(namespace)) {
                    returnMap.put(namespace, Collections.unmodifiableMap(repo.get(namespace)));
                }
            }
        }
        return returnMap;
    }

    @Override
    public Map<String, String> subscribeToConfigChanges(ConfigChangeConsumer consumer, String namespace) {
        Map<String, String> currentConfig = getConfigValuesForNamespace(namespace);
        // FIXME
        consumer.updateConfig(currentConfig);
        return currentConfig;
    }
}
