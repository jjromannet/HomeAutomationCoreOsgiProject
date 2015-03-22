package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Jan on 17/03/2015.
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        bundleContext.registerService(
                ConfigService.class.getName(), new ConfigProvider(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        // do nothing on bundle stop
    }
}
