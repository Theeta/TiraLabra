package com.mycompany.tiralabra_maven.gui;

import com.mycompany.tiralabra_maven.peli.PeliOhjain;


/**
 * Luokka tarjoaa käyttöliittymän metodit
 *
 * @author noora
 */
public class Kayttoliittyma implements Runnable {

    private final PeliOhjain peli;
    private Ikkuna frame3;

    /**
     *
     * @param peli Konstruktorille annetaan peliohjain
     */
    public Kayttoliittyma(PeliOhjain peli) {
        this.peli = peli;
    }

    /**
     * Käyttöliittymän suorittaminen luo uuden käyttöliittymäikkunan
     */
    @Override
    public void run() {
        frame3 = new Ikkuna(peli);
        frame3.setVisible(true);
    }

    public Paivitettava getPaivitettava() {
        return this.frame3;
    }

}
