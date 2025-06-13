import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import model.Fitxa;

/**
 * Tests exhaustivos para la clase Fitxa
 */
public class TestFitxa {
    
    @Test
    public void testConstructor() {
        // Test constructor con letra normal y valor positivo
        Fitxa fitxa = new Fitxa('A', 1);
        assertEquals('A', fitxa.getLletra());
        assertEquals(1, fitxa.getValor());
        
        // Test constructor con letra minúscula
        Fitxa fitxaMinuscula = new Fitxa('b', 3);
        assertEquals('b', fitxaMinuscula.getLletra());
        
        // Test constructor con número como carácter
        Fitxa fitxaNumero = new Fitxa('7', 5);
        assertEquals('7', fitxaNumero.getLletra());
        
        // Test constructor con valor cero
        Fitxa fitxaCero = new Fitxa('C', 0);
        assertEquals(0, fitxaCero.getValor());
        
        // Test constructor con valor negativo (asumimos que se permite)
        Fitxa fitxaNegativa = new Fitxa('D', -2);
        assertEquals(-2, fitxaNegativa.getValor());
    }
    
    @Test
    public void testGetLletra() {
        Fitxa fitxa = new Fitxa('E', 1);
        assertEquals('E', fitxa.getLletra());
        
        Fitxa fitxaEspecial = new Fitxa('Ñ', 8);
        assertEquals('Ñ', fitxaEspecial.getLletra());
        
        Fitxa fitxaSimbol = new Fitxa('$', 0);
        assertEquals('$', fitxaSimbol.getLletra());
    }
    
    @Test
    public void testGetValor() {
        Fitxa fitxaValorBajo = new Fitxa('A', 1);
        assertEquals(1, fitxaValorBajo.getValor());
        
        Fitxa fitxaValorAlto = new Fitxa('Z', 10);
        assertEquals(10, fitxaValorAlto.getValor());
        
        Fitxa fitxaComodin = new Fitxa('*', 0);
        assertEquals(0, fitxaComodin.getValor());
    }
    
    @Test
    public void testSetValor() {
        // Test cambio de valor positivo a positivo
        Fitxa fitxa = new Fitxa('F', 4);
        fitxa.setValor(6);
        assertEquals(6, fitxa.getValor());
        
        // Test cambio a cero
        fitxa.setValor(0);
        assertEquals(0, fitxa.getValor());
        
        // Test cambio a valor negativo
        fitxa.setValor(-3);
        assertEquals(-3, fitxa.getValor());
        
        // Verificar que la letra no cambia
        assertEquals('F', fitxa.getLletra());
    }
    
    @Test
    public void testCaracteresEspeciales() {
        // Test con letra Ñ
        Fitxa fitxaEnie = new Fitxa('Ñ', 8);
        assertEquals('Ñ', fitxaEnie.getLletra());
        assertEquals(8, fitxaEnie.getValor());
        
        // Test con caracteres acentuados
        Fitxa fitxaAcento = new Fitxa('Á', 1);
        assertEquals('Á', fitxaAcento.getLletra());
        
        // Test con comodín
        Fitxa fitxaComodin = new Fitxa('*', 0);
        assertEquals('*', fitxaComodin.getLletra());
        assertEquals(0, fitxaComodin.getValor());
        
        // Test con símbolo
        Fitxa fitxaSimbolo = new Fitxa('?', 0);
        assertEquals('?', fitxaSimbolo.getLletra());
    }
    
    @Test
    public void testLimitesValor() {
        // Test con valor extremadamente alto
        Fitxa fitxaValorAlto = new Fitxa('X', Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fitxaValorAlto.getValor());
        
        // Test con valor extremadamente bajo
        Fitxa fitxaValorBajo = new Fitxa('Y', Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, fitxaValorBajo.getValor());
    }
    
    @Test
    public void testCambiosMultiples() {
        Fitxa fitxa = new Fitxa('G', 2);
        
        // Cambios sucesivos de valor
        fitxa.setValor(5);
        assertEquals(5, fitxa.getValor());
        
        fitxa.setValor(0);
        assertEquals(0, fitxa.getValor());
        
        fitxa.setValor(10);
        assertEquals(10, fitxa.getValor());
        
        // Verificar que la letra se mantiene
        assertEquals('G', fitxa.getLletra());
    }
    
    @Test
    public void testUnicodeChars() {
        // Test con caracteres Unicode
        Fitxa fitxaUnicode = new Fitxa('\u03A9', 5); // Omega mayúscula
        assertEquals('\u03A9', fitxaUnicode.getLletra());
        assertEquals(5, fitxaUnicode.getValor());
    }
    
}