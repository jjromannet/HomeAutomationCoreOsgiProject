package net.jjroman.homeautomation.osgi.pinconsumer.pi4j;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import java.util.Properties;

/**
 * Activator using Felix Dependency Manager for CoalBurnerController
 * Created by Jan on 07/03/2015.
 */
public class Activator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext bundleContext, DependencyManager dependencyManager) throws Exception {

        Properties properties = new Properties();
        properties.put(CommandProcessor.COMMAND_SCOPE, "coalburner");
        properties.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"start", "stop"});

        dependencyManager.add(createComponent()
                        .setInterface(Object.class.getName(), properties)
                        .setImplementation(CoalBurnerController.class)
                                // TODO xxx call start and stop method of the object
                        .add(createServiceDependency()
                                .setService(IGPIOPin.class, "(PinNumber=1)")
                                .setRequired(true)
                                .setAutoConfig("fanPin"))
                        .add(createServiceDependency()
                                .setService(IGPIOPin.class, "(PinNumber=2)")
                                .setRequired(true)
                                .setAutoConfig("dispenserPin"))
                        .add(createServiceDependency()
                                .setService(DoubleMeasure.class, "(MeasureStringId=CoalBurnerWaterTank)")
                                .setRequired(true)
                                .setAutoConfig("coalBurnerWaterTank"))
                        .add(createServiceDependency()
                                .setService(ConfigService.class)
                                .setRequired(true)
                                .setAutoConfig("configService"))
                        .add(createServiceDependency().setService(LogService.class))

        );

    }
}
