/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.view;

import br.feevale.ads.utils.ADS_Utils;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author carlosborges
 */
public class InfoListWindow extends JFrame implements ActionListener {

    private static InfoListWindow currentInstance;

    private TextArea textArea;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public InfoListWindow() {
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        // textArea
        textArea = new TextArea();
        textArea.setSize(WIDTH - 30, HEIGHT - 90);
        textArea.setLocation(15, 15);
        textArea.setEditable(false);
        add(textArea);
        // btn
        JButton btn = new JButton("OK");
        btn.addActionListener(this);
        btn.setSize(90, 40);
        btn.setDefaultCapable(true);
        btn.setLocation(WIDTH - 15 - btn.getSize().width, HEIGHT - 30 - btn.getSize().height);
        add(btn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ADS_Utils.closeWindow(this);
    }

    public static InfoListWindow createNewInstance(String title) {
        currentInstance = new InfoListWindow();
        if (title == null) {
            title = "Error list";
        }
        currentInstance.setTitle(title);
        ADS_Utils.centerWindow(currentInstance);
        return currentInstance;
    }

    public static InfoListWindow getCurrentInstance() {
        return currentInstance;
    }

    private static void createIfNull() {
        if (currentInstance == null) {
            createNewInstance(null);
        }
    }

    public static void clear() {
        createIfNull();
        currentInstance.textArea.setText("");
    }

    public static void appendStringLn(String text) {
        createIfNull();
        currentInstance.textArea.append(text + "\n");
    }

    public static void main(String[] args) {
        createNewInstance("teste");
        appendStringLn("erro 1");
        appendStringLn("erro 2");
        appendStringLn("erro 3");
        appendStringLn("erro 4");
        currentInstance.setVisible(true);
    }

}
