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

    private static final int RUA_WIDTH = 20;
    private static final int LINHA_WIDTH = 2;
    private static final Color COR_RUA = Color.DARK_GRAY;
    private static final Color COR_LINHA_BRANCA = Color.WHITE;
    private static final Color COR_LINHA_AMARELA = Color.YELLOW;
    private static final Color COR_LINHA_CAMINHO = Color.GREEN;
    private static final BasicStroke LINHA_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);
    private static final BasicStroke CAMINHO_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public static GeneralPath path = new GeneralPath();

    // sem getter para desempenho
    public Point ponto1;
    public Point ponto2;
    public int viasIda;
    public int viasVolta;
    public ArrayList<Via> listViasIda = new ArrayList<Via>();
    public ArrayList<Via> listViasVolta = new ArrayList<Via>();
    private double angle;
    private Point lastPoint;

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

    private void calcAngle() {
        double eq_a;
        angle = 0f;
        if (ponto1.x == ponto2.x) {
            eq_a = 0;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        } else {
            double y = ponto2.y - ponto1.y;
            double x = ponto2.x - ponto1.x;
            eq_a = y / x;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        }
    }

    public void pack() {
        if (viasIda == 0 && viasVolta == 0) {
            throw new RuntimeException("sem vias cadastradas");
        }
        calcAngle();
        double raio = (viasIda + viasVolta) * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        for (int i = 0; i < viasIda; i++) {
            // linha branca ida
            tmp = RUA_WIDTH;
            tmp = tmp / 2;
            tmp = (i * RUA_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, ponto1);
            Point p2 = pointForRaio(tmp, ponto2);
            listViasIda.add(Via.getNewInstance(p1, p2));
        }
//        for (int i = 1; i <= viasVolta; i++) {
//            // linha branca ida
//            tmp = ((i + viasIda) * RUA_WIDTH);
//            tmp = raio - tmp;
//            Point p1 = pointForRaio(tmp, ponto1);
//            Point p2 = pointForRaio(tmp, ponto2);
//            listViasVolta.add(Via.getNewInstance(p1, p2));
//        }
    }

    public void drawPath(Graphics2D g2d) {
        g2d.setColor(COR_RUA);
        int vias = (viasIda + viasVolta);
        g2d.setStroke(new BasicStroke(vias * RUA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        path.reset();
        path.moveTo(ponto1.x, ponto1.y);
        path.lineTo(ponto2.x, ponto2.y);
        g2d.draw(path);
        // linhas na pista
        // desenha as linhas
        double raio = vias * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        g2d.setStroke(LINHA_STROKE);
        for (int i = 1; i < vias; i++) {
            // linha branca ida
            if (i < viasVolta) {
                g2d.setColor(COR_LINHA_BRANCA);
            } else {
                // linha amarela
                if (i == viasVolta) {
                    g2d.setColor(COR_LINHA_AMARELA);
                } else {
                    g2d.setColor(COR_LINHA_BRANCA);
                }
            }
            tmp = (i * RUA_WIDTH);
            tmp = raio - tmp;
            path.reset();
            pointForRaio(tmp, ponto1);
            path.moveTo(lastPoint.x, lastPoint.y);
            pointForRaio(tmp, ponto2);
            path.lineTo(lastPoint.x, lastPoint.y);
            g2d.draw(path);
        }
        // desenha o caminho
        g2d.setColor(COR_LINHA_CAMINHO);
        g2d.setStroke(CAMINHO_STROKE);
        for (Via v : listViasIda) {
            path.reset();
            path.moveTo(v.ponto1.x, v.ponto1.y);
            path.lineTo(v.ponto2.x, v.ponto2.y);
            g2d.draw(path);
        }
    }

    private Point pointForRaio(double r, Point origem) {
        lastPoint = new Point();
        lastPoint.x = origem.x + (int) (Math.cos(angle) * r);
        lastPoint.y = origem.y + (int) (Math.sin(angle) * r);
        return lastPoint;
    }

}
