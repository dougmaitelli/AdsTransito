package br.feevale.ads.view;

import br.feevale.ads.Parametros;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import br.feevale.ads.obstacles.Blockage;
import br.feevale.ads.obstacles.Reduction;
import br.feevale.ads.street.Street;
import br.feevale.ads.street.StraightStreet;
import br.feevale.ads.utils.DoubleBuffer;

/**
 *
 * @author 0066115
 */
public class RuaCanvas extends DoubleBuffer {

    public static ArrayList<StraightStreet> ruas;

    private void addRua(Street rua) {
        RuaWindow.rua = rua;
        if (ruas == null) {
            ruas = new ArrayList<StraightStreet>();
        }
        if (rua instanceof StraightStreet) {
            ruas.add((StraightStreet) rua);
        }
    }

    public Street ruaForPoint(Point ponto) {
        for (Street rua : ruas) {
            if (rua.contains(ponto)) {
                return rua;
            }
        }
        return null;
    }

    private void processarObstaculosContexto(Street rua) {
        /* =================== TIAGO ===============
         ADICIONA AQUI OS DIVERSOS CONTEXTOS QUE TU TESTAR E FAZER
         GRAFICOS DE FORMA QUE POSSAMOS SIMULAR NA FRENTE DA PROFESSORA
         CASO ELA SOLICITE.
         ===========================================
         */
        switch (Parametros.contexto) {
            default:
            case 1: // sem nenhum obstaculo
                break;

            case 2: // obstaculos padroes que testamos em aula
                rua.addObstaculo(new Reduction(rua, 0, 500, Reduction.TIPO_BURACO));
                rua.addObstaculo(new Blockage(rua, 1, 700));
                rua.addObstaculo(new Reduction(rua, 2, 300, Reduction.TIPO_BURACO));
                break;

            case 3: // apenas redutores
                rua.addObstaculo(new Reduction(rua, 0, 500, Reduction.TIPO_BURACO));
                rua.addObstaculo(new Reduction(rua, 2, 300, Reduction.TIPO_BURACO));
                break;

            case 4: // quebra molas
                rua.addObstaculo(new Reduction(rua, 0, 900, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 1, 900, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 2, 900, Reduction.TIPO_REDUTOR));
                break;

            case 5: // quebra molas
                rua.addObstaculo(new Reduction(rua, 0, 900, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 1, 900, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 2, 900, Reduction.TIPO_REDUTOR));
                // inicio
                rua.addObstaculo(new Reduction(rua, 0, 250, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 1, 250, Reduction.TIPO_REDUTOR));
                rua.addObstaculo(new Reduction(rua, 2, 250, Reduction.TIPO_REDUTOR));
                break;

            case 6: // buracos
                rua.addObstaculo(new Reduction(rua, 0, 500, Reduction.TIPO_BURACO));
                rua.addObstaculo(new Reduction(rua, 1, 750, Reduction.TIPO_BURACO));
                rua.addObstaculo(new Reduction(rua, 2, 300, Reduction.TIPO_BURACO));
                break;

        }
    }

    public void loadRuas(File arquivo) {
        ruas = new ArrayList<StraightStreet>();
        Street r = StraightStreet.createRuaFor(50, 50, 1100, 50, 3, "Pereira");
        addRua(r);
        /* =================== TIAGO ===============
         ADICIONA AQUI OS DIVERSOS CONTEXTOS QUE TU TESTAR E FAZER
         GRAFICOS DE FORMA QUE POSSAMOS SIMULAR NA FRENTE DA PROFESSORA
         CASO ELA SOLICITE.
         ============================================
         */
        for (Street rua : ruas) {
            rua.pack();
            processarObstaculosContexto(rua);
        }
        // repinta tela
        invalidate();
        repaint();
    }

    public RuaCanvas() {
        // canvas
        loadRuas(null);
    }

    public void paintBuffer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // pinta de branco
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);
        // carro
        for (Street rua : ruas) {
            rua.drawPath(g2d);
        }
    }

}
