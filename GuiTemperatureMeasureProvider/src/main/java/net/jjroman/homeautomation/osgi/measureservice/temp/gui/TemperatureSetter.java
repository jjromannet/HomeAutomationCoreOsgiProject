package net.jjroman.homeautomation.osgi.measureservice.temp.gui;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;

import javax.swing.*;

/**
 * Manually adjusted measure provider for temperature.
 * Mock measure to test bundles behaviour. It displays JFrame with slider to adjust measure reading.
 * Created by Jan on 13/03/2015.
 */
public class TemperatureSetter implements DoubleMeasure {
    private JTextField textField1;

    @Override
    public double getValue() {
        double retval = 0;
        if(textField1 != null){
            String val = textField1.getText();
            try {
                retval = Double.parseDouble(val);
            }catch (NumberFormatException nfe){
                textField1.setText("");
            }
        }
        return retval;
    }
}
