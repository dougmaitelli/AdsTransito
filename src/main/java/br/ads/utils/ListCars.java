package br.ads.utils;

import java.util.ArrayList;

import br.ads.car.Car;

public class ListCars extends ArrayList<Car> {

    private static final long serialVersionUID = 1L;

    public ListCars getCloseCars(Car car, double distance) {
        ListCars closeCars = new ListCars();

        int totalDistance = car.getDistance();

        for (Car c : this) {
            if (Math.abs(c.getDistance() - totalDistance) <= distance) {
                closeCars.add(c);
            }
        }

        return closeCars;
    }

    public ListCars getCarsAhead(Car car) {
        ListCars closeCars = new ListCars();

        for (Car c : this) {
            if (c.getLane() != car.getLane()) {
                continue;
            }

            if (c.getDistance() > car.getDistance()) {
                closeCars.add(c);
            }
        }

        return closeCars;
    }

    public ListCars getCarsBehind(Car car) {
        ListCars closeCars = new ListCars();

        for (Car c : this) {
            if (c.getLane() != car.getLane()) {
                continue;
            }

            if (c.getDistance() < car.getDistance()) {
                closeCars.add(c);
            }
        }

        return closeCars;
    }

    public ListCars getCarsLane(int via) {
        ListCars closeCars = new ListCars();

        for (Car c : this) {
            if (c.getLane() == via) {
                closeCars.add(c);
            }
        }

        return closeCars;
    }

}
