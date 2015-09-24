package br.feevale.ads.model;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class Rua {
    
    // sem getter para desempenho
    public Point ponto1;
    public Point ponto2;
    
    public int viasIda;
    public int viasVolta;
    
    public ArrayList<Rua> conectaPonto1;
    public ArrayList<Rua> conectaPonto2;

    public Rua() {
        // nao faz nada
    }
    
    public static Rua createRuaFor(int x1, int y1, int x2, int y2, int viasIda, int viasVolta) {
        Rua r = new Rua();
        r.ponto1 = new Point(x1, y1);
        r.ponto2 = new Point(x2, y2);
        r.viasIda = viasIda;
        r.viasVolta = viasVolta;
        return r;
    }
    
    public GeneralPath drawLine() {
        
    }
    
}

