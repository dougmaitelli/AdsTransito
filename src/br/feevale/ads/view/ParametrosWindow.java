/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import br.feevale.ads.Parametros;
import br.feevale.ads.utils.ADS_Utils;
import br.feevale.ads.utils.SpringUtilities;

/**
 *
 * @author 0066115
 */
public class ParametrosWindow extends JFrame {

    public static interface ParametrosSalvouActionListener {

        public void onParametroSalvou(Parametros parametros);

    }

    private JPanel panel = new JPanel();

    public static final int WIDTH = 220;
    public static final int HEIGHT = 226;

    private JTextField jtfCiclosPorSegundo;
    private JTextField jtfNumeroVeiculos;
    private JTextField jtfIntervaloEntradaMin;
    private JTextField jtfIntervaloEntradaMax;

    public ParametrosSalvouActionListener listener;

    public static ParametrosWindow singleton = null;

    private void addLabel(String text) {
        JLabel lbl = new JLabel(text);
        panel.add(lbl);
    }

    public ParametrosWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel.setSize(WIDTH, HEIGHT);
        panel.setLayout(new SpringLayout());
        panel.setLocation(0, 0);
        add(panel);
        // size
        setSize(WIDTH, HEIGHT);
        setLayout(new SpringLayout());
        setTitle("Parâmetros");
        // taxa por segundo
        addLabel("Ciclos por segundo:");
        jtfCiclosPorSegundo = new JTextField("30");
        jtfCiclosPorSegundo.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfCiclosPorSegundo.setSize(100, ADS_Utils.FIELD_HEIGHT);
        panel.add(jtfCiclosPorSegundo);
        // numero de veiculos inseridos
        addLabel("Total de veículos:");
        jtfNumeroVeiculos = new JTextField("100");
        jtfNumeroVeiculos.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfNumeroVeiculos.setSize(100, ADS_Utils.FIELD_HEIGHT);
        panel.add(jtfNumeroVeiculos);
        // intervalo de entrada
        addLabel("Intervalo min.:");
        jtfIntervaloEntradaMin = new JTextField("20");
        jtfIntervaloEntradaMin.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfIntervaloEntradaMin.setSize(100, ADS_Utils.FIELD_HEIGHT);
        panel.add(jtfIntervaloEntradaMin);
        // intervalo minimo saida
        addLabel("Intervalo max:");
        jtfIntervaloEntradaMax = new JTextField("60");
        jtfIntervaloEntradaMax.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfIntervaloEntradaMax.setSize(100, ADS_Utils.FIELD_HEIGHT);
        panel.add(jtfIntervaloEntradaMax);
        // cancelar
        JButton btn = new JButton("Cancelar");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCancelarClick();
            }
        });
        panel.add(btn);
        // salvar
        btn = new JButton("Salvar");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSalvarClick();
            }
        });
        panel.add(btn);
        //Lay out the panel.
        SpringUtilities.makeCompactGrid(panel, //parent
                5, 2, // rows, cols
                5, 5, //initX, initY
                10, 10); //xPad, yPad
        // se fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                singleton = null;
            }
        });
    }

    public static void createAndShow(Parametros param, ParametrosSalvouActionListener listener) {
        singleton = new ParametrosWindow();
        ADS_Utils.centerWindow(singleton);
        singleton.loadParametros(param);
        singleton.listener = listener;
        singleton.setVisible(true);
    }

    private void loadParametros(Parametros param) {
        jtfCiclosPorSegundo.setText(String.valueOf(param.ciclosPorSegundo));
        jtfNumeroVeiculos.setText(String.valueOf(param.totalVeiculos));
        jtfIntervaloEntradaMin.setText(String.valueOf(param.intervaloVeiculoMin));
        jtfIntervaloEntradaMax.setText(String.valueOf(param.intervaloVeiculoMax));
    }

    private void actionSalvarClick() {
        try {
            Parametros parametros = new Parametros();
            parametros.ciclosPorSegundo = Integer.parseInt(jtfCiclosPorSegundo.getText());
            parametros.totalVeiculos = Integer.parseInt(jtfNumeroVeiculos.getText());
            parametros.intervaloVeiculoMin = Integer.parseInt(jtfIntervaloEntradaMin.getText());
            parametros.intervaloVeiculoMax = Integer.parseInt(jtfIntervaloEntradaMax.getText());
            if (listener != null) {
                listener.onParametroSalvou(parametros);
            }
            ADS_Utils.closeWindow(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Formato de dados errado.");
        }
    }

    private void actionCancelarClick() {
        ADS_Utils.closeWindow(this);
    }

}
