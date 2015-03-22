package net.jjroman.homeautomation.osgi.measureservice.temp.gui;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Provides GUI for manual manipulation of measure value.
 * Created by Jan on 13/03/2015.
 */
public class SimpleFrame extends JFrame implements DoubleMeasure, ChangeListener {

    private JSlider slider = null;

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

    private void logService(int level, String mesage, Throwable th){
        LogService logService = null;
        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(LogService.class.getName(), "");
            if (refs != null && refs.length > 0) {
                Object service = bundleContext.getService(refs[0]);
                if (LogService.class.isAssignableFrom(service.getClass())) {
                    logService = (LogService) service;
                }
            }
        }catch (InvalidSyntaxException ise){
            logOnStdErr(ise);
        }
        if(logService == null){
            System.err.print(String.format("Severity: %d, message: %s", level, mesage));
            if(th != null){
                logOnStdErr(th);
            }
            System.err.println();
        }else{
            if(th == null){
                logService.log(level, mesage);
            }else{
                logService.log(level, mesage, th);
            }
        }

    }
    private void logOnStdErr(Throwable th){
        if(th == null){
            return;
        }
        System.err.println(th.getMessage());
        for(StackTraceElement element: th.getStackTrace()){
            System.err.print("\t");
            System.err.println(element.toString());
        }
    }
}
