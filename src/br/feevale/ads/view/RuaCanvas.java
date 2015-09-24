/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.view;

import br.feevale.ads.model.Rua;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends Canvas implements MouseListener, MouseMotionListener {

    private ArrayList<Rua> ruas;

    public void loadRuas(File arquivo) {
        ruas = new ArrayList<Rua>();
        ruas.add(Rua.createRuaFor(50, 10, 250, 50, 3, 2));
    }

    private BasicStroke stroke = ;

    public RuaCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void paint(Graphics g) {
        Rua ruaAnterior = null;
        Rua ruaAtual = null;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        GeneralPath path = new GeneralPath();
        for (Rua rua : ruas) {
            g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            path.moveTo(xs[0], ys[0]);
            path.curveTo(xs[1], ys[1], xs[2], ys[2], xs[3], ys[3]);
            path.lin
            
        }
        g2d.draw(path);
    }

    public void mousePressed(MouseEvent e) {
        dragIndex = NOT_DRAGGING;
        int minDistance = Integer.MAX_VALUE;
        int indexOfClosestPoint = -1;
        for (int i = 0; i < 4; i++) {
            int deltaX = xs[i] - e.getX();
            int deltaY = ys[i] - e.getY();
            int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
            if (distance < minDistance) {
                minDistance = distance;
                indexOfClosestPoint = i;
            }
        }
        if (minDistance > NEIGHBORHOOD) {
            return;
        }

        dragIndex = indexOfClosestPoint;
    }

    public void mouseReleased(MouseEvent e) {
        if (dragIndex == NOT_DRAGGING) {
            return;
        }
        xs[dragIndex] = e.getX();
        ys[dragIndex] = e.getY();
        dragIndex = NOT_DRAGGING;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        if (dragIndex == NOT_DRAGGING) {
            return;
        }

        xs[dragIndex] = e.getX();
        ys[dragIndex] = e.getY();
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

}
