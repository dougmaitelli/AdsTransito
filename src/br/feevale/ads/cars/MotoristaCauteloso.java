package br.feevale.ads.cars;

import br.feevale.ads.model.Carro;
import br.feevale.ads.model.Obstrucao;
import br.feevale.ads.model.PontoReducao;

public class MotoristaCauteloso extends Carro {
	
	@Override
	public void reacaoViaLivre() {
		velocidade += 10;
	}

	@Override
	public void reacaoCarroProximoFrente(Carro outroCarro) {
		if (velocidade > outroCarro.getVelocidade()) {
			velocidade -= 10;
		}
	}

	@Override
	public void reacaoCarroProximoTras(Carro outroCarro) {
		//Pegar vias laterais livres
		//trocar de via
	}

	@Override
	public void reacaoObstrucaoProxima(Obstrucao obstrucao) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reacaoPontoReducaoProximo(PontoReducao pontoReducao) {
		// TODO Auto-generated method stub
		
	}

	
}
