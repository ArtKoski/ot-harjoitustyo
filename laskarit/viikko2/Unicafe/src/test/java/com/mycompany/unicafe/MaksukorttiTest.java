package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void SaldoAlussaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    @Test
    public void SaldoToimii() {
        assertEquals(10, kortti.saldo());
    }
    @Test
    public void RahanLataaminenToimii() {
        kortti.lataaRahaa(5);
        assertEquals("saldo: 0.15", kortti.toString());
    }
    @Test
    public void RahanOttaminenToimii() {
        kortti.otaRahaa(5);
        assertEquals("saldo: 0.5", kortti.toString());
    }
    @Test
    public void RahanOttaminenToimiiTrue() {
        assertTrue(kortti.otaRahaa(5));
        
    }
    @Test
    public void RahanOttaminenToimiiFalse() {
        assertFalse(kortti.otaRahaa(15));
        
    }
    @Test
    public void RahanOttaminenToimiiKunRahaEiRiita() {
        kortti.otaRahaa(15);
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
}
