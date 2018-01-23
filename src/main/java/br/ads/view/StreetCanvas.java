package br.ads.view;

import br.ads.Parameters;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import br.ads.obstacles.Blockage;
import br.ads.obstacles.Reduction;
import br.ads.street.Street;
import br.ads.street.StraightStreet;
import br.ads.utils.DoubleBuffer;

public class StreetCanvas extends DoubleBuffer {

    public static ArrayList<StraightStreet> streets;

    public StreetCanvas() {
        loadStreets();
    }

    private void addStreet(Street street) {
        StreetWindow.street = street;
        if (streets == null) {
            streets = new ArrayList<>();
        }
        if (street instanceof StraightStreet) {
            streets.add((StraightStreet) street);
        }
    }

    public Street streetForPoint(Point point) {
        for (Street rua : streets) {
            if (rua.contains(point)) {
                return rua;
            }
        }
        return null;
    }

    private void processObstaclesContext(Street street) {
        switch (Parameters.context) {
            default:
            case 1:
                break;

            case 2:
                street.addObstacle(new Reduction(street, 0, 500, Reduction.HOLE));
                street.addObstacle(new Blockage(street, 1, 700));
                street.addObstacle(new Reduction(street, 2, 300, Reduction.HOLE));
                break;

            case 3:
                street.addObstacle(new Reduction(street, 0, 500, Reduction.HOLE));
                street.addObstacle(new Reduction(street, 2, 300, Reduction.HOLE));
                break;

            case 4:
                street.addObstacle(new Reduction(street, 0, 900, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 1, 900, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 2, 900, Reduction.SPEED_REDUCER));
                break;

            case 5:
                street.addObstacle(new Reduction(street, 0, 900, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 1, 900, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 2, 900, Reduction.SPEED_REDUCER));

                street.addObstacle(new Reduction(street, 0, 250, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 1, 250, Reduction.SPEED_REDUCER));
                street.addObstacle(new Reduction(street, 2, 250, Reduction.SPEED_REDUCER));
                break;

            case 6:
                street.addObstacle(new Reduction(street, 0, 500, Reduction.HOLE));
                street.addObstacle(new Reduction(street, 1, 750, Reduction.HOLE));
                street.addObstacle(new Reduction(street, 2, 300, Reduction.HOLE));
                break;

        }
    }

    public void loadStreets() {
        streets = new ArrayList<>();
        Street r = StraightStreet.createStreetFor(50, 50, 1100, 50, 3, "Street");
        addStreet(r);

        for (Street street : streets) {
            street.pack();
            processObstaclesContext(street);
        }

        invalidate();
        repaint();
    }

    public void paintBuffer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);

        for (Street rua : streets) {
            rua.drawPath(g2d);
        }
    }

}
