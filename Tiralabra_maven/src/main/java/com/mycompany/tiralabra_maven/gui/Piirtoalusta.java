
package com.mycompany.tiralabra_maven.gui;

import com.mycompany.tiralabra_maven.Peli;
import com.mycompany.tiralabra_maven.Siirto;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Luokka tarjoaa metodit pelinäkymän piirtämiseen
 * @author noora
 */
public class Piirtoalusta extends JPanel implements ActionListener, Paivitettava {

    private final Peli peli;
    private JButton uusiPeliNappi;
    private JButton luovutaPeliNappi;
    private JButton AINappi;
    private JLabel viestiKentta;
    private final Ruudukko ruudukko;

    /**
     * Konstruktorille annetaan käynnissä oleva peli.
     * Se asettaa ikkunan koon, luo uuden peliruudukon ja pelin napit sekä viestikentän
     * @param peli 
     */
    public Piirtoalusta(Peli peli) {
        this.peli = peli;
        this.setPreferredSize(new Dimension(700, 500));
        this.ruudukko = new Ruudukko(peli);
        luoUusiPeliNappi();
        luoLuovutaPeliNappi();
        luoAINappi();
        luoViestiKentta();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        add(ruudukko);
        add(uusiPeliNappi);
        add(luovutaPeliNappi);
        add(AINappi);
        add(viestiKentta);

        ruudukko.setBounds(20, 20, 328, 328);
        uusiPeliNappi.setBounds(470, 120, 120, 30);
        luovutaPeliNappi.setBounds(470, 180, 120, 30);
        AINappi.setBounds(470, 240, 120, 30);
        viestiKentta.setBounds(0, 400, 700, 50);

        ruudukko.addMouseListener(new Hiirenkuuntelija(peli));

    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == AINappi) {
            peli.AISiirtaa();
        } else if (o == uusiPeliNappi) {
            peli.uusiPeli();
        } else if (o == luovutaPeliNappi) {
            peli.luovutaPeli();
        }
    }

    /**
     * Metodi luo napin, jolla voi aloittaa uuden pelin
     */
    private void luoUusiPeliNappi() {
        this.uusiPeliNappi = new JButton("Aloita peli");
        uusiPeliNappi.addActionListener(this);
    }

    /**
     * Metodi luo napin, jolla voi luovuttaa ja siten lopettaa pelin
     */
    private void luoLuovutaPeliNappi() {
        this.luovutaPeliNappi = new JButton("Luovuta");
        luovutaPeliNappi.addActionListener(this);
    }

    /**
     * Metodi luo napin, jolla saa AIn tekemään siirron
     */
    private void luoAINappi() {
        this.AINappi = new JButton("AI");
        AINappi.addActionListener(this);
    }

    /**
     * Metodi luo kentän, jossa näytetään käyttäjälle viestejä
     */
    private void luoViestiKentta() {
        this.viestiKentta = new JLabel("Tammi", JLabel.CENTER);
        viestiKentta.setFont(new Font("a", Font.PLAIN, 14));
    }

    /**
     * Metodi näyttää viestin käyttäjälle viestikentässä
     * @param viesti Käyttäjälle näytettävä viesti
     */
    public void naytaViesti(String viesti) {
        this.viestiKentta.setText(viesti);
    }

    public void paivita() {
        System.out.println("paivita");
        super.repaint();
    }

    /**
     * Metodi muokkaa nappeja siten, että uuden pelin aloittaminen ei ole mahdollista
     */
    public void muokkaaNapitKunPeliKaynnissa() {
        this.uusiPeliNappi.setEnabled(false);
        this.luovutaPeliNappi.setEnabled(true);
        this.AINappi.setEnabled(true);
    }

    /**
     * Metodi muokkaa nappeja sitten, että pelin luovuttaminen tai AIn käyttö ei ole mahdollista
     */
    public void muokkaaNapitKunPeliLoppu() {
        this.uusiPeliNappi.setEnabled(true);
        this.luovutaPeliNappi.setEnabled(false);
        this.AINappi.setEnabled(false);
    }
    
    /**
     * Metodi muokkaa nappeja siten, että AIn käyttö on mahdollista
     */
    public void muokkaaNapitKunAInVuoro(){
        this.AINappi.setEnabled(true);
    }
    
    /**
     * Metodi muokkaa nappeja siten että AIn käyttö ei ole mahdollista
     */
    public void muokkaaNapitKunEiOleAInVuoro(){
        this.AINappi.setEnabled(false);
    }
}