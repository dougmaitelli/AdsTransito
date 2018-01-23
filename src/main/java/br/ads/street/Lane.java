package br.ads.street;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author carlosborges
 */
public class Lane {

    public String nome;
    public Point ponto1;
    public Point ponto2;

    public ArrayList<Lane> conectaViasPonto1 = new ArrayList<Lane>();
    public ArrayList<Lane> conectaViasParalelasIda = new ArrayList<Lane>();
    public ArrayList<Lane> conectaViasPonto2 = new ArrayList<Lane>();
    public ArrayList<Lane> conectaViasParalelasVolta = new ArrayList<Lane>();

    private Lane() {

    }

    public static Lane getNewInstance(Point ponto1, Point ponto2, String nome) {
        Lane v = new Lane();
        v.ponto1 = ponto1;
        v.ponto2 = ponto2;
        v.nome = nome;
        return v;
    }

    public void reverter() {
        Point p = ponto1;
        ponto1 = ponto2;
        ponto2 = p;
        // pontos conectados
        ArrayList<Lane> l = conectaViasPonto1;
        conectaViasPonto1 = conectaViasPonto2;
        conectaViasPonto2 = l;
        // paralelas
        l = conectaViasParalelasIda;
        conectaViasParalelasIda = conectaViasParalelasVolta;
        conectaViasParalelasVolta = l;
    }

}
