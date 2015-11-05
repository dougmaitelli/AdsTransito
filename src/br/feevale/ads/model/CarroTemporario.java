/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author 0066115
 */
public class CarroTemporario {

    public Point centro;
    public Rectangle rect;
    public float angle = 0;
    public Color color = Color.RED;
    
    private Rua rua;

    private static final int PADDING = 4;
    private static final int WIDTH = Rua.RUA_WIDTH - PADDING;
    private static final int DEEP = (int) (Rua.RUA_WIDTH * 1.5);

    private int distancia = 0;
    private int via;

    public CarroTemporario() {
        // nao faz nada
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

    public void setRua(Rua rua, int via) {
        if (this.rua != null) {
            rua.removeCarro(this);
        }
        this.rua = rua;
        this.via = via;
    }

    public void moverCiclo(int ciclos) {
        if (rua == null) {
            return;
        }
        distancia += 3;
        setCentro(rua.pontoForVia(via, distancia));
    }

}
