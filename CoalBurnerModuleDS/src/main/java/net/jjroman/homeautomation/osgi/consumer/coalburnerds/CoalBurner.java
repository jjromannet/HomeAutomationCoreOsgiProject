package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import java.util.concurrent.*;

/**
 * Created by Jan on 05/04/2015.
 */
@Component
public class CoalBurner {

    private IGPIOPin fanPin;
    private IGPIOPin dispenserPin;
    private DoubleMeasure coalBurnerWaterTank;
    private ConfigService configService;
    private LogService logService;

    @Reference(
            name = "fanPin",
            cardinality = ReferenceCardinality.MANDATORY,
            unbind = "unsetFanPin",
            policy = ReferencePolicy.STATIC,
            target = "(PinNumber=1)",
            service = IGPIOPin.class
    )
    public void setFanPin(IGPIOPin fanPin) {
        this.fanPin = fanPin;
    }
    public void unsetFanPin(IGPIOPin fanPin) {
        if(fanPin != null && fanPin.equals(this.fanPin)) {
            this.fanPin = null;
        }else{
            logService.log(LogService.LOG_WARNING, "Skipping fan unset operation");
        }
    }

    @Reference(
            name = "dispenserPin",
            cardinality = ReferenceCardinality.MANDATORY,
            unbind = "unsetDispenserPin",
            policy = ReferencePolicy.STATIC,
            target = "(PinNumber=2)",
            service = IGPIOPin.class
    )
    public void setDispenserPin(IGPIOPin dispenserPin) {
        this.dispenserPin = dispenserPin;
    }

    public void unsetDispenserPin(IGPIOPin dispenserPin) {
        if(dispenserPin != null && dispenserPin.equals(this.dispenserPin)) {
            this.dispenserPin = null;
        }else{
            logService.log(LogService.LOG_WARNING, "Skipping dispenser unset operation");
        }
    }



    @Reference(
            name = "waterTankMeasure",
            cardinality = ReferenceCardinality.MANDATORY,
            unbind = "unsetCoalBurnerWaterTank",
            policy = ReferencePolicy.STATIC,
            service = DoubleMeasure.class
    )
    public void setCoalBurnerWaterTank(DoubleMeasure coalBurnerWaterTank) {
        this.coalBurnerWaterTank = coalBurnerWaterTank;
    }
    public void unsetCoalBurnerWaterTank(DoubleMeasure coalBurnerWaterTank) {
        if(coalBurnerWaterTank != null && coalBurnerWaterTank.equals(this.coalBurnerWaterTank)) {
            this.coalBurnerWaterTank = null;
        }else{
            logService.log(LogService.LOG_WARNING, "Skipping coalBurnerWaterTank unset operation");
        }

    }




    @Reference(
            name = "configService",
            cardinality = ReferenceCardinality.MANDATORY,
            unbind = "unsetConfigService",
            policy = ReferencePolicy.STATIC,
            service = ConfigService.class
    )
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public void unsetConfigService(ConfigService configService) {
        if(configService != null && configService.equals(this.configService)) {
            this.configService = null;
        }else{
            logService.log(LogService.LOG_WARNING, "Skipping configService unset operation");
        }

    }


    @Reference(
            name = "logService",
            cardinality = ReferenceCardinality.MANDATORY,
            unbind = "unsetLogService",
            policy = ReferencePolicy.STATIC,
            service = LogService.class
    )
    public void setLogService(LogService logService) {
        this.logService = logService;
    }
    public void unsetLogService(LogService logService) {
        if(logService != null && logService.equals(this.logService)) {
            this.logService = null;
        }else{
            logService.log(LogService.LOG_WARNING, "Skipping logService unset operation");
        }

    }


    private ScheduledExecutorService scheduledExecutorService;
    protected synchronized void activate(ComponentContext context)
    {
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdownNow();
            throw new UnsupportedOperationException("scheduledExecutorService has not been shutdown and nulled");
        }
        CycleExecutor activeCycleExecutor = new ActiveCycleExecutor(fanPin,dispenserPin);
        CycleExecutor stanCycleExecutor = new StandbyCycleExecutor(fanPin,dispenserPin);

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new CoalBurnerRunnable(stanCycleExecutor, activeCycleExecutor), 1, 1, TimeUnit.SECONDS);
        logService.log(LogService.LOG_INFO, "Activating ... ");
    }

    protected synchronized void deactivate(ComponentContext context)
    {
        scheduledExecutorService.shutdown();
        logService.log(LogService.LOG_INFO, "Deactivating ... ");
    }
}

