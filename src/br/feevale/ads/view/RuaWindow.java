/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.view;

import br.feevale.ads.utils.ADS_Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author 0066115
 */
public class RuaWindow extends JFrame {
    
    private boolean isDebug = false;
    
    private JTextField jtfCiclos;
    private JTextField jtfTaxaPorSegundo;
    
    private JLabel jlblCicloAtual;
    private JLabel jlblCicloMedia;
    
    private RuaCanvas jpCanvas;
    
    private int cicloAtual;
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;

    public RuaWindow() {
        setLayout(null);
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setSize(size);
        setResizable(false);
        ADS_Utils.centerWindow(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // dados da janela
        setTitle("Simulador de Transito - 4N (Análise de desempenho de sistemas 201502)");
        ADS_Utils.labelForText("Semestre 2015/02 Turma 4N - Alunos:", 300, 30, 10, this);
        ADS_Utils.labelForText("0066115 - Carlos Eduardo Liedtke Borges", 300, 30, 30, this);
        ADS_Utils.labelForText("0158378 - Douglas Alan Maitelli", 300, 30, 50, this);
        ADS_Utils.labelForText("0082006 - Tiago Steil", 300, 30, 70, this);
        ADS_Utils.labelForText("Professora: Adriana Neves dos Reis", 300, 30, 90, this);
        // ciclos
        jtfCiclos = new JTextField("900");
        jtfCiclos.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfCiclos.setSize(100, ADS_Utils.FIELD_HEIGHT);
        jtfCiclos.setLocation(WIDTH - jtfCiclos.getWidth() - 30, 10);
        add(jtfCiclos);
        ADS_Utils.labelForText("Total de ciclos:", 100, WIDTH - 240, 10, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        // taxa por segundo
        jtfTaxaPorSegundo = new JTextField("30");
        jtfTaxaPorSegundo.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfTaxaPorSegundo.setSize(100, ADS_Utils.FIELD_HEIGHT);
        jtfTaxaPorSegundo.setLocation(WIDTH - jtfTaxaPorSegundo.getWidth() - 30, 40);
        add(jtfTaxaPorSegundo);
        ADS_Utils.labelForText("Taxa por seg.:", 100, WIDTH - 240, 40, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        //  ciclo
        ADS_Utils.labelForText("Ciclo atual:", 100, WIDTH - 240, 70, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCicloAtual = ADS_Utils.labelForText("0", 100, WIDTH - 130, 70, this, isDebug);
        jlblCicloAtual.setHorizontalAlignment(SwingConstants.RIGHT);
        ADS_Utils.labelForText("Taxa média:", 100, WIDTH - 240, 100, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCicloMedia = ADS_Utils.labelForText("0", 100, WIDTH - 130, 100, this, isDebug);
        jlblCicloMedia.setHorizontalAlignment(SwingConstants.RIGHT);
        // canvas
        jpCanvas = new RuaCanvas();
        int canvasHeight = 700;
        jpCanvas.setSize(WIDTH - 40, canvasHeight);
        jpCanvas.setLocation(15, HEIGHT - (40 + canvasHeight));
        jpCanvas.setBackground(Color.white);
        add(jpCanvas);
    }
    
}
