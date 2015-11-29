package br.feevale.ads.obstacles;

import br.feevale.ads.Parametros;
import br.feevale.ads.car.Car;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.feevale.ads.street.Street;

public class Reduction extends Obstacle {

    private static final int WIDTH = Street.RUA_WIDTH;
    private static final int DEEP = (int) (Street.RUA_WIDTH * 0.5);

    public static final int TIPO_REDUTOR = 1;
    public static final int TIPO_BURACO = 2;

    private Point centro;
    private Rectangle rect;
    private Color color = Color.BLUE;
    private int tipo = TIPO_REDUTOR; // quebra mola

    public Reduction(Street rua, int via, int distancia, int tipo) {
        super(rua, via, distancia);
        this.tipo = tipo;
    }

    public void setCentro(Point centro) {
        int x = centro.x - (DEEP / 2);
        int y = centro.y - (WIDTH / 2);
        this.centro = new Point(x, y);
        this.rect = new Rectangle(x, y, DEEP, WIDTH);
    }

    @Override
    public double calcularMaxVelocidade(Car carro, double velocidadeAtual) {
        switch (tipo) {
            case TIPO_REDUTOR:
                if (velocidadeAtual < Parametros.velocidadeMaximaRedutor) {
                    return velocidadeAtual;
                }
                return Parametros.velocidadeMaximaRedutor;

            case TIPO_BURACO: {
                double vel = velocidadeAtual * 0.70;
                if (vel < (Parametros.velocidadeMaximaRedutor * 2)) {
                    return (Parametros.velocidadeMaximaRedutor * 2);
                }
                return vel;
            }
        }
        return velocidadeAtual;
    }

    public void drawPath(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (tipo == TIPO_REDUTOR) {
            g2d.setColor(Color.BLUE);
        } else {
            g2d.setColor(Color.MAGENTA);
        }
        g2d.draw(rect);
        g2d.fill(rect);
    }

}
