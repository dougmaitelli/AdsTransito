package br.feevale.ads.car;

import br.feevale.ads.obstacles.Blockage;
import br.feevale.ads.obstacles.Obstacle;
import br.feevale.ads.obstacles.Reduction;
import br.feevale.ads.utils.ADS_Utils;

/**
 * @author 0066115
 */
public class NormalCar extends Car {

    public NormalCar() {
        super();
        VELOCIDADE_MAXIMA = ADS_Utils.oscilateDouble(VELOCIDADE_MAXIMA, 0.75);
        // velocidade inicial do carro eh 50% da velocidade maxima na entrada
        addVelocidade(VELOCIDADE_MAXIMA * 0.5);
    }

    @Override
    public void reactionCarroAhead() {
        double reducao = -(getVelocidade() * 0.1);
        reducao = Math.min(-1, reducao);
        addVelocidade(reducao);
        if (getVelocidade() < VELOCIDADE_MAXIMA - 3d) {
            int novaVia = getVia() + (ADS_Utils.getNextFloat() > 0.5 ? 1 : -1);
            if (getCarrosProximos().getCarrosVia(novaVia).size() == 0) {
                setVia(novaVia);
            }
        }
    }

    @Override
    public void reactionObstaculoAhead(Obstacle obstaculo) {
        if (obstaculo instanceof Blockage) {
            int novaVia = getVia() + (ADS_Utils.getNextFloat() > 0.5 ? 1 : -1);

            if (getCarrosProximos().getCarrosVia(novaVia).size() == 0) {
                setVia(novaVia);
            }

            addVelocidade(-1);
        } else if (obstaculo instanceof Reduction) {
            if (getVelocidade() > obstaculo.calcularMaxVelocidade(this, getVelocidade())) {
                double reduce = Math.min(-1, -(getVelocidade() * 0.1d));
                addVelocidade(reduce);
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
