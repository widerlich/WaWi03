
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.junit.Test;

import geschaeftsobjekte.Artikel;

public class TestArtikelWaWi01 {
	private String[] nichtErlaubteMethoden = { "setNummer", "setKurzbezeichnung", 	"setLagerbestand" };
	private String[] geforderteMethoden = { "getNummer", "getBezeichnung",	"getKurzbezeichnung", "getLagerbestand", 
			"setBezeichnung",	"einlagern", "auslagern" };

	@Test
	public void testeReadOnlyAttribute() {
		Class artikel = Artikel.class;
		Method[] methods = artikel.getMethods();

		// Setter trotz read-only?
		for (Method m : methods) {
			for (String pfui : nichtErlaubteMethoden)
				if (m.getName().equals(pfui))
					fail("Setter "
							+ pfui
							+ " nicht erlaubt, Attribut ist read-only (Aufgabenstellung exakt beachten!)");
		}

	}

	@Test
	public void testVollstaendigkeitDerMethoden() {
		Class artikel = Artikel.class;
		Method[] methods = artikel.getMethods();

		for (String name : geforderteMethoden) {
			boolean vorhanden = false;
			for (Method m : methods) {
				if (m.getName().equals(name)) {
					vorhanden = true;
					break;
				}
			}
			if (vorhanden == false)
				fail("Methode " + name + " fehlt");
		}
	}

	@Test
	public void testePrivateAttribute() {
		// Default-Regel: Attribute haben so restriktive Sichtbarkeit wie
		// möglich --> hier: alle private
		for (Field attribut : Artikel.class.getDeclaredFields()) {
			if (!Modifier.isPrivate(attribut.getModifiers()))
				fail("Attribut "
						+ attribut.getName()
						+ " ist nicht private (so restriktiv wie möglich --> hier private!)");
		}
	}

	@Test
	public void testKonstruktor() {
		Constructor[] constuctors = Artikel.class.getConstructors();
		for (Constructor c : constuctors) {
			Class[] types = c.getParameterTypes();
			if (types.length == 0)
				fail("Default-Konstruktor ohne Parameter für Artikel nicht erlaubt, siehe Aufgabenstellung");
			if (types.length == 1)
				fail("Konstruktor muss mind. Nummer und Bezeichnung als Parameter erhalten, weniger nicht erlaubt, siehe Aufgabenstellung");
		}
	}

	@Test
	public void testeKurzbezeichnung() {
		HashMap<Artikel, String> testStrings = new HashMap<>();
		testStrings.put(new Artikel(123, "Torx-Schrauben 6x35", 1.),			"163001236");
		testStrings.put(new Artikel(8, "Spax 8x55", 1.), 						"069400087");
		testStrings.put(new Artikel(62873408, "Schloßschraube", 1.), 			"135034084");
		testStrings.put(new Artikel(1, "A", 1.), 								"006500012");
		testStrings.put(new Artikel(1, "a", 1.), 								"009700017");
		testStrings.put(new Artikel(12, "a", 1.), 								"009700129");
		testStrings.put(new Artikel(123, "a", 1.), 								"009701232");
		testStrings.put(new Artikel(1234, "a", 1.), 							"009712346");
		testStrings.put(new Artikel(12345, "a", 1.), 							"009723450");
		testStrings.put(new Artikel(123456, "a", 1.), 							"009734564");
		testStrings.put(new Artikel(1, "", 1.), 								"000000011");
		testStrings.put(new Artikel(1, "X", 1.), 								"008800017");
		testStrings.put(new Artikel(1, "x", 1.), 								"012000014");
		testStrings.put(new Artikel(1, "XAX", 1.), 								"024100018"); 
		testStrings.put(new Artikel(1, "xax", 1.), 								"033700014");
		testStrings.put(new Artikel(1, "AXA", 1.), 								"021800012");
		testStrings.put(new Artikel(1, "axa", 1.), 								"031400019");
		testStrings.put(new Artikel(1, "AAAAAAAAAABCEFGHJKLMNPQRSTVWXYZ", 1.), 	"229500019");
		testStrings.put(new Artikel(1, "aaaaaaaaaabcefghjklmnpqrstvwxyz", 1.), 	"328700011");
		testStrings.put(new Artikel(1, "!\"Â§$%&/()=?BCEFGHJK", 1.), 			"056400016");
		testStrings.put(new Artikel(2109, "0987654321", 1.), 					"052521094");

		// Methode erzeugeKurzbezeichnung testen
		for (Artikel a : testStrings.keySet()) {
			assertEquals(
					"Falsche Kurzbezeichnung zu geg. (Lang-)Bezeichnung bei direktem Test von erzeugeKurzbezeichnung",
					testStrings.get(a),
					Artikel.erzeugeKurzbezeichnung(a.getNummer(),
							a.getBezeichnung()));
		}

		// Artikel-Objekte nach Erzeugung testen
		for (Artikel a : testStrings.keySet()) {
			assertEquals(
					"Falsche Kurzbezeichnung zu geg. (Lang-)Bezeichnung bei Test von Artikel-Objekt",
					testStrings.get(a), a.getKurzbezeichnung());
		}

		// setBezeichnung --> auch Kurzbezeichnung geändert?
		Artikel last = null;
		for (Artikel a : testStrings.keySet()) {
			if (last == null) {
				last = a;
				continue;
			}
			last.setBezeichnung(a.getBezeichnung());
			assertEquals(
					"setBezeichnung(...) muss auch Kurzbezeichnung veraendern",
					Artikel.erzeugeKurzbezeichnung(last.getNummer(),
							a.getBezeichnung()), last.getKurzbezeichnung());
			last = a;
		}

	}

	@Test
	public void testeEinlagernAuslagern() {
		Artikel test = new Artikel(1, "Blub", 1.);
		test.einlagern(10);
		assertEquals(
				"Lagerbestand wird beim Einlagern nicht korrekt verbucht (0 vorher --> einlagern(10) --> muss 10 sein",
				10, test.getLagerbestand());
		test.einlagern(5);
		assertEquals(
				"Lagerbestand wird beim Einlagern nicht korrekt verbucht (10 vorher --> einlagern(5) --> muss 15 sein",
				15, test.getLagerbestand());
		test.auslagern(12);
		assertEquals(
				"Lagerbestand wird beim Auslagern nicht korrekt verbucht (15 vorher --> auslagern(12) --> muss 3 sein",
				3, test.getLagerbestand());
		test.auslagern(4);
		assertEquals(
				"Bei nicht ausreichendem Lagerbestand findet beim Auslagern keine Veraenderung statt (3 vorher --> auslagern(4) --> muss 3 bleiben",
				3, test.getLagerbestand());
	}

	@Test
	public void testToString() {
		HashMap<Artikel, String> testStrings = new HashMap<>();
		testStrings.put(new Artikel(123, "Torx-Schrauben 6x35", 1.),
				"TRXSCHRB01230");
		testStrings.put(new Artikel(8, "Spax 8x55", 1.), "SPX8X550008X");
		testStrings.put(new Artikel(62873408, "SchloÃŸschraube", 1.),
				"SCHLSSCH3408X");

		for (Artikel a : testStrings.keySet()) {
			// a.einlagern(((int)Math.random()*1000) + 1);
			a.einlagern(10);
			String korrekt = "" + a.getNummer() + ", " + a.getKurzbezeichnung()
					+ ", " + a.getBezeichnung() + ", " + a.getLagerbestand()
					+ " auf Lager";
			assertEquals(
					"toString() erzeugt falschen String, Aufgabenstellung (Beispiel) beachten!",
					korrekt, a.toString());
		}
	}

}
