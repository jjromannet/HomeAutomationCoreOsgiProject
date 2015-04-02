package net.jjroman.homeautomation.osgi.configprovider.hardcoded;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Test;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Smoke tests on Activator
 * Created by Jan on 02/04/2015.
 */
public class ActivatorTest {

    @Test
    public void testModuleBeenRegisteredInContext() throws Exception {
        BundleContext bundleContext = MockOsgi.newBundleContext();
        BundleActivator ba = new Activator();
        ba.start(bundleContext);
        ServiceReference[] serviceReferences = bundleContext.getServiceReferences(ConfigService.class.getName(), "");

        assertNotNull(serviceReferences);
        assertTrue("Returned at least one service", serviceReferences.length > 0);

        for (ServiceReference serviceReference : serviceReferences) {
            assertNotNull(serviceReference);
        }
    }

    @Test
    public void testDoNothingOnStop() throws Exception {
        BundleContext bundleContext = MockOsgi.newBundleContext();
        BundleActivator ba = new Activator();
        ba.stop(bundleContext);
    }
}
