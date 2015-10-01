package br.feevale.ads.model;

import java.awt.Point;

public abstract class Carro {

	protected Rua rua;
	protected int via;
	protected Point posicao;
	protected int velocidade;
	
	public Rua getRua() {
		return rua;
	}

	public void setRua(Rua rua) {
		this.rua = rua;
	}

	public int getVia() {
		return via;
	}

	public void setVia(int via) {
		this.via = via;
	}

	public Point getPosicao() {
		return posicao;
	}
	
	public void setPosicao(Point posicao) {
		this.posicao = posicao;
	}
	
	public int getVelocidade() {
		return velocidade;
	}
	
	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}
	
	public abstract void reacaoViaLivre();
	public abstract void reacaoCarroProximoFrente(Carro outroCarro);
	public abstract void reacaoCarroProximoTras(Carro outroCarro);
	public abstract void reacaoObstrucaoProxima(Obstrucao obstrucao);
	public abstract void reacaoPontoReducaoProximo(PontoReducao pontoReducao);
	
}
