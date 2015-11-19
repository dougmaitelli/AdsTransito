/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.carro;

import br.feevale.ads.obstacles.Bloqueio;
import br.feevale.ads.obstacles.Obstaculo;
import br.feevale.ads.obstacles.Reducao;

/**
 *
 * @author 0066115
 */
public class CarroNormal extends Carro {
	
	public CarroNormal() {
		GRAU_DE_ATENCAO = Math.random() * 5d + 3d; // metros
		VELOCIDADE_MAXIMA = Math.random() * 10d + 10d; // metros por segundo
		
		addVelocidade(Math.random() * 5d + 10d);
	}
	
	@Override
	public void reactionCarroAhead() {
		addVelocidade(-1);
		
		if (getVelocidade() < VELOCIDADE_MAXIMA - 3d) {
			int novaVia = getVia() + (Math.random() > 0.5 ? 1 : -1);
			
			if (getCarrosProximos().getCarrosVia(novaVia).size() == 0) {
				setVia(novaVia);
			}
		}
	}
	
	@Override
	public void reactionObstaculoAhead(Obstaculo obstaculo) {
		if (obstaculo instanceof Bloqueio) {
			int novaVia = getVia() + (Math.random() > 0.5 ? 1 : -1);
			
			if (getCarrosProximos().getCarrosVia(novaVia).size() == 0) {
				setVia(novaVia);
			}
			
			addVelocidade(-1);
		} else if (obstaculo instanceof Reducao) {
			if (getVelocidade() > VELOCIDADE_MAXIMA * 0.5d) {
				addVelocidade(-1);
			} else {
				addVelocidade(1);
			}
		}
	}
	
	@Override
	public void reactionCarroBehind() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void reactionCaminhoLivre() {
		addVelocidade(0.2);
	}

}
