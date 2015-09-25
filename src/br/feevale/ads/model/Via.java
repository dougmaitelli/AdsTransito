package br.feevale.ads.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author carlosborges
 */
public class Via {

    public Point ponto1;
    public Point ponto2;

    public ArrayList<Via> conectaViasPonto1 = new ArrayList<Via>();
    public ArrayList<Via> conectaViasPonto2 = new ArrayList<Via>();

    private Via() {

    }

    public static Via getNewInstance(Point ponto1, Point ponto2) {
        Via v = new Via();
        v.ponto1 = ponto1;
        v.ponto2 = ponto2;
        return v;
    }

    public void reverter() {
        Point p = ponto1;
        ponto1 = ponto2;
        ponto2 = p;
        ArrayList<Via> l = conectaViasPonto1;
        conectaViasPonto1 = conectaViasPonto2;
        conectaViasPonto2 = l;
    }

}
