

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Dienstleistung;
import geschaeftsobjekte.Kunde;
import geschaeftsobjekte.Produkt;

import org.junit.Test;

public class TestVergleichVonObjekten02{

	/*
	 * equals-Methode auf Produkte auf Nr
	 */
	@Test
	public void testeProduktEquals(){
		Dienstleistung d1 = new Dienstleistung(1, "D", 1., "E");
		Dienstleistung d2 = new Dienstleistung(1, "DX", 2., "EX");
		Dienstleistung d3 = new Dienstleistung(2, "DX", 2., "EX");
		Artikel a1 = new Artikel(1, "A", 1.);
		Artikel a2 = new Artikel(1, "AX", 2.);
		Artikel a3 = new Artikel(2, "AX", 2.);
		Kunde k = new Kunde(1, "N", "S", "O");

		assertTrue("Zwei Artikel mit der selben Nr gelten auch dann als gleich, auch wenn ihre sonstigen Attribute nicht gleich sind (Primaerschluesselvergleich!)", a1.equals(a2));
		assertTrue("Zwei Dienstleistungen mit der selben Nr gelten auch dann als gleich, auch wenn ihre sonstigen Attribute nicht gleich sind (Primaerschluesselvergleich!)", d1.equals(d2));
		assertFalse("Ein Artikel und eine Dienstleistung koennen auch bei gleicher Nr nicht gleich sein!", a1.equals(d1));
		assertFalse("Ein Produkt kann nicht gleich einem (Geschaefts-)Objekt sein, das nicht selbst ein Produkt ist!", a1.equals(k));
		assertFalse("Zwei Artikel mit unterschiedlicher Nr koennen nicht gleich sein!", a2.equals(a3));
		assertFalse("Zwei Dienstleistungen mit unterschiedlicher Nr koennen nicht gleich sein!", d2.equals(d3));
	}

	/*
	 * Comparable<Produkt>
	 *  - Dienstleistung vor Artikel
	 *  - alphabetisch Bezeichnung
	 *  - null-Werte bei Bezeichnung
	 *  - Preis
	 */
	@Test
	public void testeProdukteComparable(){

		Dienstleistung d1 = new Dienstleistung(1, null, 1., "qm");
		Dienstleistung d2 = new Dienstleistung(2, null, 2., "am");
		Dienstleistung d3 = new Dienstleistung(3, "DienstA", 2., "qm");
		Dienstleistung d4 = new Dienstleistung(4, "DienstB", 2., "qm");
		Dienstleistung d5 = new Dienstleistung(5, "DienstB", 3., "qm");
		Artikel a1 = new Artikel(6, "ArtikelA", 2.);
		Artikel a2 = new Artikel(7, "ArtikelB", 2.);
		Artikel a3 = new Artikel(8, "ArtikelB", 3.);
		Artikel a4 = new Artikel(9, "ArtikelC", 4.);

		Produkt[] unsortiert = { a4, a3, a2, a1, d5, d4, d3, d2, d1 };
		Produkt[] sortiert   = { d1, d2, d3, d4, d5, a1, a2, a3, a4 };

		Arrays.sort(unsortiert);
		assertArrayEquals("Sortierreihenfolge von Produkten ist nicht korrekt! Pruefen Sie die Implementierung der Methode compareTo!", sortiert, unsortiert);
	}
}
