/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.street;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import br.feevale.ads.car.Car;
import br.feevale.ads.obstacles.Obstacle;
import br.feevale.ads.utils.ListCars;

/**
 *
 * @author 0066115
 */
public abstract class Street {

    public static final int RUA_WIDTH = 12;
    public static final int LINHA_WIDTH = 2;

    public static final Color COR_RUA = Color.DARK_GRAY;
    public static final Color COR_LINHA_BRANCA = Color.WHITE;
    public static final Color COR_LINHA_AMARELA = Color.YELLOW;
    public static final Color COR_LINHA_VIA_IDA = Color.GREEN;
    public static final Color COR_LINHA_VIA_VOLTA = Color.RED;

    public static final BasicStroke LINHA_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);
    public static final BasicStroke CAMINHO_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public static boolean DRAW_VIA = false;
    public static GeneralPath path = new GeneralPath();

    protected String nome;

    protected Point ponto1;
    protected Point ponto2;

    public int carrosConcluidos = 0;

    protected int vias;

    protected double metrosPorPixel;

    protected ListCars carros = new ListCars();
    protected List<Obstacle> obstrucoes = new ArrayList<>();

    public Street(double metrosPorPixel) {
        this.metrosPorPixel = metrosPorPixel;
    }

    public abstract void pack();

    public abstract void drawPath(Graphics2D g2d);

    public abstract boolean contains(Point p);

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVias() {
        return vias;
    }

    public double getMetrosPorPixel() {
        return metrosPorPixel;
    }

    public abstract Point pontoForVia(int via, float distancia);

    public abstract void addCarro(int via, Car carro);

    public abstract void removeCarro(Car carro);

    public ListCars getCarrosProximos(Car carro, double distanciaMetros) {
        return carros.getCarrosProximos(carro, distanciaMetros / metrosPorPixel);
    }

    public Obstacle getObstaculosAhead(Car carro, double distanciaMetros) {
        int via = carro.getVia();
        int distanciaPercorrida = carro.getDistancia();

        Obstacle obstaculoProximo = null;

        for (Obstacle o : obstrucoes) {
            if (via != o.getVia() || distanciaPercorrida > o.getDistancia()) {
                continue;
            }

            if (obstaculoProximo == null && o.getDistancia() - distanciaPercorrida <= distanciaMetros / metrosPorPixel || obstaculoProximo != null && o.getDistancia() < obstaculoProximo.getDistancia()) {
                obstaculoProximo = o;
            }
        }

        return obstaculoProximo;
    }

    public void addObstaculo(Obstacle obstaculo) {
        obstrucoes.add(obstaculo);
    }

    public ListCars getCarros() {
        return carros;
    }
    
    public void clear() {
        
    }

    public abstract void processarCiclos(double ciclos);

}
