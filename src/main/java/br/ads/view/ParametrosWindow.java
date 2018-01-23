package br.ads.view;

import br.ads.Parametros;
import br.ads.Parametros.Tipo;
import br.ads.utils.ADS_Utils;
import br.ads.utils.SpringUtilities;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

/**
 *
 * @author 0066115
 */
public class ParametrosWindow extends JFrame {

    public static interface ParametrosSalvouActionListener {

        public void onParametroSalvou();

    }

    public static final int WIDTH = 220;
    public static final int HEIGHT = 226;
    public static ParametrosWindow singleton = null;

    private Container panel;
    private HashMap<Tipo, JTextField> campos;

    private ParametrosSalvouActionListener listener;

    private void addLabel(String text) {
        if (!text.endsWith(":")) {
            text += ":";
        }
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl);
    }

    public ParametrosWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLocation(0, 0);
        panel.setLayout(new SpringLayout());
        // size
        setLayout(new SpringLayout());
        setTitle("Parâmetros");
        // cria
        HashMap<Tipo, String> map = Parametros.getListParametros();
        List<Tipo> list = Parametros.Tipo.orderedList();
        campos = new HashMap<Tipo, JTextField>();
        for (Tipo tp : list) {
            String valor = map.get(tp);
            if (valor == null) {
                valor = "0";
            }
            addLabel(tp.getTitulo());
            JTextField fd = new JTextField(valor);
            fd.setHorizontalAlignment(SwingConstants.RIGHT);
            fd.setSize(100, ADS_Utils.FIELD_HEIGHT);
            panel.add(fd);
            // adiciona campo
            campos.put(tp, fd);
        }
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
                map.size() + 1, 2, // rows, cols
                5, 5, //initX, initY
                10, 10); //xPad, yPad
        // se fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                singleton = null;
            }
        });
        // panel
        setContentPane(panel);
        pack();
    }

    public static void createAndShow(ParametrosSalvouActionListener listener) {
        singleton = new ParametrosWindow();
        ADS_Utils.centerWindow(singleton);
        singleton.listener = listener;
        singleton.setVisible(true);
    }

    private void actionSalvarClick() {
        try {
            HashMap<Tipo, String> valores = new HashMap<Tipo, String>();
            for (Tipo tp : campos.keySet()) {
                valores.put(tp, campos.get(tp).getText());
            }
            // salva
            if (!Parametros.putListParametros(valores)) {
                JOptionPane.showMessageDialog(null, "Formato de dados errado.");
                return;
            }
            // avisa
            if (listener != null) {
                listener.onParametroSalvou();
            }
            // fecha
            ADS_Utils.closeWindow(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Formato de dados errado.");
        }
    }

    private void actionCancelarClick() {
        ADS_Utils.closeWindow(this);
    }

}
