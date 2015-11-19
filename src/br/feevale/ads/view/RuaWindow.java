/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.view;

import br.feevale.ads.Parametros;
import br.feevale.ads.rua.Rua;
import br.feevale.ads.utils.ADS_Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author 0066115
 */
public class RuaWindow extends JFrame implements ParametrosWindow.ParametrosSalvouActionListener {
	
    public class MoverThread extends Thread {

        private boolean shouldClose = false;

        @Override
        public void run() {
            double ciclos = 1d / Parametros.ciclosPorSegundo;
            while (!shouldClose) {
                parar();
                for (Rua rua : RuaCanvas.ruas) {
                    rua.processarCiclos(ciclos);
                }
                cicloAtual++;
                //jpCanvas.invalidate();
                jpCanvas.repaint();
                updateUI();
                if (Parametros.totalDeCiclos < cicloAtual) {
                    close();
                }
            }
        }

        private void parar() {
            int tempo = 30;
            if (Parametros.ciclosPorSegundo > 0) {
                tempo = 1000 / Parametros.ciclosPorSegundo;
            }
            try {
                Thread.sleep(tempo);
            } catch (InterruptedException ex) {
                // nao faz nada
            }
        }

        public void close() {
            shouldClose = true;
        }
    }

    private boolean isDebug = false;
    private JLabel jlblCicloAtual;
    private JLabel jlblCicloMedia;
    private RuaCanvas jpCanvas;
    public Parametros parametros = new Parametros();
    private int cicloAtual;
    public MoverThread moverThread;
    public JButton btnIniciarParar;

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
        // parametros
        JButton btn = new JButton("Configurar");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                actionConfigurarClick(evt);
            }
        });
        btn.setLocation(WIDTH - 130, 10);
        btn.setSize(100, 20);
        add(btn);
        // botao de iniciar
        btnIniciarParar = new JButton("Iniciar");
        btnIniciarParar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                moverThreadIniciar();
            }
        });
        btnIniciarParar.setLocation(WIDTH - 240, 10);
        btnIniciarParar.setSize(100, 20);
        add(btnIniciarParar);
        //  ciclo
        ADS_Utils.labelForText("Ciclo atual:", 100, WIDTH - 240, 30, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCicloAtual = ADS_Utils.labelForText("0", 100, WIDTH - 130, 30, this, isDebug);
        jlblCicloAtual.setHorizontalAlignment(SwingConstants.RIGHT);
        // taxa media
        ADS_Utils.labelForText("Taxa média:", 100, WIDTH - 240, 50, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCicloMedia = ADS_Utils.labelForText("0", 100, WIDTH - 130, 50, this, isDebug);
        jlblCicloMedia.setHorizontalAlignment(SwingConstants.RIGHT);
        // canvas
        jpCanvas = new RuaCanvas();
        int canvasHeight = 700;
        jpCanvas.setSize(WIDTH - 40, canvasHeight);
        jpCanvas.setLocation(15, HEIGHT - (40 + canvasHeight));
        jpCanvas.setBackground(Color.white);
        add(jpCanvas);
        // window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (moverThread != null) {
                    moverThread.close();
                    moverThread = null;
                }
            }
        });
    }

    private void moverThreadIniciar() {
        if (moverThread != null) {
            moverThreadParar();
            btnIniciarParar.setText("Iniciar");
        } else {
            moverThread = new MoverThread();
            moverThread.start();
            btnIniciarParar.setText("Parar");
        }
    }

    private void moverThreadParar() {
        if (moverThread != null) {
            moverThread.close();
            moverThread = null;
        }
    }

    private void actionConfigurarClick(ActionEvent evt) {
        moverThreadParar();
        ParametrosWindow.createAndShow(parametros, this);
    }

    @Override
    public void onParametroSalvou(Parametros parametros) {
        moverThreadParar();
        this.parametros = parametros;
        cicloAtual = 0;
        updateUI();
    }

    private void updateUI() {
        jlblCicloAtual.setText("" + cicloAtual + " / " + parametros.totalDeCiclos);
    }

}
