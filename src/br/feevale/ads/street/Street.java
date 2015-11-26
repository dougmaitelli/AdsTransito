/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.feevale.ads.street;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import br.feevale.ads.Parametros;
import br.feevale.ads.car.Car;
import br.feevale.ads.car.NormalCar;
import br.feevale.ads.obstacles.Obstacle;
import br.feevale.ads.utils.ListCars;

/**
 *
 * @author 0066115
 */
public class Street {
    
    public static final int RUA_WIDTH = 12;
    public static final int LINHA_WIDTH = 2;
    
    public static final Color COR_RUA = Color.DARK_GRAY;
    public static final Color COR_LINHA_BRANCA = Color.WHITE;
    public static final Color COR_LINHA_AMARELA = Color.YELLOW;
    public static final Color COR_LINHA_VIA_IDA = Color.GREEN;
    public static final Color COR_LINHA_VIA_VOLTA = Color.RED;
    
    public static final BasicStroke LINHA_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{5f}, 0f);
    public static final BasicStroke CAMINHO_STROKE = new BasicStroke(LINHA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public static boolean DRAW_VIA = false;
    public static GeneralPath path = new GeneralPath();
    
    protected String nome;
    
    protected Point ponto1;
    protected Point ponto2;
    
    protected int vias;
    
    protected double metrosPorPixel;
    protected double carsPerMinute;
    
    protected ListCars carros = new ListCars();
    protected List<Obstacle> obstrucoes = new ArrayList<>();
    
    public Polygon forma;

    public ArrayList<Point> listViaPonto1 = new ArrayList<Point>();
    public ArrayList<Point> listViaPonto2 = new ArrayList<Point>();

    // calculos
    public double angle;
    private Point lastPoint;
    
    public Street(double metrosPorPixel) {
		this.metrosPorPixel = metrosPorPixel;
	}
    
    public static Street createRuaFor(int x1, int y1, int x2, int y2, int vias, String nome) {
        Street r = new Street(0.1); //metros por pixel
        r.ponto1 = new Point(x1, y1);
        r.ponto2 = new Point(x2, y2);
        r.vias = vias;
        if (nome == null) {
            nome = "Rua sem nome";
        }
        r.nome = nome;
        r.forma = new Polygon(new int[]{x1, x2, x1, x2}, new int[]{y1, y1, y2, y2}, 4);
        return r;
    }
    
    private void calcAngle() {
        double eq_a;
        angle = 0f;
        if (ponto1.x == ponto2.x) {
            eq_a = 0;
            angle = 0;
        } else {
            double y = ponto2.y - ponto1.y;
            double x = ponto2.x - ponto1.x;
            eq_a = y / x;
            angle = Math.atan(eq_a) + (Math.PI / 2);
        }
    }
    
    public void pack() {
        if (vias == 0) {
            throw new RuntimeException("sem vias cadastradas");
        }
        calcAngle();
        double raio = vias * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        for (int i = 1; i <= vias; i++) {
            // linha branca ida
            tmp = RUA_WIDTH;
            tmp = tmp / 2;
            tmp = ((vias - i) * RUA_WIDTH) + tmp;
            tmp = raio - tmp;
            Point p1 = pointForRaio(tmp, ponto1);
            Point p2 = pointForRaio(tmp, ponto2);
            listViaPonto1.add(p1);
            listViaPonto2.add(p2);
        }
    }
    
    public void drawPath(Graphics2D g2d) {
        g2d.setColor(COR_RUA);
        g2d.setStroke(new BasicStroke(vias * RUA_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        path.reset();
        path.moveTo(ponto1.x, ponto1.y);
        path.lineTo(ponto2.x, ponto2.y);
        g2d.draw(path);
        // linhas na pista
        // desenha as linhas
        double raio = vias * RUA_WIDTH;
        raio = raio / 2;
        double tmp = 0;
        g2d.setStroke(LINHA_STROKE);
        for (int i = 0; i <= vias; i++) {
            // linha branca ida
            if ((i == 0) || (i == vias)) {
                g2d.setColor(COR_LINHA_AMARELA);
            } else {
                g2d.setColor(COR_LINHA_BRANCA);
            }
            tmp = (i * RUA_WIDTH);
            tmp = raio - tmp;
            path.reset();
            pointForRaio(tmp, ponto1);
            path.moveTo(lastPoint.x, lastPoint.y);
            pointForRaio(tmp, ponto2);
            path.lineTo(lastPoint.x, lastPoint.y);
            g2d.draw(path);
        }
        if (DRAW_VIA) {
            // desenha o caminho
            g2d.setColor(COR_LINHA_VIA_IDA);
            g2d.setStroke(CAMINHO_STROKE);
            for (int i = 0; i < vias; i++) {
                path.reset();
                Point p = listViaPonto1.get(i);
                path.moveTo(p.x, p.y);
                p = listViaPonto2.get(i);
                path.lineTo(p.x, p.y);
                g2d.draw(path);
            }
        }
        g2d.setColor(Color.black);
        g2d.drawString(String.valueOf(carsPerMinute), ponto2.x, ponto2.y - 30);
        for (Obstacle obstrucao : obstrucoes) {
            obstrucao.drawPath(g2d);
        }
        // desenha carros
        for (Car carro : carros) {
        	if (carro.isConcluiuPercurso()) {
        		continue;
        	}
        	
            carro.drawPath(g2d);
        }
    }
    
    public boolean contains(Point p) {
        return forma.contains(p);
    }
    
    public String getNome() {
		return nome;
	}
    
    public void setNome(String nome) {
		this.nome = nome;
	}
    
    public int getVias() {
		return vias;
	}
    
    public double getMetrosPorPixel() {
		return metrosPorPixel;
	}
    
    public Point pontoForVia(int via, float distancia) {
        Point p = listViaPonto1.get(via);
        lastPoint = new Point();
        lastPoint.x = p.x + (int) (distancia * Math.sin(angle));
        lastPoint.y = p.y + (int) (distancia * Math.cos(angle));
        return lastPoint;
    }

    private Point pointForRaio(double r, Point origem) {
        lastPoint = new Point();
        lastPoint.x = origem.x + (int) (Math.cos(angle) * r);
        lastPoint.y = origem.y + (int) (Math.sin(angle) * r);
        return lastPoint;
    }
    
    public void addCarro(int via, Car carro) {
        Point p = listViaPonto1.get(via);
        carro.setCentro(p);
        carros.add(carro);
        carro.setRua(this, via);
    }

    public void removeCarro(Car carro) {
        carros.remove(carro);
    }
    
    public ListCars getCarrosProximos(Car carro, double distanciaMetros) {
    	return carros.getCarrosProximos(carro, distanciaMetros / metrosPorPixel);
    }
    
    public Obstacle getObstaculosAhead(Car carro, double distanciaMetros) {
    	int via = carro.getVia();
    	int distanciaPercorrida = carro.getDistancia();
    	
    	Obstacle obstaculoProximo = null;
    	
    	for (Obstacle o : obstrucoes) {
    		if (via != o.getVia() || distanciaPercorrida > o.getDistancia()) {
    			continue;
    		}
    		
    		if (obstaculoProximo == null && o.getDistancia() - distanciaPercorrida <= distanciaMetros / metrosPorPixel || obstaculoProximo != null && o.getDistancia() < obstaculoProximo.getDistancia()) {
    			obstaculoProximo = o;
    		}
    	}
    	
    	return obstaculoProximo;
    }
    
    public void addObstaculo(Obstacle obstaculo) {
    	obstrucoes.add(obstaculo);
    }
    
    private long lastCarTimestamp = 0;
    private int carsCount = 0;
    
    public void processarCiclos(double ciclos) {
    	List<Integer> viasLivres = new ArrayList<>();
    	
    	for (int i = 0; i < vias; i++) {
    		viasLivres.add(i);
    	}
    	
        for (Car carro : carros) {
        	if (carro.isConcluiuPercurso()) {
        		continue;
        	}
        	
            carro.moverCiclo(ciclos);
            
            if (carro.getDistancia() < 5d / metrosPorPixel) {
            	viasLivres.remove((Integer) carro.getVia());
            }
            
            if (carro.getDistancia() > 1050) {
            	carro.setConcluiuPercurso(true);
            	carsCount++;
        	}
            
            long newCarTimestamp = System.currentTimeMillis();
    		
    		if (lastCarTimestamp == 0) {
    			lastCarTimestamp = newCarTimestamp;
    		} else {
            	if (newCarTimestamp - lastCarTimestamp > 1000) {
            		carsPerMinute = carsCount * 60;
            		carsCount = 0;
            		
            		lastCarTimestamp = newCarTimestamp;
            	}
    		}
        }
        
        for (Integer via : viasLivres) {
        	if (carros.size() < Parametros.totalVeiculos) {
    	        NormalCar c = new NormalCar();
    	        addCarro(via, c);
        	}
        }
    }
    
}
