/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.model;

/**
 *
 * @author 0066115
 */
public class CarroTemporario extends Carro {
	
	public CarroTemporario() {
		DISTANCIA_DE_ATENCAO = Math.random() * 8d + 2d; // metros
		VELOCIDADE_MAXIMA = Math.random() * 10d + 10d; // metros por segundo
		
		addVelocidade(Math.random() * 10);
	}
	
	@Override
	public void reactionCarroAhead() {
		if (Math.random() < 0.1) {
			int novaVia = getVia() + (Math.random() > 0.5 ? 1 : -1);
			
			if (getCarrosProximos().getCarrosVia(novaVia).size() == 0) {
				setVia(novaVia);
			}
		} else {
			//TODO: Only 
			addVelocidade(-1);
		}
	}
	
	@Override
	public void reactionCarroBehind() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void reactionCaminhoLivre() {
		addVelocidade(1);
	}

}
