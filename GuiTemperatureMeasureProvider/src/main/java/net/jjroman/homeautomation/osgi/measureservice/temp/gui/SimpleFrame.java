package net.jjroman.homeautomation.osgi.measureservice.temp.gui;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by Jan on 13/03/2015.
 */
public class SimpleFrame extends JFrame implements DoubleMeasure, ChangeListener {

    //private JTextField passage = null;
    private JSlider slider = null;

    /**
     * Area where the result is displayed.
     */
    private JLabel result = null;

    private final BundleContext bundleContext;

    public SimpleFrame(BundleContext bundleContext){
        super();
        System.out.println("Constructor called");
        initComponents();
        this.setTitle("Spellchecker Gui");

        this.bundleContext = bundleContext;
        System.out.println("Constructor finished");
    }

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
        //passage = new JTextField();
        slider = new JSlider(JSlider.HORIZONTAL, 0,100,0);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Stop Felix...
        getContentPane().setLayout(new java.awt.GridBagLayout());

        checkButton.setText("Check");
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //check();
            }
        });
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
        System.out.println("start() starting");
        this.setVisible(true);
        System.out.println("start() finished");
    }

    @Override
    public void dispose() {
        //super.dispose();
        try {
            bundleContext.getBundle().stop();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }
    public void disposeOnly(){
        super.dispose();
    }

    public void stateChanged(ChangeEvent e) {

    }
}
