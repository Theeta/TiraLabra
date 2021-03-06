package com.mycompany.tiralabra_maven.AI;

import com.mycompany.tiralabra_maven.peli.Peli;
import com.mycompany.tiralabra_maven.peli.PeliOhjain;
import com.mycompany.tiralabra_maven.peli.Pelilauta;
import com.mycompany.tiralabra_maven.peli.Siirto;

/**
 * Luokka toteuttaa minimax-algoritmia käyttävän tekoälyn
 *
 * @author noora
 */
public class MinimaxAI extends AI {

    private Heuristiikka heuristiikka;
    private final int miniMaxSyvyys;

    public MinimaxAI(Peli peli, PeliOhjain peliohjain, boolean siirraAutomaagisesti, int viive, int syvyys) {
        super(peli, peliohjain, siirraAutomaagisesti, viive);
        this.miniMaxSyvyys = syvyys;
    }

    private int minimax(Pelilauta lauta, int syvyys, boolean valkoisenVuoroSiirtaa, boolean vuorossaOlevaPelaaja) {
        int parasArvo, arvo;
        Siirto[] siirrot = lauta.getSallitutSiirrot(valkoisenVuoroSiirtaa);
        if (syvyys <= 0 || lauta.getSallitutSiirrot(valkoisenVuoroSiirtaa) == null) {
            heuristiikka = new Heuristiikka(lauta);
            return heuristiikka.laskeTilanteenArvo(valkoisenVuoroSiirtaa);
        }
        if (vuorossaOlevaPelaaja) {
            parasArvo = -1000000;
            for (int i = 0; i < siirrot.length; i++) {
                Pelilauta kopio = lauta.teeKopio();
                kopio.teeSiirto(siirrot[i]);
                arvo = minimax(kopio, syvyys - 1, valkoisenVuoroSiirtaa, false);
                if (arvo > parasArvo) {
                    parasArvo = arvo;
                }
            }
            return parasArvo;
        } else {
            parasArvo = 1000000;
            for (int i = 0; i < siirrot.length; i++) {
                Pelilauta kopio = lauta.teeKopio();
                kopio.teeSiirto(siirrot[i]);
                arvo = minimax(kopio, syvyys - 1, valkoisenVuoroSiirtaa, true);
                if (arvo < parasArvo) {
                    parasArvo = arvo;
                }
            }
            return parasArvo;
        }
    }

    /**
     * Metodi selvittää minkä siirron tekoäly tekee suraavaksi käyttäen apuna
     * minimax-algoritmia. Algoritmin avulla pisteytetään mahdolliset siirrot ja
     * valitaan niistä tehtäväksi se, jonka saama pistemäärä on suurin. Siirtoja lasketaan käyttäjän antaman syvyyden verran eteenpäin.
     *
     * @return Palauttaa siirron, jonka tekoäly haluaa tehdä
     */
    @Override
    public Siirto seuraavaSiirto(Siirto[] sallitutSiirrot) {
        odota();

        int[] pisteet = new int[sallitutSiirrot.length];
        for (int i = 0; i < sallitutSiirrot.length; i++) {
            Pelilauta lauta = this.peli.getPelilauta().teeKopio();
            lauta.teeSiirto(sallitutSiirrot[i]);
            pisteet[i] = minimax(lauta, this.miniMaxSyvyys, this.peli.isValkoisenVuoroSiirtaa(), false);
        }
        int suurinPistemaara = 0;
        int paikka = 0;
        for (int j = 0; j < pisteet.length; j++) {
            if (pisteet[j] > suurinPistemaara) {
                suurinPistemaara = pisteet[j];
                paikka = j;
            }
        }
        return sallitutSiirrot[paikka];
    }
}
