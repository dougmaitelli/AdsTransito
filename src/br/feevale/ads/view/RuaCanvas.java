package br.feevale.ads.view;

import br.feevale.ads.model.Rua;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends Canvas {

    private ArrayList<Rua> ruas;

    public void loadRuas(File arquivo) {
        ruas = new ArrayList<Rua>();
        ruas.add(Rua.createRuaFor(50, 50, 250, 70, 3, 2));
    }

    public RuaCanvas() {
        // canvas
        loadRuas(null);
    }

    public void paint(Graphics g) {
        Rua ruaAnterior = null;
        Rua ruaAtual = null;
        Graphics2D g2d = (Graphics2D) g;
        for (Rua rua : ruas) {
            rua.drawPath(g2d);
        }
    }

}
