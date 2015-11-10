/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author 0066115
 */
public class RuaConexao extends Rua {

    private Ellipse2D.Float elipse;
    private Point ponto;
    private int vias;
    
    public RuaConexao(int distanciaTotal) {
		super(distanciaTotal);
	}

//    public static RuaConexao createInPoint(Point p, int vias) {
//        return new RuaConexao(p, vias);
//    }
//
//    public RuaConexao(Point ponto, int vias) {
//        this.ponto = ponto;
//        this.vias = vias;
//    }

    @Override
    public void pack() {
//        float r = RUA_WIDTH;
//        r = r / 2;
//        elipse = new Ellipse2D.Float(ponto.x, ponto.y, r, r);
    }

    @Override
    public void drawPath(Graphics2D g2d) {
//        g2d.setColor(COR_RUA);
//        int r = RUA_WIDTH * vias;
//        int x = ponto.x - (r / 2);
//        int y = ponto.y - (r / 2);
//        g2d.fillOval(x, y, r, r);
    }

    @Override
    public boolean contains(Point p) {
        return elipse.contains(p);
    }

    @Override
    public Point pontoForVia(int via, float distancia) {
        return null;
    }

    @Override
    public void addCarro(int via, Carro carro) {
        // nao faz nada
    }

    @Override
    public void removeCarro(Carro carro) {
        // nao faz nada
    }

    @Override
    public void processarCiclos(double ciclos) {
        // nao faz nada
    }

}
