package com.mycompany.tiralabra_maven;

import com.mycompany.tiralabra_maven.AI.EkaAI;
import com.mycompany.tiralabra_maven.AI.MinimaxAI;
import com.mycompany.tiralabra_maven.AI.MinimaxPuuAI;
import com.mycompany.tiralabra_maven.AI.Pelaaja;
import com.mycompany.tiralabra_maven.gui.Paivitettava;

/**
 * Luokka välittää tietoa pelin, pelaajien ja käyttöliittymän välillä.
 *
 * @author noora
 */
public class PeliOhjain {

    private Peli peli;
    private boolean peliKaynnissa;
    private boolean valkoisenVuoroSiirtaa;
    private Paivitettava paivitettava;
    private Siirto[] sallitutSiirrot;
    private Siirto seuraavaSiirto;
    private String viesti;
    
    private int mustaMiniMaxSyvyys;
    private int valkoinenMiniMaxSyvyys;

    private int valittuRivi;
    private int valittuSarake;

    /**
     * Konstruktorissa asetetaan alkuarvot luokan muuttujille.
     */
    public PeliOhjain() {
        this.peliKaynnissa = false;
        valittuRivi = -1;
        valittuSarake = -1;
        this.mustaMiniMaxSyvyys = 5;
        this.valkoinenMiniMaxSyvyys = 5;
    }

    public Pelilauta getPelilauta() {
        if (peli == null) {
            return null;
        }
        return peli.getPelilauta();
    }

    public Peli getPeli() {
        return peli;
    }

    
    public void asetaMustaMinimaxSyvyys(int luku) {
        this.mustaMiniMaxSyvyys = luku;
    }
    
    public void asetaValkoinenMinimaxSyvyys(int luku){
        this.valkoinenMiniMaxSyvyys = luku;
    }
    
    public void setPaivitettava(Paivitettava paivitettava) {
        this.paivitettava = paivitettava;
    }

    public boolean isPeliKaynnissa() {
        return peliKaynnissa;
    }

    public Siirto[] getSallitutSiirrot() {
        return sallitutSiirrot;
    }

    public boolean isValkoisenVuoroSiirtaa() {
        return valkoisenVuoroSiirtaa;
    }

    /**
     * Metodi luo uuden pelin. Jos peli on jo käynnissä, pelaajalle näytetään
     * virheilmoitus. Lisäksi metodi luo annetuntyyppiset pelaajaoliot.
     *
     * @param mustaPelaaja Mustan pelaajan typpi
     * @param valkoinenPelaaja Valkoisen pelaajan tyyppi
     */
    public void uusiPeli(PelaajaTyyppi mustaPelaaja, PelaajaTyyppi valkoinenPelaaja) {
        if (peliKaynnissa) {
            peliKaynnissa = false;
        }
        this.peli = new Peli();
        peli.setPaivitettava(paivitettava);
        Pelaaja musta;
        Pelaaja valkoinen;
        if (mustaPelaaja == PelaajaTyyppi.IHMINEN) {
            musta = new IhmisPelaaja(peli, this);
        } else if (mustaPelaaja == PelaajaTyyppi.MINIMAX) {
            musta = new MinimaxAI(peli, this.mustaMiniMaxSyvyys);
        } else {
            musta = new EkaAI(peli);
        }

        if (valkoinenPelaaja == PelaajaTyyppi.IHMINEN) {
            valkoinen = new IhmisPelaaja(peli, this);
        } else if (valkoinenPelaaja == PelaajaTyyppi.MINIMAX) {
            valkoinen = new MinimaxAI(peli, this.valkoinenMiniMaxSyvyys);
        } else {
            valkoinen = new EkaAI(peli);
        }

        peli.setMusta(musta);
        peli.setValkoinen(valkoinen);

        new Thread(peli).start();

        this.peliKaynnissa = true;
    }

    /**
     * Metodia kutsutaan kun käyttäjä painaa luovuta peli -nappia. Jos peliä ei
     * ole käynnissä, käyttäjälle näytetään virheilmoitus. Muuten peli
     * lopetetaan pelin luovutaPeli()-metodia käyttäen.
     */
    public void luovutaPeli() {
        if (peliKaynnissa == false) {
            this.setViesti("Aloita peli ennen luovuttamista!");
        } else {
            this.peliKaynnissa = false;
            peli.luovutaPeli();
        }

    }

    /**
     * Metodia kutsutaan kun käyttäjä painaa AI-nappia. Jos peliä ei ole
     * käynnissä, käyttäjälle näytetään virheilmoitus. Nappulan toiminnallisuus
     * tulee vielä muuttumaan tai nappula poistuu kokonaan.
     */
    public void AISiirtaa() {
        if (peliKaynnissa == false) {
            this.setViesti("Aloita ensin uusi peli!");
            return;
        }
        //JOS AI odottaa siirtoa
        //ai.teeSiirto
    }

    public void setViesti(String viesti) {
        this.viesti = viesti;
        if (paivitettava != null) {
            this.paivitettava.paivita();
        }
    }

    public String getViesti() {
        if (peli == null) {
            return this.viesti;
        } else {
            return peli.getViesti();
        }

    }

    public int getValittuRivi() {
        return valittuRivi;
    }

    public int getValittuSarake() {
        return valittuSarake;
    }

    /**
     * Metodi asettaa ihmispelaajan valitseman siirron. Pelaaja valitsee ensin
     * liikuteltavan nappulan, jonka on löydyttävä sallittujen siirtojen
     * listalta. Tämän jälkeen pelaaja valitsee ruudun, johon haluaa nappulan
     * liikuttaa. Siirron on oltava sallittu, jotta se asetettaisiin muuttujaan.
     *
     * @param rivi Pelaajan valitsema rivi
     * @param sarake Pelaajan valitsema sarake
     */
    public void valitseRuudutJoissaSiirtoTapahtuu(int rivi, int sarake) {
        if (voikoRuudustaLiikkua(rivi, sarake)) {
            this.valittuRivi = rivi;
            this.valittuSarake = sarake;
            peli.setViesti("Tee siirto");
            return;
        }
        if (this.valittuRivi < 0) {
            peli.setViesti("Valitse nappula, jota haluat liikuttaa");
            return;
        }
        if (voikoRuutuunLiikkua(rivi, sarake)) {
            seuraavaSiirto = new Siirto(valittuRivi, valittuSarake, rivi, sarake);
            valittuRivi = -1;
            valittuSarake = -1;
            return;
        }
        peli.setViesti("Valitse ruutu, johon haluat liikkua");
    }

    private boolean voikoRuudustaLiikkua(int rivi, int sarake) {
        for (Siirto siirto : sallitutSiirrot) {
            if (siirto.getAlkuRivi() == rivi && siirto.getAlkuSarake() == sarake) {
                return true;
            }
        }
        return false;
    }

    private boolean voikoRuutuunLiikkua(int rivi, int sarake) {
        for (Siirto siirto : sallitutSiirrot) {
            if (siirto.getAlkuRivi() == valittuRivi && siirto.getAlkuSarake() == valittuSarake && siirto.getLoppuRivi() == rivi && siirto.getLoppuSarake() == sarake) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodia käytetään ihmispelaajan seuraavan siirron saamiseen. Metodissa
     * odotetaan kunnes seuraava siirto on asetettu. Muuttujan tila tarkastetaan
     * tietyin väliajoin ja siirto palautetaan kun se on saatu.
     *
     * @param sallitutSiirrot Lista pelaajan sallituista siirroista
     * @return Palauttaa siirron, jonka pelaaja haluaa tehdä
     */
    public Siirto getSeuraavaSiirto(Siirto[] sallitutSiirrot) {
        this.sallitutSiirrot = sallitutSiirrot;
        seuraavaSiirto = null;
        while (seuraavaSiirto == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        this.sallitutSiirrot = null;
        return seuraavaSiirto;
    }

}