package br.feevale.ads.view;

import br.feevale.ads.Parametros;
import br.feevale.ads.car.Car;
import br.feevale.ads.street.Street;
import br.feevale.ads.utils.ADS_Utils;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
            ts_inicio = System.currentTimeMillis();
            while (!shouldClose) {
                parar();
                for (Street rua : RuaCanvas.ruas) {
                    rua.processarCiclos(ciclos);
                }
                cicloAtual++;
                //jpCanvas.invalidate();
                jpCanvas.repaint();
                ts_fim = System.currentTimeMillis();
                updateUI();
                if (rua.carrosConcluidos >= Parametros.totalVeiculos) {
                    // valida se todos os carros ja encerraram o percurso
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
    private JLabel jlblCarrosConcluidos;
    private JLabel jlblCicloAtual;
    private JLabel jlblCarrosTotal;
    private JLabel jlblVelocidadeMaxima;
    private RuaCanvas jpCanvas;
    public MoverThread moverThread;
    public JButton btnIniciarParar;
    public static Street rua;
    private int cicloAtual = 0;

    private long ts_inicio = 0;
    private long ts_fim = 0;

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 300;

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
        // exportar
        btn = new JButton("Exportar");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                exportarAction();
            }
        });
        btn.setLocation(WIDTH - 350, 10);
        btn.setSize(100, 20);
        add(btn);
        //  ciclo
        ADS_Utils.labelForText("Ciclo atual:", 100, WIDTH - 240, 30, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCicloAtual = ADS_Utils.labelForText("0", 100, WIDTH - 130, 30, this, isDebug);
        jlblCicloAtual.setHorizontalAlignment(SwingConstants.RIGHT);
        // carros
        ADS_Utils.labelForText("Carros add:", 100, WIDTH - 240, 50, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCarrosTotal = ADS_Utils.labelForText("0", 100, WIDTH - 130, 50, this, isDebug);
        jlblCarrosTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        // carros concluidos
        ADS_Utils.labelForText("Carros fin:", 100, WIDTH - 240, 70, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblCarrosConcluidos = ADS_Utils.labelForText("0", 100, WIDTH - 130, 70, this, isDebug);
        jlblCarrosConcluidos.setHorizontalAlignment(SwingConstants.RIGHT);
        // velocidade maxima
        ADS_Utils.labelForText("Vel. máx:", 100, WIDTH - 240, 90, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        jlblVelocidadeMaxima = ADS_Utils.labelForText("0", 100, WIDTH - 130, 90, this, isDebug);
        jlblVelocidadeMaxima.setHorizontalAlignment(SwingConstants.RIGHT);
        // canvas
        jpCanvas = new RuaCanvas();
        int canvasHeight = 100;
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
        // atualiza
        updateUI();
    }

    private void exportarAction() {
        if (rua == null) {
            return;
        }
        JFileChooser fc = new JFileChooser();
        int retorno = fc.showSaveDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            PrintWriter out = null;
            try {
                file.createNewFile();
                out = new PrintWriter(file);
                for (Car carro : rua.getCarros()) {
                    out.println(carro.getId() + "\t" + carro.getTimeIniciou() + "\t" + carro.getTimeConcluiu());
                }
                out.close();
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                System.out.println("Fudeu");
                ex.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    private void moverThreadIniciar() {
        if (moverThread != null) {
            moverThreadParar();
        } else {
            jpCanvas.loadRuas(null);
            moverThread = new MoverThread();
            moverThread.start();
            btnIniciarParar.setText("Parar");
            jpCanvas.invalidate();
            jpCanvas.repaint();
        }
    }

    private void moverThreadParar() {
        if (moverThread != null) {
            moverThread.close();
            moverThread = null;
            btnIniciarParar.setText("Iniciar");
        }
    }

    private void actionConfigurarClick(ActionEvent evt) {
        ParametrosWindow.createAndShow(this);
    }

    @Override
    public void onParametroSalvou() {
        jpCanvas.loadRuas(null);
        moverThreadParar();
        updateUI();
    }

    private void updateUI() {
        double tempo = ts_fim - ts_inicio;
        tempo /= 1000.0;
        jlblCicloAtual.setText(String.format("%.3f", tempo) + "s - " + cicloAtual);
        jlblCarrosTotal.setText("" + rua.getCarros().size() + "/" + Parametros.totalVeiculos);
        jlblCarrosConcluidos.setText("" + rua.carrosConcluidos + "/" + Parametros.totalVeiculos);
        jlblVelocidadeMaxima.setText("" + Parametros.velocidadeMaxima);
    }

}
