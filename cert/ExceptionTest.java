

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

import exceptions.BookingException;
import exceptions.OutOfStockException;
import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Rechnung;
import geschaeftsobjekte.Rechnungsstatus;

public class ExceptionTest {
	@Test
	public void testBookingException() {
		// teste Vererbung
		Class<BookingException> bookingExceptionClass = BookingException.class;
		String name1 = Exception.class.getName();
		String name2 = bookingExceptionClass.getSuperclass().getName();
		assertEquals("BookingException erbt nicht von Exception!",
				name1, name2);

		// teste Konstruktor
		Constructor<?>[] constructors = BookingException.class.getConstructors();
		assertEquals("BookingException sollte genau einen Konstruktor haben!",  
				1, constructors.length);
		Constructor<?> c = constructors[0];
		Class<?>[] types = c.getParameterTypes();
		if (types.length != 1 || !types[0].getName().equals("java.lang.String"))
			fail("BookingException-Konstruktor muss genau einen String-Parameter fuer eine Nachricht erhalten!");
	}
	
	@Test
	public void testOutOfStockException() {
		// teste Vererbung
		Class<OutOfStockException> oosClass = OutOfStockException.class;
		String name1 = Exception.class.getName();
		String name2 = oosClass.getSuperclass().getName();
		assertEquals("OutOfStockException erbt nicht von Exception!",
				name1, name2);

		// teste Konstruktor
		Constructor<?>[] constructors = OutOfStockException.class.getConstructors();
		assertEquals("OutOfStockException sollte genau einen Konstruktor haben!",  
				1, constructors.length);
		Constructor<?> c = constructors[0];
		Class<?>[] types = c.getParameterTypes();
		if (types.length != 2)
			fail("OutOfStockException-Konstruktor muss einen Produkt-Parameter und einen String-Parameter erhalten!");
		if (!(types[0].getName().equals("geschaeftsobjekte.Produkt") && 
			  types[1].getName().equals("java.lang.String")) && 
			!(types[1].getName().equals("geschaeftsobjekte.Produkt") && 
			  types[0].getName().equals("java.lang.String")))
			fail("OutOfStockException-Konstruktor muss einen Produkt-Parameter und einen String-Parameter erhalten!");

		// teste auf private Attribut vom Typ Produkt
		Field[] fields = OutOfStockException.class.getDeclaredFields();
		assertEquals("OutOfStockException sollte genau ein privates Attribut vom Typ Produkt haben!",  
				1, fields.length);
		if (!Modifier.isPrivate(fields[0].getModifiers())) {
			fail("Attribut " + fields[0].getName() + " ist nicht private (so restriktiv wie moeglich --> hier private!)");
		}
		if (!fields[0].getType().getName().equals("geschaeftsobjekte.Produkt")) {
			fail("Attribut " + fields[0].getName() + " hat den falschen Typ!");
		}
		
		// prüfe, ob es einen Getter für das Produkt-Attribut gibt
		String produktAttributName = fields[0].getName();
		String getterName = "get" + String.valueOf(produktAttributName.charAt(0)).toUpperCase();
		if (produktAttributName.length() > 1)
			getterName += produktAttributName.substring(1);
		try {
			Method getter = OutOfStockException.class.getMethod(getterName);
		} catch (NoSuchMethodException e) {
			fail("Kein Getter für das Produkt-Attribut '" + produktAttributName + "' definiert.");
		}
	}
	
	@Test
	public void testAuslagern() {
		Artikel a = new Artikel(1, "Testartikel", 12.0);

		try {
			a.einlagern(20);
			
			a.auslagern(10);
			a.auslagern(10);
			a.auslagern(10); // sollte Exception werfen
			fail("Keine Exception trotz fehlendem Lagerbestands!");
		} catch (Exception e) {
			if (!(e instanceof OutOfStockException)) {
				fail("Falscher Exceptiontyp beim Auslagern!");
			}
			try {
				a.einlagern(10);
				a.auslagern(10); // sollte ok sein
			} catch (OutOfStockException e1) {
				fail("Exception trotz ausreichenden Lagerbestands!");
			}
		}
		
		Rechnung r = new Rechnung(null);
		try {
			a.einlagern(1);
			r.addRechnungsposition(1, a);
		} catch (OutOfStockException | BookingException e) {
			fail("Exception trotz ausreichendem Lagerbestands!");
		}
		try {
			a.auslagern(1);
			r.addRechnungsposition(1, a);
			fail("Keine Exception trotz fehlendem Lagerbestands!");
		} catch (OutOfStockException | BookingException e) {
		}
	}
	
	@Test
	public void testStatus() {
		Rechnung r = new Rechnung(null);

		try {
			// Status auf gebucht setzen	  	    
			Artikel a = new Artikel(1, "Testartikel", 12.0);
			a.einlagern(1);
	  	    r.addRechnungsposition(1,  a);
	  	    r.buchen();
	  	    if (r.getRechnungsstatus() != Rechnungsstatus.GEBUCHT)
	  	    	fail("Rechnungsstatus sollten nach buchen()-Aufruf GEBUCHT sein!");
			a.einlagern(1);
	  	    r.addRechnungsposition(1,  a);
	  	    fail("addRechnungsposition() sollte eine BookingException werfen, wenn der Rechnungsstatus nicht IN_ERSTELLUNG ist!");
		} catch (BookingException | OutOfStockException e) {
		}		

		try {
			r.buchen();
	  	    fail("buchen() sollte eine BookingException werfen, wenn der Rechnungsstatus nicht IN_ERSTELLUNG ist!");
		} catch (BookingException | OutOfStockException e) {
		}
	}
}
