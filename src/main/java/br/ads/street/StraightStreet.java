package br.ads.street;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import br.ads.Parameters;
import br.ads.car.Car;
import br.ads.car.NormalCar;
import br.ads.obstacles.Obstacle;

public class StraightStreet extends Street {

    public Polygon form;

    public ArrayList<Point> listLanePoint1 = new ArrayList<>();
    public ArrayList<Point> listLanePoint2 = new ArrayList<>();

    public double angle;
    private Point lastPoint;

    public StraightStreet(double totalDistance) {
        super(totalDistance);
    }

    public static StraightStreet createStreetFor(int x1, int y1, int x2, int y2, int lanes, String name) {
        StraightStreet r = new StraightStreet(0.1); //metros por pixel
        r.point1 = new Point(x1, y1);
        r.point2 = new Point(x2, y2);
        r.lanes = lanes;

        if (name == null) {
            name = "No-Name Street";
        }
        r.name = name;

        r.form = new Polygon(new int[]{x1, x2, x1, x2}, new int[]{y1, y1, y2, y2}, 4);

        return r;
    }

    @Override
    public boolean contains(Point p) {
        return form.contains(p);
    }

    private void calcAngle() {
        double eq_a;
        angle = 0f;
        if (point1.x == point2.x) {
            angle = 0;
        } else {
            double y = point2.y - point1.y;
            double x = point2.x - point1.x;
            eq_a = y / x;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        }
    }

    public void pack() {
        if (lanes == 0) {
            throw new RuntimeException("No Lanes!");
        }

        calcAngle();
        double raio = lanes * STREET_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        for (int i = 1; i <= lanes; i++) {
            // linha branca ida
            tmp = STREET_WIDTH;
            tmp = tmp / 2;
            tmp = ((lanes - i) * STREET_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, point1);
            Point p2 = pointForRaio(tmp, point2);
            listLanePoint1.add(p1);
            listLanePoint2.add(p2);
        }
    }

    public void drawPath(Graphics2D g2d) {
        g2d.setColor(COLOR_STREET);
        g2d.setStroke(new BasicStroke(lanes * STREET_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        path.reset();
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        g2d.draw(path);
        // linhas na pista
        // desenha as linhas
        double raio = lanes * STREET_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        g2d.setStroke(LANE_STROKE);
        for (int i = 0; i <= lanes; i++) {
            // linha branca ida
            if ((i == 0) || (i == lanes)) {
                g2d.setColor(COLOR_LINE_YELLOW);
            } else {
                g2d.setColor(COLOR_LINE_WHITE);
            }
            tmp = (i * STREET_WIDTH);
            tmp = raio - tmp;
            path.reset();
            pointForRaio(tmp, point1);
            path.moveTo(lastPoint.x, lastPoint.y);
            pointForRaio(tmp, point2);
            path.lineTo(lastPoint.x, lastPoint.y);
            g2d.draw(path);
        }
        for (Obstacle obstrucao : obstacles) {
            obstrucao.drawPath(g2d);
        }
        // desenha carros
        for (int i = 0; i < cars.size(); i++) {
            Car carro = null;
            try {
                carro = cars.get(i);
            } catch (Exception ex) {
                continue;
            }
            if (carro != null) {
                if (carro.isFinishedCourse()) {
                    continue;
                }
                carro.drawPath(g2d);
            }
        }
    }

    public Point pointForLane(int lane, float distance) {
        Point p = listLanePoint1.get(lane);
        lastPoint = new Point();
        lastPoint.x = p.x + (int) (distance * Math.sin(angle));
        lastPoint.y = p.y + (int) (distance * Math.cos(angle));
        return lastPoint;
    }

    private Point pointForRaio(double r, Point origem) {
        lastPoint = new Point();
        lastPoint.x = origem.x + (int) (Math.cos(angle) * r);
        lastPoint.y = origem.y + (int) (Math.sin(angle) * r);
        return lastPoint;
    }

    @Override
    public void addCar(int via, Car car) {
        Point p = listLanePoint1.get(via);
        car.setCenter(p);
        cars.add(car);
        car.setStreet(this, via);
    }

    @Override
    public void removeCar(Car car) {
        cars.remove(car);
    }

    public void processCycles(double cycles) {
        List<Integer> freeLanes = new ArrayList<>();

        for (int i = 0; i < lanes; i++) {
            freeLanes.add(i);
        }

        for (Car car : cars) {
            car.moveCycle(cycles);

            if (car.getDistance() < (Parameters.maxSpeed * Parameters.entranceDistance)) {
                freeLanes.remove((Integer) car.getLane());
            }
            if (!car.isFinishedCourse()) {
                if (car.getDistance() > 1050) {
                    finishedCars++;
                    car.setFinishedCourse(true);
                }
            }
        }

        for (Integer via : freeLanes) {
            if (cars.size() < Parameters.totalVehicles) {
                NormalCar c = new NormalCar();
                addCar(via, c);
            }
        }
    }

}
