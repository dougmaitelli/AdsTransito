package br.feevale.ads.obstacles;

import java.awt.Graphics;
import java.awt.Point;

import br.feevale.ads.rua.Rua;

public abstract class Obstaculo {
	
    private Rua rua;
    private int via;
    
    private int distancia = 0;
    
    public Obstaculo(Rua rua, int via, int distancia) {
    	this.distancia = distancia;
    	
    	setRua(rua, via);
	}
    
    public abstract void setCentro(Point centro);

    public abstract void drawPath(Graphics g);
    
    public Rua getRua() {
		return rua;
	}

    public void setRua(Rua rua, int via) {
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
    
}
