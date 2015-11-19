package br.feevale.ads.carro;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.feevale.ads.obstacles.Obstaculo;
import br.feevale.ads.rua.Rua;
import br.feevale.ads.utils.ListCarros;

public abstract class Carro {
	
	private static final int PADDING = 4;
    private static final int WIDTH = Rua.RUA_WIDTH - PADDING;
    private static final int DEEP = (int) (Rua.RUA_WIDTH * 1.5);
    
    protected double GRAU_DE_ATENCAO = 3;
    protected double DISTANCIA_DE_ATENCAO = 3; // metros
    protected double VELOCIDADE_MAXIMA = 10; // metros por segundo

    private Point centro;
    private Rectangle rect;
    private float angle = 0;
    private Color color = Color.GREEN;
    
    private Rua rua;
    private int via;
    
    private int distancia = 0;
    private double velocidade = 0;
    private boolean concluiuPercurso = false;
    
    private ListCarros carrosProximos;

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
    
    public Rua getRua() {
		return rua;
	}

    public void setRua(Rua rua, int via) {
        if (this.rua != null) {
            rua.removeCarro(this);
        }
        this.rua = rua;
        this.via = via;
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
    	
    	DISTANCIA_DE_ATENCAO = Math.max(this.velocidade / 7d * GRAU_DE_ATENCAO, 3d);
	}
    
    public boolean isConcluiuPercurso() {
    	return concluiuPercurso;
    }
    
    public void setConcluiuPercurso(boolean concluiuPercurso) {
		this.concluiuPercurso = concluiuPercurso;
	}
    
    public ListCarros getCarrosProximos() {
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
        	Obstaculo obstaculo = rua.getObstaculosAhead(this, DISTANCIA_DE_ATENCAO);
        	
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
    
    public abstract void reactionCarroAhead();
    public abstract void reactionObstaculoAhead(Obstaculo obstaculo);
    public abstract void reactionCarroBehind();
    public abstract void reactionCaminhoLivre();
    
}
