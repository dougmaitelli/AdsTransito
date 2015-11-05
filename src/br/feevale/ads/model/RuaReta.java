package br.feevale.ads.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class RuaReta extends Rua {

    // sem getter para desempenho
    public Point ponto1;
    public Point ponto2;
    public int vias;
    public Polygon forma;
    public String nome;

    public ArrayList<Point> listViaPonto1 = new ArrayList<Point>();
    public ArrayList<Point> listViaPonto2 = new ArrayList<Point>();
    
    private static ArrayList<CarroTemporario> carros = new ArrayList<CarroTemporario>();

    // calculos
    public double angle;
    private Point lastPoint;

    public static RuaReta createRuaFor(int x1, int y1, int x2, int y2, int vias, String nome) {
        RuaReta r = new RuaReta();
        r.ponto1 = new Point(x1, y1);
        r.ponto2 = new Point(x2, y2);
        r.vias = vias;
        if (nome == null) {
            nome = "Rua sem nome";
        }
        r.nome = nome;
//        int ruaW2 = RUA_WIDTH / 2;
        // cria retangulo
//        int x = Math.min(x1 - ruaW2, x2 - ruaW2);
//        int y = Math.min(y1, y2);
//        int w = Math.max(x1, x2) - x;
//        int h = Math.max(y1, y2) - y;
        r.forma = new Polygon(new int[]{x1, x2, x1, x2}, new int[]{y1, y1, y2, y2}, 4);
        return r;
    }

    @Override
    public boolean contains(Point p) {
        return forma.contains(p);
    }

    private void calcAngle() {
        double eq_a;
        angle = 0f;
        if (ponto1.x == ponto2.x) {
            eq_a = 0;
            angle = 0;
        } else {
            double y = ponto2.y - ponto1.y;
            double x = ponto2.x - ponto1.x;
            eq_a = y / x;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        }
    }

    public void pack() {
        if (vias == 0) {
            throw new RuntimeException("sem vias cadastradas");
        }
        calcAngle();
        double raio = vias * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        for (int i = 1; i <= vias; i++) {
            // linha branca ida
            tmp = RUA_WIDTH;
            tmp = tmp / 2;
            tmp = ((vias - i) * RUA_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, ponto1);
            Point p2 = pointForRaio(tmp, ponto2);
            listViaPonto1.add(p1);
            listViaPonto2.add(p2);
        }
        /*
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
         */
        /*
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
         */
    }

    public void drawPath(Graphics2D g2d) {
        g2d.setColor(COR_RUA);
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
        for (int i = 0; i <= vias; i++) {
            // linha branca ida
            if ((i == 0) || (i == vias)) {
                g2d.setColor(COR_LINHA_AMARELA);
            } else {
                g2d.setColor(COR_LINHA_BRANCA);
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
            for (int i = 0; i < vias; i++) {
                path.reset();
                Point p = listViaPonto1.get(i);
                path.moveTo(p.x, p.y);
                p = listViaPonto2.get(i);
                path.lineTo(p.x, p.y);
                g2d.draw(path);
                break;
            }
        }
        // desenha carros
        for (CarroTemporario carro : carros) {
            carro.drawPath(g2d);
        }
    }
    
    public Point pontoForVia(int via, float distancia) {
        Point p = listViaPonto1.get(via);
        lastPoint = new Point();
        lastPoint.x = p.x + (int) (distancia * Math.sin(angle));
        lastPoint.y = p.y + (int) (distancia * Math.cos(angle));
        return lastPoint;
    }

    private Point pointForRaio(double r, Point origem) {
        lastPoint = new Point();
        lastPoint.x = origem.x + (int) (Math.cos(angle) * r);
        lastPoint.y = origem.y + (int) (Math.sin(angle) * r);
        return lastPoint;
    }

    @Override
    public void addCarro(int via, CarroTemporario carro) {
        Point p = listViaPonto1.get(via);
        carro.setCentro(p);
        carros.add(carro);
        carro.setRua(this, via);
    }

    @Override
    public void removeCarro(CarroTemporario carro) {
        carros.remove(carro);
    }
    
    public void processarCiclos(int ciclos) {
        for (CarroTemporario carro : carros) {
            carro.moverCiclo(ciclos);
        }
    }

}