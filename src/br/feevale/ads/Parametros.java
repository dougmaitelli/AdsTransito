package br.feevale.ads;

import br.feevale.ads.utils.ADS_Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 0066115
 */
public class Parametros {

    public static enum Tipo {

        CICLO_POR_SEGUNDO("Ciclos por seg."),
        TOTAL_VEICULOS("Total de veículos"),
        VELOCIDADE_MAXIMA("Velocidade Máx."),
        GRAU_ATENCAO("Grau de atenção"),
        DISTANCIA_ATENCAO("Dist. atenção min"),
        SEMENTE_RANDOM("Semente");

        private final String titulo;

        private Tipo(String titulo) {
            this.titulo = titulo;
        }

        public String getTitulo() {
            return titulo;
        }
        
        public static List<Tipo> orderedList() {
            ArrayList<Tipo> list = new ArrayList<Tipo>();
            list.add(VELOCIDADE_MAXIMA);
            list.add(TOTAL_VEICULOS);
            list.add(SEMENTE_RANDOM);
            list.add(DISTANCIA_ATENCAO);
            list.add(GRAU_ATENCAO);
            list.add(CICLO_POR_SEGUNDO);
            return list;
        }

    }

    public static HashMap<Tipo, String> getListParametros() {
        HashMap map = new HashMap<Tipo, String>();
        // primarios
        map.put(Tipo.VELOCIDADE_MAXIMA, "" + velocidadeMaxima);
        map.put(Tipo.TOTAL_VEICULOS, "" + totalVeiculos);
        map.put(Tipo.SEMENTE_RANDOM, "" + sementeRandom);
        // secundarios
        map.put(Tipo.DISTANCIA_ATENCAO, "" + distanciaAtencao);
        map.put(Tipo.GRAU_ATENCAO, "" + grauAtencao);
        map.put(Tipo.CICLO_POR_SEGUNDO, "" + ciclosPorSegundo);
        return map;
    }
    
    public static boolean putListParametros(HashMap<Tipo, String> valores) {
        for (Tipo tp : valores.keySet()) {
            String valor = valores.get(tp);
            try {
                switch (tp) {
                    case CICLO_POR_SEGUNDO:
                        ciclosPorSegundo = Integer.parseInt(valor);
                        break;

                    case DISTANCIA_ATENCAO:
                        distanciaAtencao = Double.parseDouble(valor);
                        break;

                    case GRAU_ATENCAO:
                        grauAtencao = Double.parseDouble(valor);
                        break;

                    case VELOCIDADE_MAXIMA:
                        velocidadeMaxima = Integer.parseInt(valor);
                        break;

                    case TOTAL_VEICULOS:
                        totalVeiculos = Integer.parseInt(valor);
                        break;

                    case SEMENTE_RANDOM:
                        sementeRandom = Long.parseLong(valor);
                        ADS_Utils.setRandomSeed(sementeRandom);
                        break;
                }
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

    public static long sementeRandom = 2015l;
    public static double distanciaAtencao = 3;
    public static double grauAtencao = 0.5;
    public static int ciclosPorSegundo = 30;
    public static int totalVeiculos = 140;
    public static int velocidadeMaxima = 30;

}
