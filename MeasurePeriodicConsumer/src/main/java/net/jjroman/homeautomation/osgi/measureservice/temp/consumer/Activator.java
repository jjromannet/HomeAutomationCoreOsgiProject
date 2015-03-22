package net.jjroman.homeautomation.osgi.measureservice.temp.consumer;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 * Activator for periodic measure consumer requires measure with following property:
 * (MeasureStringId=CoalBurnerWaterTank)
 *
 * Created by Jan on 13/03/2015.
 */
public class Activator extends DependencyActivatorBase{

    @Override
    public void init(BundleContext bundleContext, DependencyManager dependencyManager) throws Exception {
        dependencyManager.add(createComponent()
                .setInterface(Object.class.getName(), null)
                .setImplementation(MeasureConsumer.class)
                .add(createServiceDependency()
                        .setService(LogService.class)
                        .setAutoConfig(true)
                        .setRequired(true) )
                .add(createServiceDependency()
                                .setService(DoubleMeasure.class, "(MeasureStringId=CoalBurnerWaterTank)")
                                .setRequired(true)
                                .setCallbacks("serviceAdded", "serviceRemoved")
                                .setAutoConfig(true)
                ));

    }
}
