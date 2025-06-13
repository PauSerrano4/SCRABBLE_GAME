import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import model.Casella;
import model.Fitxa;



public class TestCasella {
    
    @Test
    public void testConstructor() {
        // Test constructor con valores válidos
        Casella casella = new Casella(3, 4, "DL");
        assertEquals(3, casella.getFila());
        assertEquals(4, casella.getColumna());
        assertEquals("DL", casella.getMultiplicador());
        assertFalse(casella.teFitxa());
        assertNull(casella.getFitxa());
        
        // Test constructor con coordenadas cero
        Casella casellaCero = new Casella(0, 0, "TP");
        assertEquals(0, casellaCero.getFila());
        assertEquals(0, casellaCero.getColumna());
        assertEquals("TP", casellaCero.getMultiplicador());
        
        // Test constructor con coordenadas negativas
        Casella casellaNegativa = new Casella(-1, -2, "");
        assertEquals(-1, casellaNegativa.getFila());
        assertEquals(-2, casellaNegativa.getColumna());
        assertEquals("", casellaNegativa.getMultiplicador());
        
        // Test constructor con multiplicador vacío
        Casella casellaSinMultiplicador = new Casella(5, 6, "");
        assertEquals("", casellaSinMultiplicador.getMultiplicador());
    }
    
    @Test
    public void testGetFila() {
        Casella casella = new Casella(7, 8, "TL");
        assertEquals(7, casella.getFila());
        
        Casella casellaLimite = new Casella(Integer.MAX_VALUE, 0, "");
        assertEquals(Integer.MAX_VALUE, casellaLimite.getFila());
    }
    
    @Test
    public void testGetColumna() {
        Casella casella = new Casella(9, 10, "DP");
        assertEquals(10, casella.getColumna());
        
        Casella casellaLimite = new Casella(0, Integer.MIN_VALUE, "");
        assertEquals(Integer.MIN_VALUE, casellaLimite.getColumna());
    }
    
    @Test
    public void testGetMultiplicador() {
        // Test multiplicador DL
        Casella casellaDL = new Casella(1, 2, "DL");
        assertEquals("DL", casellaDL.getMultiplicador());
        
        // Test multiplicador TL
        Casella casellaTL = new Casella(3, 4, "TL");
        assertEquals("TL", casellaTL.getMultiplicador());
        
        // Test multiplicador DP
        Casella casellaDP = new Casella(5, 6, "DP");
        assertEquals("DP", casellaDP.getMultiplicador());
        
        // Test multiplicador TP
        Casella casellaTP = new Casella(7, 8, "TP");
        assertEquals("TP", casellaTP.getMultiplicador());
        
        // Test multiplicador C (centro)
        Casella casellaC = new Casella(7, 7, "C");
        assertEquals("C", casellaC.getMultiplicador());
        
        // Test sin multiplicador
        Casella casellaSinMulti = new Casella(9, 10, "");
        assertEquals("", casellaSinMulti.getMultiplicador());
        
        // Test con multiplicador inválido (asumiendo que se acepta cualquier string)
        Casella casellaMultiInvalido = new Casella(11, 12, "XX");
        assertEquals("XX", casellaMultiInvalido.getMultiplicador());
    }
    
    @Test
    public void testColocarFitxa() {
        Casella casella = new Casella(1, 2, "DP");
        Fitxa fitxa = new Fitxa('A', 1);
        
        // Colocar ficha en casilla vacía
        boolean resultado = casella.colocarFitxa(fitxa);
        
        assertTrue(resultado);
        assertTrue(casella.teFitxa());
        assertNotNull(casella.getFitxa());
        assertEquals('A', casella.getFitxa().getLletra());
        assertEquals(1, casella.getFitxa().getValor());
    }
    
    @Test
    public void testColocarFitxaEnCasellaOcupada() {
        Casella casella = new Casella(3, 4, "TL");
        Fitxa fitxa1 = new Fitxa('B', 3);
        Fitxa fitxa2 = new Fitxa('C', 3);
        
        // Colocar primera ficha
        assertTrue(casella.colocarFitxa(fitxa1));
        
        // Intentar colocar segunda ficha
        boolean resultado = casella.colocarFitxa(fitxa2);
        
        assertFalse(resultado);
        assertEquals('B', casella.getFitxa().getLletra()); // Mantiene la primera ficha
    }
    
    @Test
    public void testTeFitxa() {
        Casella casella = new Casella(5, 6, "");
        
        // Sin ficha inicialmente
        assertFalse(casella.teFitxa());
        
        // Después de colocar ficha
        casella.colocarFitxa(new Fitxa('D', 2));
        assertTrue(casella.teFitxa());
        
        // Después de quitar ficha
        casella.treureFitxa();
        assertFalse(casella.teFitxa());
    }
    
    @Test
    public void testGetFitxa() {
        Casella casella = new Casella(7, 8, "DL");
        
        // Sin ficha inicialmente
        assertNull(casella.getFitxa());
        
        // Después de colocar ficha
        Fitxa fitxa = new Fitxa('E', 1);
        casella.colocarFitxa(fitxa);
        assertNotNull(casella.getFitxa());
        assertEquals('E', casella.getFitxa().getLletra());
        assertEquals(1, casella.getFitxa().getValor());
    }
    
    @Test
    public void testTreureFitxa() {
        Casella casella = new Casella(9, 10, "TP");
        Fitxa fitxa = new Fitxa('F', 4);
        
        // Colocar ficha
        casella.colocarFitxa(fitxa);
        
        // Quitar ficha
        Fitxa fitxaRetornada = casella.treureFitxa();
        
        assertNotNull(fitxaRetornada);
        assertEquals('F', fitxaRetornada.getLletra());
        assertEquals(4, fitxaRetornada.getValor());
        assertFalse(casella.teFitxa());
        assertNull(casella.getFitxa());
    }
    
    @Test
    public void testTreureFitxaEnCasellaVacia() {
        Casella casella = new Casella(11, 12, "");
        
        // Intentar quitar ficha de casilla vacía
        Fitxa fitxaRetornada = casella.treureFitxa();
        
        assertNull(fitxaRetornada);
        assertFalse(casella.teFitxa());
    }
    
    @Test
    public void testFitxaComodin() {
        Casella casella = new Casella(1, 1, "DL");
        Fitxa fitxaComodin = new Fitxa('*', 0);
        
        // Colocar comodín
        boolean resultado = casella.colocarFitxa(fitxaComodin);
        
        assertTrue(resultado);
        assertTrue(casella.teFitxa());
        assertEquals('*', casella.getFitxa().getLletra());
        assertEquals(0, casella.getFitxa().getValor());
    }
    
    @Test
    public void testMultiplicadorNoAfectadoPorFitxa() {
        Casella casella = new Casella(2, 2, "DP");
        
        // Verificar multiplicador inicial
        assertEquals("DP", casella.getMultiplicador());
        
        // Colocar ficha
        casella.colocarFitxa(new Fitxa('G', 2));
        
        // Verificar que el multiplicador no cambia
        assertEquals("DP", casella.getMultiplicador());
        
        // Quitar ficha
        casella.treureFitxa();
        
        // Verificar que el multiplicador sigue sin cambiar
        assertEquals("DP", casella.getMultiplicador());
    }
    
    @Test
    public void testOperacionesMultiples() {
        Casella casella = new Casella(3, 3, "TL");
        
        // Estado inicial
        assertFalse(casella.teFitxa());
        
        // Primera operación - colocar ficha
        Fitxa fitxa1 = new Fitxa('H', 4);
        assertTrue(casella.colocarFitxa(fitxa1));
        assertTrue(casella.teFitxa());
        assertEquals('H', casella.getFitxa().getLletra());
        
        // Segunda operación - quitar ficha
        Fitxa fitxaQuitada = casella.treureFitxa();
        assertFalse(casella.teFitxa());
        assertEquals('H', fitxaQuitada.getLletra());
        assertEquals(4, fitxaQuitada.getValor());
        
        // Tercera operación - colocar otra ficha
        Fitxa fitxa2 = new Fitxa('I', 1);
        assertTrue(casella.colocarFitxa(fitxa2));
        assertTrue(casella.teFitxa());
        assertEquals('I', casella.getFitxa().getLletra());
        assertEquals(1, casella.getFitxa().getValor());
    }
    
    @Test
    public void testColocarQuitarMultiples() {
        Casella casella = new Casella(4, 4, "");
        
        // Ciclo de operaciones repetidas
        for (int i = 0; i < 5; i++) {
            Fitxa fitxa = new Fitxa((char)('A' + i), i + 1);
            
            // Antes de colocar
            assertFalse(casella.teFitxa());
            
            // Colocar
            assertTrue(casella.colocarFitxa(fitxa));
            assertTrue(casella.teFitxa());
            assertEquals((char)('A' + i), casella.getFitxa().getLletra());
            
            // Quitar
            Fitxa quitada = casella.treureFitxa();
            assertFalse(casella.teFitxa());
            assertEquals((char)('A' + i), quitada.getLletra());
            assertEquals(i + 1, quitada.getValor());
        }
    }
    
}