/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;

import br.feevale.ads.utils.ListCarros;

/**
 *
 * @author 0066115
 */
public abstract class Rua {
    
    public static final int RUA_WIDTH = 12;
    public static final int LINHA_WIDTH = 2;
    
    public static final Color COR_RUA = Color.DARK_GRAY;
    public static final Color COR_LINHA_BRANCA = Color.WHITE;
    public static final Color COR_LINHA_AMARELA = Color.YELLOW;
    public static final Color COR_LINHA_VIA_IDA = Color.GREEN;
    public static final Color COR_LINHA_VIA_VOLTA = Color.RED;
    
    public static final BasicStroke LINHA_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);
    public static final BasicStroke CAMINHO_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public static boolean DRAW_VIA = true;
    public static GeneralPath path = new GeneralPath();
    
    protected ListCarros carros = new ListCarros();
    
    public abstract void pack();
    public abstract void drawPath(Graphics2D g2d);
    public abstract boolean contains(Point p);
    
    public abstract Point pontoForVia(int via, float distancia);
    public abstract void addCarro(int via, Carro carro);
    public abstract void removeCarro(Carro carro);
    
    public ListCarros getCarrosProximos(Carro carro, int distancia) {
    	return carros.getCarrosProximos(carro, distancia);
    }
    
    public abstract void processarCiclos(int ciclos);
    
}
