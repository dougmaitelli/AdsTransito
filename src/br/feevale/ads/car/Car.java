package br.feevale.ads.car;

import br.feevale.ads.Parametros;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.feevale.ads.obstacles.Obstacle;
import br.feevale.ads.street.Street;
import br.feevale.ads.utils.ListCars;

public abstract class Car {

    private static final int PADDING = 4;
    private static final int WIDTH = Street.RUA_WIDTH - PADDING;
    private static final int DEEP = (int) (Street.RUA_WIDTH * 1.5);

    protected double GRAU_DE_ATENCAO = Parametros.grauAtencao;
    protected double DISTANCIA_DE_ATENCAO = Parametros.distanciaAtencao; // metros
    protected double VELOCIDADE_MAXIMA = Parametros.velocidadeMaxima; // metros por segundo

    private Point centro;
    private Rectangle rect;
    private float angle = 0;
    private Color color = Color.GREEN;

    private static int SEQ_ID_GENERATOR = 1;

    private Street rua;
    private int via;
    private int id;

    private int distancia = 0;
    private double velocidade = 0;
    private boolean concluiuPercurso = false;

    public long timeIniciou = 0L;
    public long timeConcluiu = 0L;

    private ListCars carrosProximos;

    public Car() {
        id = SEQ_ID_GENERATOR;
        SEQ_ID_GENERATOR++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCentro(Point centro) {
        int x = centro.x - (DEEP / 2);
        int y = centro.y - (WIDTH / 2);
        this.centro = new Point(x, y);
        this.rect = new Rectangle(x, y, DEEP, WIDTH);
    }

    public void drawPath(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
//        g2d.rotate(angle);
        g2d.draw(rect);
        g2d.fill(rect);
//        g2d.rotate(-angle);
    }

    public Street getRua() {
        return rua;
    }

    public void setRua(Street rua, int via) {
        if (this.rua != null) {
            rua.removeCarro(this);
        }
        this.rua = rua;
        this.via = via;
        timeIniciou = System.currentTimeMillis();
    }

    public int getVia() {
        return via;
    }

    public void setVia(int via) {
        if (via < 0 || via > rua.getVias() - 1) {
            return;
        }

        this.via = via;
    }

    public int getDistancia() {
        return distancia;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void addVelocidade(double velocidade) {
        this.velocidade += velocidade;
        if (this.velocidade < 0) {
            this.velocidade = 0;
        }
        if (this.velocidade > VELOCIDADE_MAXIMA) {
            this.velocidade = VELOCIDADE_MAXIMA;
        }
        DISTANCIA_DE_ATENCAO = Math.max(Parametros.distanciaAtencao, this.velocidade * GRAU_DE_ATENCAO);
    }

    public boolean isConcluiuPercurso() {
        return concluiuPercurso;
    }

    public void setConcluiuPercurso(boolean concluiuPercurso) {
        if (this.concluiuPercurso) {
            return;
        }
        timeConcluiu = System.currentTimeMillis();
        this.concluiuPercurso = concluiuPercurso;
    }

    public ListCars getCarrosProximos() {
        return carrosProximos;
    }

    public void moverCiclo(double ciclos) {
        if (rua == null) {
            return;
        }

        carrosProximos = rua.getCarrosProximos(this, DISTANCIA_DE_ATENCAO);

        if (carrosProximos.getCarrosAhead(this).size() > 0) {
            reactionCarroAhead();
        } else {
            Obstacle obstaculo = rua.getObstaculosAhead(this, DISTANCIA_DE_ATENCAO);

            if (obstaculo != null) {
                reactionObstaculoAhead(obstaculo);
            } else {
                reactionCaminhoLivre();
            }
        }

        if (carrosProximos.getCarrosBehind(this).size() > 0) {
            reactionCarroBehind();
        }

        distancia += (ciclos * velocidade) / rua.getMetrosPorPixel();

        setCentro(rua.pontoForVia(via, distancia));
    }

    public long getTimeIniciou() {
        return timeIniciou;
    }

    public long getTimeConcluiu() {
        return timeConcluiu;
    }

    public abstract void reactionCarroAhead();

    public abstract void reactionObstaculoAhead(Obstacle obstaculo);

    public abstract void reactionCarroBehind();

    public abstract void reactionCaminhoLivre();

}
