/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.model.ponto;

import br.feevale.ads.model.CarroTemporario;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class PontoEntradaSaida extends Ponto {
    
    public int capacidade;
    public ArrayList<CarroTemporario> listCarros = new ArrayList<CarroTemporario>();
    
}
