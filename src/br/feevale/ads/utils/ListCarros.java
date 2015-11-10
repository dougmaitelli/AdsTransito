package br.feevale.ads.utils;

import java.util.ArrayList;

import br.feevale.ads.model.Carro;

public class ListCarros extends ArrayList<Carro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ListCarros getCarrosProximos(Carro carro, int distancia) {
    	ListCarros carrosProximos = new ListCarros();
    	
    	int distanciaPercorrida = carro.getDistancia();
    	
    	for (Carro c : this) {
    		if (Math.abs(c.getDistancia() - distanciaPercorrida) >= distancia) {
    			carrosProximos.add(c);
    		}
    	}
    	
    	return carrosProximos;
    }
	
	public ListCarros getCarrosAhead(Carro carro) {
		ListCarros carrosProximos = new ListCarros();
		
		for (Carro c : this) {
			if (c.getDistancia() > carro.getDistancia()) {
				carrosProximos.add(c);
			}
		}
		
		return carrosProximos;
	}
	
	public ListCarros getCarrosBehind(Carro carro) {
		ListCarros carrosProximos = new ListCarros();
		
		for (Carro c : this) {
			if (c.getDistancia() < carro.getDistancia()) {
				carrosProximos.add(c);
			}
		}
		
		return carrosProximos;
	}

}
