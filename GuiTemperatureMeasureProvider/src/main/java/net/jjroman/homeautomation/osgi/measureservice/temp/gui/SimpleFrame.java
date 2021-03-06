package net.jjroman.homeautomation.osgi.measureservice.temp.gui;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Provides GUI for manual manipulation of measure value.
 * Created by Jan on 13/03/2015.
 */
public class SimpleFrame extends JFrame implements DoubleMeasure, ChangeListener {

    private JSlider slider = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFrame.class.getName());

    /**
     * Area where the result is displayed.
     */
    private JLabel result = null;

    private final transient BundleContext bundleContext;

    public SimpleFrame(BundleContext bundleContext){
        super();

        logService(LogService.LOG_DEBUG, "Constructor called", null);
        initComponents();
        this.setTitle("Spellchecker Gui");

        this.bundleContext = bundleContext;
        logService(LogService.LOG_DEBUG, "Constructor finished", null);
    }

    @Override
    public double getValue() {
        double retval = 0;
        if(slider != null){
            retval = slider.getValue();
        }
        return retval;
    }


    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        // The check button
        JButton checkButton = new JButton();
        result = new JLabel();
        slider = new JSlider(JSlider.HORIZONTAL, 0,100,0);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Stop Felix...
        getContentPane().setLayout(new java.awt.GridBagLayout());

        checkButton.setText("Check");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(checkButton, gridBagConstraints);

        result.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(result, gridBagConstraints);

        slider.setPreferredSize(new java.awt.Dimension(175, 20));
        slider.setEnabled(true);
        slider.addChangeListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);

        getContentPane().add(slider, gridBagConstraints);

        pack();
    }

    public void start() {
        logService(LogService.LOG_DEBUG, "start() starting", null);
        this.setVisible(true);
        logService(LogService.LOG_DEBUG, "start() finished", null);
    }

    @Override
    public void dispose() {
        try {
            bundleContext.getBundle().stop();
        } catch (BundleException e) {
            logService(LogService.LOG_WARNING, "Bundle exception on stop", e);
        }
    }
    public void disposeOnly(){
        super.dispose();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // required by slider
    }

    private LogService getLog() {
        if(bundleContext == null) {
            return null;
        }
        ServiceReference logRef = bundleContext
                .getServiceReference(LogService.class.getName());
        if (logRef != null) {
            return (LogService) bundleContext
                    .getService(logRef);
        }
        return null;
    }

    private void logService(int level, String mesage, Throwable th){
        LogService logService = getLog();

        if(logService == null){
            switch (level){
                case LogService.LOG_ERROR:
                    LOGGER.error(mesage, th);
                    break;
                case LogService.LOG_WARNING:
                    LOGGER.warn(mesage, th);
                    break;
                case LogService.LOG_INFO:
                    LOGGER.info(mesage, th);
                    break;
                default:
                    LOGGER.debug(mesage,th);
            }
        }else{
            if(th == null){
                logService.log(level, mesage);
            }else{
                logService.log(level, mesage, th);
            }
        }

    }
}
