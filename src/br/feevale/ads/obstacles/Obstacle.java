package br.feevale.ads.obstacles;

import br.feevale.ads.car.Car;
import java.awt.Graphics;
import java.awt.Point;

import br.feevale.ads.street.Street;

public abstract class Obstacle {

    private Street rua;
    private int via;

    private int distancia = 0;

    public Obstacle(Street rua, int via, int distancia) {
        this.distancia = distancia;

        setRua(rua, via);
    }

    public abstract void setCentro(Point centro);

    public abstract void drawPath(Graphics g);

    public Street getRua() {
        return rua;
    }

    public void setRua(Street rua, int via) {
        this.rua = rua;
        this.via = via;
        setCentro(rua.pontoForVia(via, distancia));
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

    public double calcularMaxVelocidade(Car carro, double velocidadeAtual) {
        return 0;
    }

}
