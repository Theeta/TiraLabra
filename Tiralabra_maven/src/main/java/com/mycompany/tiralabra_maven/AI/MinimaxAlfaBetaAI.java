
package com.mycompany.tiralabra_maven.AI;

import com.mycompany.tiralabra_maven.tietorakenteet.Solmu;
import com.mycompany.tiralabra_maven.tietorakenteet.Lista;
import com.mycompany.tiralabra_maven.peli.Peli;
import com.mycompany.tiralabra_maven.peli.PeliOhjain;
import com.mycompany.tiralabra_maven.peli.Pelilauta;
import com.mycompany.tiralabra_maven.peli.Siirto;
import com.mycompany.tiralabra_maven.tietorakenteet.SolmujenVertailija;

/**
 * Luokka on vastaava kuin Minimax-luokka, mutta luokassa hyödynnetään erilaisia omia tietorakenteita sekä alfa-beta -karsintaa, joka pienentää tutkittavien siirtojen määrää
 * @author noora
 */
public class MinimaxAlfaBetaAI extends AI {
    private Heuristiikka heuristiikka;
    private final int syvyys;

    public MinimaxAlfaBetaAI(Peli peli, PeliOhjain peliohjain, boolean siirraAutomaagisesti, int viive, int syvyys) {
        super(peli, peliohjain, siirraAutomaagisesti, viive);
        this.syvyys = syvyys;
    }


    private int minimax(Pelilauta lauta, int syvyys, boolean valkoisenVuoroSiirtaa, boolean vuorossaOlevaPelaaja, int alfa, int beta) {
        int arvo;
        Siirto[] siirrot = lauta.getSallitutSiirrot(valkoisenVuoroSiirtaa);
        heuristiikka = new Heuristiikka(lauta);
        if (syvyys <= 0 || lauta.getSallitutSiirrot(valkoisenVuoroSiirtaa) == null) {
            return heuristiikka.laskeTilanteenArvo(valkoisenVuoroSiirtaa);
        }
        if (vuorossaOlevaPelaaja) {
            for (int i = 0; i < siirrot.length; i++) {
                Pelilauta kopio = lauta.teeKopio();
                kopio.teeSiirto(siirrot[i]);
                arvo = minimax(kopio, syvyys - 1, valkoisenVuoroSiirtaa, false, alfa, beta);
                if (arvo > alfa) {
                    alfa = arvo;
                }
                if (beta <= alfa){
                    break;
                }
            }
            return alfa;
        } else {
            for (int i = 0; i < siirrot.length; i++) {
                Pelilauta kopio = lauta.teeKopio();
                kopio.teeSiirto(siirrot[i]);
                arvo = minimax(kopio, syvyys - 1, valkoisenVuoroSiirtaa, true, alfa, beta);
                if (arvo < beta) {
                    beta = arvo;
                }
                if (beta <= alfa){
                    break;
                }
            }
            return beta;
        }
    }


    /**
     * Metodin avulla selvitetään, mikä siirto kannattaa seuraavaksi tehdä.
     * Selvityksessä käytetään apuna minimax-algoritmia ja heuristiikkaa
     * @param sallitutSiirrot Lista sallituista siirroista
     * @return Palauttaa seuraavaksi tehtävän siirron
     */
    @Override
    public Siirto seuraavaSiirto(Siirto[] sallitutSiirrot) {
        odota();

        Lista<Solmu> lista = new Lista(new SolmujenVertailija());
        for (int i = 0; i < sallitutSiirrot.length; i++) {
            Pelilauta lauta = this.peli.getPelilauta().teeKopio();
            lauta.teeSiirto(sallitutSiirrot[i]);
            lista.lisaa(new Solmu(minimax(lauta, this.syvyys, this.peli.isValkoisenVuoroSiirtaa(), false, -1000000, 1000000), sallitutSiirrot[i]));
        }
        Solmu palautus = lista.getSuurin();
        return palautus.getSiirto();
    }
}
