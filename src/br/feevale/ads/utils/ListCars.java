package br.feevale.ads.utils;

import java.util.ArrayList;

import br.feevale.ads.car.Car;

public class ListCars extends ArrayList<Car> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ListCars getCarrosProximos(Car carro, double distancia) {
        ListCars carrosProximos = new ListCars();

        int distanciaPercorrida = carro.getDistancia();

        for (Car c : this) {
            if (Math.abs(c.getDistancia() - distanciaPercorrida) <= distancia) {
                carrosProximos.add(c);
            }
        }

        return carrosProximos;
    }

    public ListCars getCarrosAhead(Car carro) {
        ListCars carrosProximos = new ListCars();

        for (Car c : this) {
            if (c.getVia() != carro.getVia()) {
                continue;
            }

            if (c.getDistancia() > carro.getDistancia()) {
                carrosProximos.add(c);
            }
        }

        return carrosProximos;
    }

    public ListCars getCarrosBehind(Car carro) {
        ListCars carrosProximos = new ListCars();

        for (Car c : this) {
            if (c.getVia() != carro.getVia()) {
                continue;
            }

            if (c.getDistancia() < carro.getDistancia()) {
                carrosProximos.add(c);
            }
        }

        return carrosProximos;
    }

    public ListCars getCarrosVia(int via) {
        ListCars carrosProximos = new ListCars();

        for (Car c : this) {
            if (c.getVia() == via) {
                carrosProximos.add(c);
            }
        }

        return carrosProximos;
    }

}
