package br.ads.view;

import br.ads.Parameters;
import br.ads.utils.AdsUtils;
import br.ads.utils.SpringUtilities;
import java.awt.Container;
import java.awt.event.ActionEvent;
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

public class ParametersWindow extends JFrame {

    private static ParametersWindow singleton = null;

    private Container panel;
    private HashMap<Parameters.Type, JTextField> fields;

    private ParametersSavedActionListener listener;

    public interface ParametersSavedActionListener {

        void onParametersSave();

    }

    private ParametersWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLocation(0, 0);
        panel.setLayout(new SpringLayout());
        setLayout(new SpringLayout());
        setTitle("Parameters");

        HashMap<Parameters.Type, String> map = Parameters.getListParameters();
        List<Parameters.Type> list = Parameters.Type.orderedList();
        fields = new HashMap<>();
        for (Parameters.Type tp : list) {
            String valor = map.get(tp);
            if (valor == null) {
                valor = "0";
            }
            addLabel(tp.getName());
            JTextField fd = new JTextField(valor);
            fd.setHorizontalAlignment(SwingConstants.RIGHT);
            fd.setSize(100, AdsUtils.FIELD_HEIGHT);
            panel.add(fd);
            fields.put(tp, fd);
        }

        JButton btn = new JButton("Cancel");
        btn.addActionListener((ActionEvent e) -> actionCancelClick());
        panel.add(btn);

        btn = new JButton("Save");
        btn.addActionListener((ActionEvent e) -> actionSaveClick());
        panel.add(btn);

        SpringUtilities.makeCompactGrid(panel, //parent
                map.size() + 1, 2, // rows, cols
                5, 5, //initX, initY
                10, 10); //xPad, yPad

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                singleton = null;
            }
        });

        setContentPane(panel);
        pack();
    }

    private void addLabel(String text) {
        if (!text.endsWith(":")) {
            text += ":";
        }
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl);
    }

    public static void createAndShow(ParametersSavedActionListener listener) {
        singleton = new ParametersWindow();
        AdsUtils.centerWindow(singleton);
        singleton.listener = listener;
        singleton.setVisible(true);
    }

    private void actionSaveClick() {
        try {
            HashMap<Parameters.Type, String> values = new HashMap<>();

            for (Parameters.Type tp : fields.keySet()) {
                values.put(tp, fields.get(tp).getText());
            }

            if (!Parameters.putListParameters(values)) {
                JOptionPane.showMessageDialog(null, "Invalid Data.");
                return;
            }

            if (listener != null) {
                listener.onParametersSave();
            }

            AdsUtils.closeWindow(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid Data.");
        }
    }

    private void actionCancelClick() {
        AdsUtils.closeWindow(this);
    }

}
