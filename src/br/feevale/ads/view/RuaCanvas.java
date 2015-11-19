package br.feevale.ads.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import br.feevale.ads.obstacles.Bloqueio;
import br.feevale.ads.obstacles.Reducao;
import br.feevale.ads.rua.Rua;
import br.feevale.ads.rua.RuaConexao;
import br.feevale.ads.rua.RuaReta;
import br.feevale.ads.view.utils.DoubleBuffer;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends DoubleBuffer {

    public static ArrayList<RuaReta> ruas;
    public static ArrayList<RuaConexao> conexoes;
    
    private void addRua(Rua rua) {
        if (conexoes == null) {
            conexoes = new ArrayList<RuaConexao>();
        }
        if (ruas == null) {
            ruas = new ArrayList<RuaReta>();
        }
        if (rua instanceof RuaConexao) {
            conexoes.add((RuaConexao) rua);
        }
        if (rua instanceof RuaReta) {
            ruas.add((RuaReta) rua);
        }
    }
    
    public Rua ruaForPoint(Point ponto) {
        for (Rua rua : conexoes) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        for (Rua rua : ruas) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        return null;
    }

    public void loadRuas(File arquivo) {
        conexoes = new ArrayList<RuaConexao>();
        ruas = new ArrayList<RuaReta>();
        Rua r = RuaReta.createRuaFor(50, 350, 1100, 350, 3, "Pereira");
        addRua(r);
//        addRua(RuaConexao.createInPoint(new Point(800, 450), 3));
//        addRua(RuaReta.createRuaFor(50, 150, 800, 150, 4, "Pereira 2"));
//        addRua(RuaConexao.createInPoint(new Point(800, 150), 4));
//        addRua(RuaReta.createRuaFor(800, 50, 800, 600, 4, "Pereira 3"));
        for (Rua rua : ruas) {
            rua.pack();
            rua.addObstaculo(new Reducao(rua, 0, 500));
            rua.addObstaculo(new Bloqueio(rua, 1, 700));
            rua.addObstaculo(new Reducao(rua, 2, 300));
        }
        for (Rua rua : conexoes) {
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
        for (Rua rua : ruas) {
            rua.drawPath(g2d);
        }
        for (Rua rua : conexoes) {
            rua.drawPath(g2d);
        }
    }

}
