
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Dienstleistung;
import geschaeftsobjekte.Geschaeftsobjekt;
import geschaeftsobjekte.Kunde;
import geschaeftsobjekte.Produkt;

public class TestNeueKlassenstruktur01 {

	/*
	 * Geschaeftsobjekt
	 */
	@Test
	public void testeGeschaeftsobjekt(){
		Class<Geschaeftsobjekt> go = Geschaeftsobjekt.class;
		// abstract?
		if(!Modifier.isAbstract(go.getModifiers()))
			fail("Von Klasse Geschaeftsobjekt darf es keine Instanzen geben (Klasse darf nicht konkret sein)!");
		// Default-Konstruktor?
		Constructor<?>[] ctors = go.getConstructors();
		if(ctors.length != 1)
			fail("Klasse Geschaeftsobjekt darf nur einen public-Konstruktor haben!");
		// Konstruktor-Parameter int?
		if(ctors[0].getParameterTypes().length != 1)
			fail("Der Konstruktor der Klasse Geschaeftsobjekt darf nur einen Parameter haben!");
		String ctorParameterType = ctors[0].getParameterTypes()[0].getName();
		if(! (ctorParameterType.equals("int") || ctorParameterType.equals("long") || ctorParameterType.equals("java.lang.Integer") || ctorParameterType.equals("java.lang.Long")))
			fail("Der Konstruktor der Klasse Geschaeftsobjekt muss die Nr (als ganze Zahl) beim Erzeugen setzen!");
		if (Utilities.containsMethodName(Geschaeftsobjekt.class, "setNr"))
			fail("Die Nr eines Geschaeftsobjekts darf nach Objekterzeugung nicht mehr veraendert werden (read-only)!");
		if (Utilities.containsMethodName(Geschaeftsobjekt.class, "setNummer"))
			fail("Die Nr eines Geschaeftsobjekts darf nach Objekterzeugung nicht mehr veraendert werden (read-only)!");
		if (!Utilities.containsMethodName(Geschaeftsobjekt.class, "getNummer"))
			fail("Die Klasse Geschaeftsobjekt hat keinen Getter fuer das Attribut \"Nummer\" (getNummer() fehlt)!");

	}

	/*
	 * Kunde
	 */
	@Test
	public void testeKunde(){
		String[] required = {"getName", "getStrasse", "getOrt", "getNummer", "setName", "setStrasse", "setOrt"};
		for(String method : required)
			if(!Utilities.containsMethodName(Kunde.class, method))
				fail("Die Klasse Kunde hat keine Methode \"" + method + "\"!");
		// Default-Konstruktor?
		Constructor<?>[] ctors = Kunde.class.getConstructors();
		if(ctors.length != 1)
			fail("Klasse Kunde darf nur einen public-Konstruktor haben!");
		int zahl=0, string=0;
		for(Class<?> c : ctors[0].getParameterTypes()){
			if(c.getName().equals("int") || c.getName().equals("long") || c.getName().equals("java.lang.Integer") || c.getName().equals("java.lang.Long"))
				zahl++;
			if(c.getName().equals("java.lang.String"))
				string++;
		}
		if(zahl != 1)
			fail("Konstruktor der Klasse Kunde muss genau einen ganzzahligen Parameter (Nr) haben!");
		if(string != 3)
			fail("Konstruktor der Klasse Kunde muss genau drei String-Parameter fuer Name, Strasse und Ort haben!");
		if(! Kunde.class.getSuperclass().getName().equals("geschaeftsobjekte.Geschaeftsobjekt") )
			fail("Ein Kunde IST ein Geschaeftsobjekt! Auf korrekte Vererbung achten!");
	}

	/*
	 * Produkt
	 */
	@Test
	public void testeProdukt(){
		String[] required = {"getBezeichnung", "setBezeichnung", "getPreis", "setPreis"};
		for(String method : required)
			if(!Utilities.containsMethodName(Produkt.class, method))
				fail("Die Klasse Produkt hat keine Methode \"" + method + "\"!");
		if(! Produkt.class.getSuperclass().getName().equals("geschaeftsobjekte.Geschaeftsobjekt") )
			fail("Ein Produkt IST ein Geschaeftsobjekt! Auf korrekte Vererbung achten!");
		if(!Modifier.isAbstract(Produkt.class.getModifiers()))
			fail("Von Klasse Produkt darf es keine Instanzen geben (Produkte sind ENTWEDER Artikel ODER Dienstleistung)! Klasse darf nicht konkret sein!");

		Artikel a = new Artikel(12345, "Schraube", 3.99);
		assertEquals("Fehlerhafte Implementierung von getBezeichnung() der Klasse Produkt (nach Erzeugung eines neuen Artikels)", "Schraube", a.getBezeichnung());
		assertEquals("Initialer Lagerbestand eines neuen Artikels nicht 0", 0, a.getLagerbestand());
		assertEquals("Fehlerhafte Implementierung von getNummer() der Klasse Produkt (nach Erzeugung eines neuen Artikels)", 12345, a.getNummer());
		assertTrue("Fehlerhafte Implementierung von getPreis() der Klasse Produkt (nach Erzeugung eines neuen Artikels)", new Double(3.99).equals(a.getPreis()));
		a.setBezeichnung("Schraubenschluessel");
		assertEquals("Fehlerhafte Implementierung von setBezeichnung() der Klasse Produkt (nach Werteaenderung durch Setter an einem Artikel)", "Schraubenschluessel", a.getBezeichnung());
		a.setPreis(4.99);
		assertTrue("Fehlerhafte Implementierung von setPreis() der Klasse Produkt (nach Preisaenderung an einem Artikel)", new Double(4.99).equals(a.getPreis()));

	}

	/*
	 * Artikel
	 */
	@Test
	public void testeArtikel(){
		// Vererbungsbeziehung?
		if(! Artikel.class.getSuperclass().getName().equals("geschaeftsobjekte.Produkt") )
			fail("Ein Artikel IST ein Produkt! Auf korrekte Vererbung achten!");

		// Default-Konstruktor?
		Constructor<?>[] ctors = Artikel.class.getConstructors();
		for(Constructor<?> c : ctors){
			Class<?>[] parameters = c.getParameterTypes();
			assertTrue("Artikel koennen nur dann angelegt werden, wenn alle drei Attribute gegeben sind! Achten Sie auf die Anzahl der Parameter Ihres Konstruktors", parameters.length >= 3);
		}

	}

	/*
	 * Dienstleistung:
	 *  - get Einheit
	 *  - set Einheit
	 *  - kein default CTOR
	 */
	@Test
	public void testeDienstleistung(){
		// Vererbungsbeziehung?
		if(! Dienstleistung.class.getSuperclass().getName().equals("geschaeftsobjekte.Produkt") )
			fail("Eine Dienstleistung IST ein Produkt! Auf korrekte Vererbung achten!");

		// Default-Konstruktor?
		Constructor<?>[] ctors = Dienstleistung.class.getConstructors();
		for(Constructor<?> c : ctors){
			Class<?>[] parameters = c.getParameterTypes();
			assertTrue("Dienstleistungen koennen nur dann angelegt werden, wenn alle vier Attribute gegeben sind! Achten Sie auf die Anzahl der Parameter Ihres Konstruktors", parameters.length >= 4);
		}

		Dienstleistung d = new Dienstleistung(1, "DL", 20.10, "qm");
		assertEquals("Fehlerhafte Implementierung von getEinheit() der Klasse Dienstleistung (nach Objekterzeugung)", "qm", d.getEinheit());
		d.setEinheit("lfdm");
		assertEquals("Fehlerhafte Implementierung von getEinheit() der Klasse Dienstleistung (nach Werteaenderung durch Setter)", "lfdm", d.getEinheit());

	}
	
}
