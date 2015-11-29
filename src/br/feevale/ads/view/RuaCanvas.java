package br.feevale.ads.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import br.feevale.ads.obstacles.Blockage;
import br.feevale.ads.obstacles.Reduction;
import br.feevale.ads.street.Street;
import br.feevale.ads.street.StraightStreet;
import br.feevale.ads.utils.DoubleBuffer;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends DoubleBuffer {

    public static ArrayList<StraightStreet> ruas;

    private void addRua(Street rua) {
        RuaWindow.rua = rua;
        if (ruas == null) {
            ruas = new ArrayList<StraightStreet>();
        }
        if (rua instanceof StraightStreet) {
            ruas.add((StraightStreet) rua);
        }
    }

    public Street ruaForPoint(Point ponto) {
        for (Street rua : ruas) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        return null;
    }

    public void loadRuas(File arquivo) {
        ruas = new ArrayList<StraightStreet>();
        Street r = StraightStreet.createRuaFor(50, 350, 1100, 350, 3, "Pereira");
        addRua(r);
        for (Street rua : ruas) {
            rua.pack();
            rua.addObstaculo(new Reduction(rua, 0, 500));
            rua.addObstaculo(new Blockage(rua, 1, 700));
            rua.addObstaculo(new Reduction(rua, 2, 300));
        }
        invalidate();
        repaint();
    }

    public RuaCanvas() {
        // canvas
        loadRuas(null);
    }

    public void paintBuffer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // pinta de branco
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);
        // carro
        for (Street rua : ruas) {
            rua.drawPath(g2d);
        }
    }

}
