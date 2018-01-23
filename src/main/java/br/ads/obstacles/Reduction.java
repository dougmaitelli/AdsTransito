package br.ads.obstacles;

import br.ads.Parameters;
import br.ads.car.Car;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.ads.street.Street;

public class Reduction extends Obstacle {

    private static final int WIDTH = Street.STREET_WIDTH;
    private static final int DEEP = (int) (Street.STREET_WIDTH * 0.5);

    public static final int SPEED_REDUCER = 1;
    public static final int HOLE = 2;

    private Point center;
    private Rectangle rect;
    private int type;

    public Reduction(Street street, int lane, int distance, int type) {
        super(street, lane, distance);
        this.type = type;
    }

    public void setCenter(Point center) {
        int x = center.x - (DEEP / 2);
        int y = center.y - (WIDTH / 2);
        this.center = new Point(x, y);
        this.rect = new Rectangle(x, y, DEEP, WIDTH);
    }

    @Override
    public double calculateMaxSpeed(Car car, double currentSpeed) {
        switch (type) {
            case SPEED_REDUCER:
                if (currentSpeed < Parameters.maxSpeedObstacle) {
                    return currentSpeed;
                }
                return Parameters.maxSpeedObstacle;

            case HOLE: {
                double vel = currentSpeed * 0.70;
                if (vel < (Parameters.maxSpeedObstacle * 2)) {
                    return (Parameters.maxSpeedObstacle * 2);
                }
                return vel;
            }
        }
        return currentSpeed;
    }

    public void drawPath(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (type == SPEED_REDUCER) {
            g2d.setColor(Color.BLUE);
        } else {
            g2d.setColor(Color.MAGENTA);
        }
        g2d.draw(rect);
        g2d.fill(rect);
    }

}
