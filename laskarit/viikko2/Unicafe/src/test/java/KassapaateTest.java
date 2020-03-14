/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.unicafe.Kassapaate;
import com.mycompany.unicafe.Maksukortti;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author artkoski
 */
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {
    
        Kassapaate kassapaate;
        Maksukortti kortti;
    
    public KassapaateTest() {
        kassapaate = new Kassapaate();
        kortti = new Maksukortti(500);
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void KonstruktoriToimiiko() {
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    @Test
    public void KateisOstoEdullisestiRahaaTarpeeksi() {
        assertEquals(260, 
        kassapaate.syoEdullisesti(500));
    }
    @Test
    public void KateisOstoEdullisestiRahaaEiTarpeeksi() {
        assertEquals(230, 
        kassapaate.syoEdullisesti(230));
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    @Test
    public void KateisOstoEdullisestiLounaidenMaara() {
        kassapaate.syoEdullisesti(240);
        assertEquals(1, 
        kassapaate.edullisiaLounaitaMyyty());
    }
    @Test
    public void KateisOstoMaukkaastiRahaaTarpeeksi() {
        assertEquals(100, 
        kassapaate.syoMaukkaasti(500));
    }
    @Test
    public void KateisOstoMaukkaastiRahaaEiTarpeeksi() {
        assertEquals(350, 
        kassapaate.syoMaukkaasti(350));
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    @Test
    public void KateisOstoMaukkaastiLounaidenMaara() {
        kassapaate.syoMaukkaasti(400);
        assertEquals(1, 
        kassapaate.maukkaitaLounaitaMyyty());
    }
    @Test
    public void KorttiOstoRahaaTarpeeksiJaLounaitaMyyty() {
        assertTrue(kassapaate.syoEdullisesti(kortti));
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
    }
    @Test
    public void KorttiOstoRahaaTarpeeksiMaukkaastiJaLounaitaMyyty() {
        assertTrue(kassapaate.syoMaukkaasti(kortti));
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
    }
    @Test
    public void KortillaEiRahaaMaukkaasti() {
        kortti.otaRahaa(400);
        assertFalse(kassapaate.syoMaukkaasti(kortti));
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    @Test
    public void KortillaEiRahaaEdullisesti() {
        kortti.otaRahaa(400);
        assertFalse(kassapaate.syoEdullisesti(kortti));
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    @Test
    public void KassaEiMuutuKortillaOstaessa() {
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
        kassapaate.syoEdullisesti(kortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    @Test
    public void KortinLataus() {
        kassapaate.lataaRahaaKortille(kortti, 50);
        assertEquals(550, kortti.saldo());
        assertEquals(100_050, kassapaate.kassassaRahaa());
    }
    @Test
    public void KortinLatausNegatiivinen() {
        kassapaate.lataaRahaaKortille(kortti, -50);
        assertEquals(500, kortti.saldo());
        assertEquals(100_000, kassapaate.kassassaRahaa());
    }
     
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
