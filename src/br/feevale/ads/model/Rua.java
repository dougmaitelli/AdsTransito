package br.feevale.ads.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class Rua {

    public static GeneralPath path = new GeneralPath();
    public static final Color COR = Color.RED;

    public static final int RUA_WIDTH = 10;
    public static final int LINHA_WIDTH = 2;

    // sem getter para desempenho
    public Point ponto1;
    public Point ponto2;

    public int viasIda;
    public int viasVolta;

    public ArrayList<Rua> conectaPonto1 = new ArrayList<Rua>();
    public ArrayList<Rua> conectaPonto2 = new ArrayList<Rua>();

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

    public void drawPath(Graphics2D g2d) {
        double eq_a;
        g2d.setColor(COR);
        g2d.setStroke(new BasicStroke((viasIda + viasVolta) * RUA_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        path.reset();
        path.moveTo(ponto1.x, ponto1.y);
        path.lineTo(ponto2.x, ponto2.y);
        g2d.draw(path);
        double angle = 0f;
        if (ponto1.x == ponto2.x) {
            eq_a = 0;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        } else {
            double y = -(ponto2.y - ponto1.y);
            double x = ponto2.x - ponto1.x;
            eq_a = y / x;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        }
        // strokes
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{2.5f}, 0f));
        g2d.setColor(Color.YELLOW);
        path.reset();
        Point p = pointForAngle(angle, -5, ponto1);
        path.moveTo(p.x, p.y);
        p = pointForAngle(angle, -5, ponto2);
        path.lineTo(p.x, p.y);
        g2d.draw(path);
        // desenha linha da rua
        for (Rua ponto : conectaPonto1) {
            ponto.drawPath(g2d);
        }
        for (Rua ponto : conectaPonto2) {
            ponto.drawPath(g2d);
        }
    }

    private static Point pointForAngle(double angle, int r, Point origem) {
        Point p = new Point();
        p.x = origem.x + (int) (Math.cos(angle) * r);
        p.y = origem.y + (int) (Math.sin(angle) * r);
        return p;
    }

}
