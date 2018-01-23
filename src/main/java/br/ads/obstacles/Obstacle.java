package br.ads.obstacles;

import br.ads.car.Car;
import java.awt.Graphics;
import java.awt.Point;

import br.ads.street.Street;

public abstract class Obstacle {

    private Street street;
    private int lane;

    private int distance;

    public Obstacle(Street street, int lane, int distance) {
        this.distance = distance;

        setStreet(street, lane);
    }

    public abstract void setCenter(Point center);

    public abstract void drawPath(Graphics g);

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street rua, int via) {
        this.street = rua;
        this.lane = via;
        setCenter(rua.pointForLane(via, distance));
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

    public double calculateMaxSpeed(Car car, double currentSpeed) {
        return 0;
    }

}
