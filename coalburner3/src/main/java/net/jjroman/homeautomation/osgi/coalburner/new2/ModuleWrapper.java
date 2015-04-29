package net.jjroman.homeautomation.osgi.coalburner.new2;


import net.jjroman.homeautomation.osgi.configservice.api.ConfigNamespace;
import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.measureservice.api.MeasureName;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 24/04/2015.
 */
class ModuleWrapper {

    DoubleMeasure doubleMeasure;
    ConfigService configService;
    IGPIOPin fanPin;
    IGPIOPin dispenserPin;

    CoalBurner coalBurner = null;

    public synchronized void start(){
        coalBurner = new CoalBurner( new CoalBurnerExternalsRPIGpio(fanPin, dispenserPin), doubleMeasure);
        coalBurner.init();
        configService.subscribeToConfigChanges(coalBurner, ConfigNamespace.COAL_BURNER);
    }

    public synchronized void stop(){
        coalBurner.deinit();
    }

}
