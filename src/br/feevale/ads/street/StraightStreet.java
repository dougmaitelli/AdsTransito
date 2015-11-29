package br.feevale.ads.street;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import br.feevale.ads.Parametros;
import br.feevale.ads.car.Car;
import br.feevale.ads.car.NormalCar;
import br.feevale.ads.obstacles.Obstacle;

/**
 *
 * @author 0066115
 */
public class StraightStreet extends Street {

    public Polygon forma;

    public ArrayList<Point> listViaPonto1 = new ArrayList<Point>();
    public ArrayList<Point> listViaPonto2 = new ArrayList<Point>();

    // calculos
    public double angle;
    private Point lastPoint;

    public StraightStreet(double distanciaTotal) {
        super(distanciaTotal);
    }

    public static StraightStreet createRuaFor(int x1, int y1, int x2, int y2, int vias, String nome) {
        StraightStreet r = new StraightStreet(0.1); //metros por pixel
        r.ponto1 = new Point(x1, y1);
        r.ponto2 = new Point(x2, y2);
        r.vias = vias;
        if (nome == null) {
            nome = "Rua sem nome";
        }
        r.nome = nome;
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
        for (Obstacle obstrucao : obstrucoes) {
            obstrucao.drawPath(g2d);
        }
        // desenha carros
        for (int i = 0; i < carros.size(); i++) {
            Car carro = null;
            try {
                carro = carros.get(i);
            } catch (Exception ex) {
                continue;
            }
            if (carro != null) {
                if (carro.isConcluiuPercurso()) {
                    continue;
                }
                carro.drawPath(g2d);
            }
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
    public void addCarro(int via, Car carro) {
        Point p = listViaPonto1.get(via);
        carro.setCentro(p);
        carros.add(carro);
        carro.setRua(this, via);
    }

    @Override
    public void removeCarro(Car carro) {
        carros.remove(carro);
    }

    public void processarCiclos(double ciclos) {
        List<Integer> viasLivres = new ArrayList<>();

        for (int i = 0; i < vias; i++) {
            viasLivres.add(i);
        }

        for (Car carro : carros) {
            carro.moverCiclo(ciclos);

            if (carro.getDistancia() < (Parametros.velocidadeMaxima * 2.5)) {
                viasLivres.remove((Integer) carro.getVia());
            }
            if (!carro.isConcluiuPercurso()) {
                if (carro.getDistancia() > 1050) {
                    carrosConcluidos++;
                    carro.setConcluiuPercurso(true);
                }
            }
        }

        for (Integer via : viasLivres) {
            if (carros.size() < Parametros.totalVeiculos) {
                NormalCar c = new NormalCar();
                addCarro(via, c);
            }
        }
    }

}
