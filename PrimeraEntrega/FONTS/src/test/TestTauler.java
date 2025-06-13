
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Tauler;
import model.Fitxa;
import model.Casella;
import util.Pair;

public class TestTauler {
    private Tauler tauler;

    @Before
    public void setUp() {
        tauler = new Tauler(15);
    }

    @Test
    public void testColocarParaulaHoritzontalment() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa('G', 2));
        fitxes.add(new Fitxa('A', 1));
        fitxes.add(new Fitxa('T', 1));

        Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fitxes, 7, 7, true);
        List<Pair<Integer, Integer>> posicions = resultat.first;

        assertEquals(3, posicions.size());

        assertEquals('G', tauler.getCasella(7, 7).getFitxa().getLletra());
        assertEquals('A', tauler.getCasella(7, 8).getFitxa().getLletra());
        assertEquals('T', tauler.getCasella(7, 9).getFitxa().getLletra());
    }

    @Test
    public void testColocarParaulaSenseEspai() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa('H', 4));
        fitxes.add(new Fitxa('I', 1));
        fitxes.add(new Fitxa('J', 8));

        Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fitxes, 14, 13, true);
        List<Pair<Integer, Integer>> posicions = resultat.first;

        assertEquals(0, posicions.size());
    }

    @Test
    public void testColocarParaulaVerticalSenseEspai() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa('A', 1));
        fitxes.add(new Fitxa('B', 3));
        fitxes.add(new Fitxa('C', 3));

        Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fitxes, 13, 7, false);
        List<Pair<Integer, Integer>> posicions = resultat.first;

        assertEquals(0, posicions.size());
    }

    @Test
    public void testColocarParaulaEnCasillasOcupadas() {
        List<Fitxa> fitxes1 = new ArrayList<>();
        fitxes1.add(new Fitxa('C', 3));
        fitxes1.add(new Fitxa('A', 1));
        fitxes1.add(new Fitxa('S', 1));
        fitxes1.add(new Fitxa('A', 1));

        tauler.colocarParaula(fitxes1, 7, 7, true);

        List<Fitxa> fitxes2 = new ArrayList<>();
        fitxes2.add(new Fitxa('S', 1));
        fitxes2.add(new Fitxa('O', 1));
        fitxes2.add(new Fitxa('L', 1));

        Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fitxes2, 7, 8, false);
        List<Pair<Integer, Integer>> posicions = resultat.first;

        assertEquals(0, posicions.size());

        assertEquals('C', tauler.getCasella(7, 7).getFitxa().getLletra());
        assertEquals('A', tauler.getCasella(7, 8).getFitxa().getLletra());
        assertEquals('S', tauler.getCasella(7, 9).getFitxa().getLletra());
        assertEquals('A', tauler.getCasella(7, 10).getFitxa().getLletra());
    }

    @Test
    public void testPosicionesCorrectas() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa('P', 3));
        fitxes.add(new Fitxa('A', 1));
        fitxes.add(new Fitxa('Z', 10));

        Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fitxes, 5, 5, true);
        List<Pair<Integer, Integer>> posicions = resultat.first;

        assertEquals(3, posicions.size());

        assertEquals(Integer.valueOf(5), posicions.get(0).first);
        assertEquals(Integer.valueOf(5), posicions.get(0).second);

        assertEquals(Integer.valueOf(5), posicions.get(1).first);
        assertEquals(Integer.valueOf(6), posicions.get(1).second);

        assertEquals(Integer.valueOf(5), posicions.get(2).first);
        assertEquals(Integer.valueOf(7), posicions.get(2).second);
    }
}
