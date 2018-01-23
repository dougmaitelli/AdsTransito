package br.ads.street;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import br.ads.car.Car;
import br.ads.obstacles.Obstacle;
import br.ads.utils.ListCars;

public abstract class Street {

    public static final int STREET_WIDTH = 12;
    public static final int LINE_WIDTH = 2;

    public static final Color COLOR_STREET = Color.DARK_GRAY;
    public static final Color COLOR_LINE_WHITE = Color.WHITE;
    public static final Color COLOR_LINE_YELLOW = Color.YELLOW;

    public static final BasicStroke LANE_STROKE = new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);

    public static GeneralPath path = new GeneralPath();

    protected String name;

    protected Point point1;
    protected Point point2;

    public int finishedCars = 0;

    protected int lanes;

    protected double metersPerPixel;

    protected ListCars cars = new ListCars();
    protected List<Obstacle> obstacles = new ArrayList<>();

    public Street(double metersPerPixel) {
        this.metersPerPixel = metersPerPixel;
    }

    public abstract void pack();

    public abstract void drawPath(Graphics2D g2d);

    public abstract boolean contains(Point p);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLanes() {
        return lanes;
    }

    public double getMetersPerPixel() {
        return metersPerPixel;
    }

    public abstract Point pointForLane(int lane, float distance);

    public abstract void addCar(int via, Car car);

    public abstract void removeCar(Car car);

    public ListCars getCloseCars(Car car, double distanceMeters) {
        return cars.getCloseCars(car, distanceMeters / metersPerPixel);
    }

    public Obstacle getObstaclesAhead(Car car, double distanceMeters) {
        int via = car.getLane();
        int runDistance = car.getDistance();

        Obstacle closestObstacle = null;

        for (Obstacle o : obstacles) {
            if (via != o.getLane() || runDistance > o.getDistance()) {
                continue;
            }

            if (closestObstacle == null && o.getDistance() - runDistance <= distanceMeters / metersPerPixel || closestObstacle != null && o.getDistance() < closestObstacle.getDistance()) {
                closestObstacle = o;
            }
        }

        return closestObstacle;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public ListCars getCars() {
        return cars;
    }

    public void clear() {
    }

    public abstract void processCycles(double cycles);

}
