package br.ads.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.ads.street.Street;

public class Blockage extends Obstacle {

    private static final int WIDTH = Street.STREET_WIDTH;
    private static final int DEEP = (int) (Street.STREET_WIDTH * 1.5);

    private Point center;
    private Rectangle rect;
    private Color color = Color.RED;

    public Blockage(Street rua, int via, int distance) {
    	super(rua, via, distance);
	}

    public void setCenter(Point center) {
        int x = center.x - (DEEP / 2);
        int y = center.y - (WIDTH / 2);
        this.center = new Point(x, y);
        this.rect = new Rectangle(x, y, DEEP, WIDTH);
    }

    public void drawPath(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.draw(rect);
        g2d.fill(rect);
    }

}
