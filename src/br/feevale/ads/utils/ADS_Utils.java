/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JLabel;

/**
 *
 * @author 0066115
 */
public class ADS_Utils {

    public static int FIELD_HEIGHT = 25;

    public static Random random;

    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void closeWindow(Window frame) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static JLabel labelForText(String text, int width, int x, int y, Container container, boolean test) {
        JLabel lbl = new JLabel(text);
        lbl.setSize(width, FIELD_HEIGHT);
        lbl.setLocation(x, y);
        if (test) {
            lbl.setOpaque(true);
            lbl.setBackground(Color.YELLOW);
        }
        container.add(lbl);
        return lbl;
    }

    public static JLabel labelForText(String text, int width, int x, int y, Container container) {
        return labelForText(text, width, x, y, container, false);
    }

    public static void setRandomSeed(long seed) {
        random = new Random(seed);
    }

    public static float getNextFloat() {
        if (random == null) {
            random = new Random();
        }
        return random.nextFloat();
    }

    public static double oscilateDouble(double maxValue, double minPercent) {
        double gen = maxValue * minPercent;
        gen += (getNextFloat() * maxValue) * (1.0 - minPercent);
        if (gen > maxValue) {
            return maxValue;
        }
        return gen;
    }

}
