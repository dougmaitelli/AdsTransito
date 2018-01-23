package br.ads.view;

import br.ads.Parameters;
import br.ads.car.Car;
import br.ads.street.Street;
import br.ads.utils.AdsUtils;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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

public class StreetWindow extends JFrame implements ParametersWindow.ParametersSavedActionListener {

    private boolean isDebug = false;

    public JButton buttonStartStop;
    private JLabel labelCurrentCycle;
    private JLabel labelCarsTotal;
    private JLabel labelCarsFinished;
    private JLabel labelMaxSpeed;

    private StreetCanvas jpCanvas;

    public MoveThread moveThread;
    public static Street street;
    private int currentCycle = 0;

    private long ts_start = 0;
    private long ts_end = 0;

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 250;

    public class MoveThread extends Thread {

        private boolean shouldClose = false;

        @Override
        public void run() {
            double cycles = 1d / Parameters.cyclesPerSecond;
            ts_start = System.currentTimeMillis();
            currentCycle = 0;
            while (!shouldClose) {
                pause();

                for (Street rua : StreetCanvas.streets) {
                    rua.processCycles(cycles);
                }
                currentCycle++;

                //jpCanvas.invalidate();
                jpCanvas.repaint();

                ts_end = System.currentTimeMillis();
                updateUI();

                // validate if all vehicles ended course
                if (street.finishedCars >= Parameters.totalVehicles) {
                    close();
                }
            }
        }

        private void pause() {
            int time = 30;
            if (Parameters.cyclesPerSecond > 0) {
                time = 1000 / Parameters.cyclesPerSecond;
            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                // nothing
            }
        }

        public void close() {
            shouldClose = true;
        }
    }

    public StreetWindow() {
        setLayout(null);
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setSize(size);
        setResizable(false);
        AdsUtils.centerWindow(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("ADS Transit");

        buttonStartStop = new JButton("Start");
        buttonStartStop.addActionListener((ActionEvent evt) -> moveThreadStart());
        buttonStartStop.setLocation(WIDTH - 350, 10);
        buttonStartStop.setSize(100, 30);
        add(buttonStartStop);

        JButton btn = new JButton("Config");
        btn.addActionListener((ActionEvent evt) -> actionConfigClick());
        btn.setLocation(WIDTH - 240, 10);
        btn.setSize(100, 30);
        add(btn);

        btn = new JButton("Export");
        btn.addActionListener((ActionEvent evt) -> exportAction());
        btn.setLocation(WIDTH - 130, 10);
        btn.setSize(100, 30);
        add(btn);

        AdsUtils.labelForText("Current Cycle:", 100, 10, 10, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        labelCurrentCycle = AdsUtils.labelForText("0", 100, 100, 10, this, isDebug);
        labelCurrentCycle.setHorizontalAlignment(SwingConstants.RIGHT);

        AdsUtils.labelForText("Cars Total:", 100, 10, 30, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        labelCarsTotal = AdsUtils.labelForText("0", 100, 100, 30, this, isDebug);
        labelCarsTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        AdsUtils.labelForText("Cars Finished:", 100, 10, 50, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        labelCarsFinished = AdsUtils.labelForText("0", 100, 100, 50, this, isDebug);
        labelCarsFinished.setHorizontalAlignment(SwingConstants.RIGHT);

        AdsUtils.labelForText("Max Speed:", 100,  10, 70, this, isDebug).setHorizontalAlignment(SwingConstants.RIGHT);
        labelMaxSpeed = AdsUtils.labelForText("0", 100, 100, 70, this, isDebug);
        labelMaxSpeed.setHorizontalAlignment(SwingConstants.RIGHT);

        // Canvas
        jpCanvas = new StreetCanvas();
        int canvasHeight = 100;
        jpCanvas.setSize(WIDTH - 40, canvasHeight);
        jpCanvas.setLocation(15, HEIGHT - (40 + canvasHeight));
        jpCanvas.setBackground(Color.white);
        add(jpCanvas);

        // Window Close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (moveThread != null) {
                    moveThread.close();
                    moveThread = null;
                }
            }
        });

        updateUI();
    }

    private void exportAction() {
        if (street == null) {
            return;
        }

        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            PrintWriter out = null;
            try {
                file.createNewFile();
                out = new PrintWriter(file);
                for (Car car : street.getCars()) {
                    out.println(car.getId() + "\t" + car.getTimeStarted() + "\t" + car.getTimeFinished());
                }
                out.close();
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    private void moveThreadStart() {
        if (moveThread != null) {
            moveThreadStop();
        } else {
            jpCanvas.loadStreets();
            moveThread = new MoveThread();
            moveThread.start();
            buttonStartStop.setText("Stop");
            jpCanvas.invalidate();
            jpCanvas.repaint();
        }
    }

    private void moveThreadStop() {
        if (moveThread != null) {
            moveThread.close();
            moveThread = null;
            buttonStartStop.setText("Start");
        }
    }

    private void actionConfigClick() {
        ParametersWindow.createAndShow(this);
    }

    @Override
    public void onParametersSave() {
        jpCanvas.loadStreets();
        moveThreadStop();
        updateUI();
    }

    private void updateUI() {
        double tempo = ts_end - ts_start;
        tempo /= 1000.0;

        labelCurrentCycle.setText(String.format("%.3f", tempo) + "s - " + currentCycle);
        labelCarsTotal.setText(street.getCars().size() + "/" + Parameters.totalVehicles);
        labelCarsFinished.setText(street.finishedCars + "/" + Parameters.totalVehicles);
        labelMaxSpeed.setText(String.valueOf(Parameters.maxSpeed));
    }

}
