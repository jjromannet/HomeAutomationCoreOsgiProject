package net.jjroman.homeautomation.osgi.measureservice.temp.gui;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Activator for JFrame pin provider - GUI reflection of pin states.
 * Created by Jan on 13/03/2015.
 */
public class Activator implements BundleActivator {

    private SimpleFrame simpleFrame = null;

    @Override
    public void start(final BundleContext bundleContext) throws Exception {


        simpleFrame = new SimpleFrame(bundleContext);

        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("MeasureStringId", "CoalBurnerWaterTank");
        bundleContext.registerService(
                DoubleMeasure.class.getName(), simpleFrame, props);
        simpleFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        simpleFrame.start();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        simpleFrame.disposeOnly();
    }
}
