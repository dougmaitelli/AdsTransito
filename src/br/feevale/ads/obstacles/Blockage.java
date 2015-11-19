package br.feevale.ads.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.feevale.ads.street.Street;

public class Blockage extends Obstacle {
	
    private static final int WIDTH = Street.RUA_WIDTH;
    private static final int DEEP = (int) (Street.RUA_WIDTH * 1.5);
    
    private Point centro;
    private Rectangle rect;
    private Color color = Color.RED;
    
    public Blockage(Street rua, int via, int distancia) {
    	super(rua, via, distancia);
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
        g2d.draw(rect);
        g2d.fill(rect);
    }
    
}
