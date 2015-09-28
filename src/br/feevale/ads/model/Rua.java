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

    private static final int RUA_WIDTH = 12;
    private static final int LINHA_WIDTH = 2;
    private static final Color COR_RUA = Color.DARK_GRAY;
    private static final Color COR_LINHA_BRANCA = Color.WHITE;
    private static final Color COR_LINHA_AMARELA = Color.YELLOW;
    private static final Color COR_LINHA_VIA_IDA = Color.GREEN;
    private static final Color COR_LINHA_VIA_VOLTA = Color.RED;
    private static final BasicStroke LINHA_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);
    private static final BasicStroke CAMINHO_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public static boolean DRAW_VIA = false;

    public static GeneralPath path = new GeneralPath();

    // sem getter para desempenho
    public Point ponto1;
    public Point ponto2;
    public int viasIda;
    public int viasVolta;
    public String nome;
    public ArrayList<Via> listViasIda = new ArrayList<Via>();
    public ArrayList<Via> listViasVolta = new ArrayList<Via>();
    private double angle;
    private Point lastPoint;

    public Rua() {
        // nao faz nada
    }

    public static Rua createRuaFor(int x1, int y1, int x2, int y2, int viasIda, int viasVolta, String nome) {
        Rua r = new Rua();
        r.ponto1 = new Point(x1, y1);
        r.ponto2 = new Point(x2, y2);
        r.viasIda = viasIda;
        r.viasVolta = viasVolta;
        if (nome == null) {
            nome = "Rua sem nome";
        }
        r.nome = nome;
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
        int vias = viasIda + viasVolta;
        double raio = vias * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        int l = 0;
        for (int i = viasIda; i >= 1; i--) {
            // linha branca ida
            tmp = RUA_WIDTH;
            tmp = tmp / 2;
            tmp = ((vias - i) * RUA_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, ponto1);
            Point p2 = pointForRaio(tmp, ponto2);
            Via via = Via.getNewInstance(p1, p2, nome + " Ida #" + l);
            listViasIda.add(via);
            // se tem mais que uma
            if (listViasIda.size() > 1) {
                for (Via viaAdd : listViasIda) {
                    if (viaAdd != via) {
                        viaAdd.conectaViasParalelasIda.add(via);
                        via.conectaViasParalelasIda.add(viaAdd);
                    }
                }
            }
            l++;
        }
        l = 0;
        for (int i = (viasVolta - 1); i >= 0; i--) {
            // linha branca ida
            tmp = RUA_WIDTH;
            tmp = tmp / 2;
            tmp = (i * RUA_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, ponto1);
            Point p2 = pointForRaio(tmp, ponto2);
            Via via = Via.getNewInstance(p1, p2, nome + " Volta #" + l);
            listViasVolta.add(via);
            // se tem listViasVolta que uma
            if (listViasVolta.size() > 1) {
                for (Via viaAdd : listViasVolta) {
                    if (viaAdd != via) {
                        viaAdd.conectaViasParalelasVolta.add(via);
                        via.conectaViasParalelasVolta.add(viaAdd);
                    }
                }
            }
            l++;
        }
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
        if (DRAW_VIA) {
            // desenha o caminho
            g2d.setColor(COR_LINHA_VIA_IDA);
            g2d.setStroke(CAMINHO_STROKE);
            for (Via v : listViasIda) {
                path.reset();
                path.moveTo(v.ponto1.x, v.ponto1.y);
                path.lineTo(v.ponto2.x, v.ponto2.y);
                g2d.draw(path);
            }
            g2d.setColor(COR_LINHA_VIA_VOLTA);
            for (Via v : listViasVolta) {
                path.reset();
                path.moveTo(v.ponto1.x, v.ponto1.y);
                path.lineTo(v.ponto2.x, v.ponto2.y);
                g2d.draw(path);
            }
        }
    }

    private Point pointForRaio(double r, Point origem) {
        lastPoint = new Point();
        lastPoint.x = origem.x + (int) (Math.cos(angle) * r);
        lastPoint.y = origem.y + (int) (Math.sin(angle) * r);
        return lastPoint;
    }

    public void debug() {
        System.out.println("===== Rua =====");
        System.out.println(nome);
        for (Via via : listViasIda) {
            System.out.println("--- via.nome = " + via.nome);
            for (Via vx : via.conectaViasParalelasIda) {
                System.out.println("paralela a " + vx.nome);
            }
            for (Via vx : via.conectaViasPonto1) {
                System.out.println("conecta na vinda com " + vx.nome);
            }
            for (Via vx : via.conectaViasPonto2) {
                System.out.println("conecta na saida com " + vx.nome);
            }
        }
        for (Via via : listViasVolta) {
            System.out.println("--- via.nome = " + via.nome);
            for (Via vx : via.conectaViasParalelasVolta) {
                System.out.println("paralela a " + vx.nome);
            }
            for (Via vx : via.conectaViasPonto2) {
                System.out.println("conecta na vinda com " + vx.nome);
            }
            for (Via vx : via.conectaViasPonto1) {
                System.out.println("conecta na saida com " + vx.nome);
            }
        }
        System.out.println("==============");
        System.out.println();
        System.out.println();
    }

}
