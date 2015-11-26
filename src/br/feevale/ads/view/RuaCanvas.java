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
import br.feevale.ads.street.StreetConnection;
import br.feevale.ads.view.utils.DoubleBuffer;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends DoubleBuffer {

    public static ArrayList<Street> ruas;
    public static ArrayList<StreetConnection> conexoes;
    
    private void addRua(Street rua) {
        if (conexoes == null) {
            conexoes = new ArrayList<StreetConnection>();
        }
        if (ruas == null) {
            ruas = new ArrayList<Street>();
        }
        if (rua instanceof StreetConnection) {
            conexoes.add((StreetConnection) rua);
        }
        if (rua instanceof Street) {
            ruas.add((Street) rua);
        }
    }
    
    public Street ruaForPoint(Point ponto) {
        for (Street rua : conexoes) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        for (Street rua : ruas) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        return null;
    }

    public void loadRuas(File arquivo) {
        conexoes = new ArrayList<StreetConnection>();
        ruas = new ArrayList<Street>();
        Street r = Street.createRuaFor(50, 350, 1100, 350, 3, "Pereira");
        addRua(r);
//        addRua(RuaConexao.createInPoint(new Point(800, 450), 3));
//        addRua(RuaReta.createRuaFor(50, 150, 800, 150, 4, "Pereira 2"));
//        addRua(RuaConexao.createInPoint(new Point(800, 150), 4));
//        addRua(RuaReta.createRuaFor(800, 50, 800, 600, 4, "Pereira 3"));
        for (Street rua : ruas) {
            rua.pack();
            rua.addObstaculo(new Reduction(rua, 0, 500));
            rua.addObstaculo(new Blockage(rua, 1, 700));
            rua.addObstaculo(new Reduction(rua, 2, 300));
        }
        for (Street rua : conexoes) {
            rua.pack();
        }
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
        for (Street rua : conexoes) {
            rua.drawPath(g2d);
        }
    }

}
