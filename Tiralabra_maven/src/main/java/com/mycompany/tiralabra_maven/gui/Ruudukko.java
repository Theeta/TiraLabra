package com.mycompany.tiralabra_maven.gui;

import com.mycompany.tiralabra_maven.Nappula;
import com.mycompany.tiralabra_maven.Peli;
import com.mycompany.tiralabra_maven.Siirto;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 * Luokka tarjoaa metodit, jotka tarvitaan peliruudukon piirtämiseen
 * @author noora
 */
public class Ruudukko extends JPanel {

    private final Peli peli;

    /**
     * Konstruktori saa parametrinaan pelin
     * @param peli Käynnissä oleva peli
     */
    public Ruudukko(Peli peli) {
        this.peli = peli;
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        piirraRuudut(g);
        if (peli.isPeliKaynnissa()) {
            merkitseVaihtoehtoisetLiikutettavat(g);
            if (peli.getValittuRivi() >= 0) {
                merkitseLiikutettavaNappula(g);
            }
        }
    }

    /**
     * Metodi piirtää pelilaudan ruudut
     * @param g 
     */
    private void piirraRuudut(Graphics g) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (y % 2 == x % 2) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(4 + x * 40, 4 + y * 40, 40, 40);
                piirraNappulat(y, x, g);

            }
        }
    }

    /**
     * Metodi piirtää pelilaudan nappulat
     * @param y
     * @param x
     * @param g 
     */
    private void piirraNappulat(int y, int x, Graphics g) {
        Nappula nappula = this.peli.getPelilauta().getNappula(y, x);
        if (nappula != null) {
            switch (nappula) {
                case MUSTA:
                    g.setColor(Color.BLACK);
                    g.fillOval(8 + x * 40, 8 + y * 40, 30, 30);
                    return;
                case VALKOINEN:
                    g.setColor(Color.WHITE);
                    g.fillOval(8 + x * 40, 8 + y * 40, 30, 30);
                    return;
                case KRUUNATTU_MUSTA:
                    g.setColor(Color.BLACK);
                    g.fillOval(8 + x * 40, 8 + y * 40, 30, 30);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("a", Font.BOLD, 25));
                    g.drawString("K", 14 + x * 40, 32 + y * 40);
                    return;
                case KRUUNATTU_VALKOINEN:
                    g.setColor(Color.WHITE);
                    g.fillOval(8 + x * 40, 8 + y * 40, 30, 30);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("a", Font.BOLD, 25));
                    g.drawString("K", 14 + x * 40, 32 + y * 40);
            }
        }
    }

    /**
     * Metodi merkitsee nappulat, joita on mahdollista liikuttaa
     * @param g 
     */
    private void merkitseVaihtoehtoisetLiikutettavat(Graphics g) {
        g.setColor(Color.BLUE);
        Siirto[] sallitut = peli.getSallitutSiirrot();
        if (sallitut.length != 0) {
            for (Siirto sallitut1 : sallitut) {
                g.drawRect(4 + sallitut1.getAlkuSarake() * 40, 4 + sallitut1.getAlkuRivi() * 40, 38, 38);
                g.drawRect(6 + sallitut1.getAlkuSarake() * 40, 6 + sallitut1.getAlkuRivi() * 40, 34, 34);
            }
        }
    }

    /**
     * Metodi merkitsee nappulan, jota pelaaja on päättänyt liikuttaa
     * @param g 
     */
    private void merkitseLiikutettavaNappula(Graphics g) {
        g.setColor(Color.CYAN);
        g.drawRect(4 + peli.getValittuSarake() * 40, 4 + peli.getValittuRivi() * 40, 38, 38);
        g.drawRect(6 + peli.getValittuSarake() * 40, 6 + peli.getValittuRivi() * 40, 34, 34);
        merkitseMihinVoidaanLiikkua(g);
    }

    /**
     * Metodi merkitsee ruudut, joihin valittu nappula on mahdollista liikuttaa
     * @param g 
     */
    private void merkitseMihinVoidaanLiikkua(Graphics g) {
        Siirto[] sallitut = peli.getSallitutSiirrot();

        g.setColor(Color.GREEN);
        for (int i = 0; i < sallitut.length; i++) {
            if (sallitut[i].getAlkuSarake() == peli.getValittuSarake() && sallitut[i].getAlkuRivi() == peli.getValittuRivi()) {
                g.drawRect(4 + sallitut[i].getLoppuSarake() * 40, 4 + sallitut[i].getLoppuRivi() * 40, 38, 38);
                g.drawRect(6 + sallitut[i].getLoppuSarake() * 40, 6 + sallitut[i].getLoppuRivi() * 40, 34, 34);
            }
        }
    }

}