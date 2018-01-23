package br.ads.car;

import br.ads.Parameters;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import br.ads.obstacles.Obstacle;
import br.ads.street.Street;
import br.ads.utils.ListCars;

public abstract class Car {

    private static final int PADDING = 4;
    private static final int WIDTH = Street.STREET_WIDTH - PADDING;
    private static final int DEEP = (int) (Street.STREET_WIDTH * 1.5);

    protected double ATTENTION_LEVEL = Parameters.attentionLevel;
    protected double ATTENTION_DISTANCE = Parameters.attentionDistance; // metros
    protected double MAX_SPEED = Parameters.maxSpeed; // metros por segundo

    private Point centro;
    private Rectangle rect;
    private Color color = Color.GREEN;

    private static int SEQ_ID_GENERATOR = 1;

    private Street street;
    private int lane;
    private int id;

    private int distance = 0;
    private double speed = 0;
    private boolean finishedCourse = false;

    public long timeStarted = 0L;
    public long timeFinished = 0L;

    private ListCars closeCars;

    public Car() {
        id = SEQ_ID_GENERATOR++;
    }

    public int getId() {
        return id;
    }

    public void setCenter(Point center) {
        int x = center.x - (DEEP / 2);
        int y = center.y - (WIDTH / 2);
        this.centro = new Point(x, y);
        this.rect = new Rectangle(x, y, DEEP, WIDTH);
    }

    public void drawPath(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.draw(rect);
        g2d.fill(rect);
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street, int lane) {
        if (this.street != null) {
            street.removeCar(this);
        }
        this.street = street;
        this.lane = lane;
        timeStarted = System.currentTimeMillis();
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        if (lane < 0 || lane > street.getLanes() - 1) {
            return;
        }

        this.lane = lane;
    }

    public int getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMaxSpeed() {
        return MAX_SPEED;
    }

    public void addSpeed(double speed) {
        this.speed += speed;
        if (this.speed < 0) {
            this.speed = 0;
        }
        if (this.speed > MAX_SPEED) {
            this.speed = MAX_SPEED;
        }
        ATTENTION_DISTANCE = Math.max(Parameters.attentionDistance, this.speed * ATTENTION_LEVEL);
    }

    public boolean isFinishedCourse() {
        return finishedCourse;
    }

    public void setFinishedCourse(boolean finishedCourse) {
        if (this.finishedCourse) {
            return;
        }
        timeFinished = System.currentTimeMillis();
        this.finishedCourse = finishedCourse;
    }

    public ListCars getCloseCars() {
        return closeCars;
    }

    public void moveCycle(double cycles) {
        if (street == null) {
            return;
        }

        closeCars = street.getCloseCars(this, ATTENTION_DISTANCE);

        if (closeCars.getCarsAhead(this).size() > 0) {
            reactionCarAhead();
        } else {
            Obstacle obstacle = street.getObstaclesAhead(this, ATTENTION_DISTANCE);

            if (obstacle != null) {
                reactionObstacleAhead(obstacle);
            } else {
                reactionFreeWay();
            }
        }

        if (closeCars.getCarsBehind(this).size() > 0) {
            reactionCarBehind();
        }

        distance += (cycles * speed) / street.getMetersPerPixel();

        setCenter(street.pointForLane(lane, distance));
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public long getTimeFinished() {
        return timeFinished;
    }

    public abstract void reactionCarAhead();

    public abstract void reactionObstacleAhead(Obstacle obstacle);

    public abstract void reactionCarBehind();

    public abstract void reactionFreeWay();

}
