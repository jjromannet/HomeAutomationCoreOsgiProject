package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;

import java.util.*;

/**
 * Implementation of base/mock version of the configuration store. This version stores
 * fixed/hardcoded values.
 * Created by Jan on 17/03/2015.
 */
public class ConfigProvider implements ConfigService {

    private final Map<String, Properties> repo;

    public ConfigProvider(){
        Properties coalBurnerProperties = new Properties();
        coalBurnerProperties.setProperty("goto.active.temperature",  "55.00");
        coalBurnerProperties.setProperty("goto.standby.temperature", "65.00");
        Map<String, Properties> tmpRepo = new HashMap<String, Properties>();
        tmpRepo.put("coalburner", coalBurnerProperties);
        this.repo = Collections.unmodifiableMap(tmpRepo);
    }

    @Override
    public String getConfigValueByNamespaceAndKey(String namespace, String key) {
        if(namespace == null || key == null) {
            return null;
        }
        if(repo.containsKey(namespace) && repo.get(namespace).containsKey(key) ){
            return repo.get(namespace).getProperty(key);
        }
        return null;
    }
}
