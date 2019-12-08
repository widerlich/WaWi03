

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
/**
 * Hilfsklasse - enthält statische Methoden, die mithilfe von Java-Reflection arbeiten.
 * @author windisch
 */
public class Utilities {
	/**
	 * Liefert true zurück, wenn name in c definiert ist.
	 * @param c Klasse
	 * @param name Methodenname
	 */
	public static boolean containsMethodName(Class c, String name){
		Method[] methods = c.getMethods();

		for(Method m : methods){
			if(m.getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Prüft,ob eine Methode namens "name" mit den in "types" gegebenen Parametern ein der Klasse "c" definiert ist.
	 * @param c Klasse
	 * @param name Name der gesuchten Methode
	 * @param types Parametertypen der Methode
	 * @return gefundene Methode
	 * @throws RuntimeException wird geworfen, falls die Methode nicht gefunden wurde
	 */
	public static Method getMethod(Class<?> c, String name, Class[] types) throws RuntimeException{
		Method[] methods = c.getMethods();

		for(Method m : methods){
			if(m.getName().equals(name))
				if(types == null || types.length==0){
					return m;
				}
				else if(Arrays.equals(types, m.getParameterTypes()))
					return m;
		}
		throw new RuntimeException("Methode " + name + " mit gesuchten Parametern nicht gefunden");
	}

	/**
	 * Liefert true, falls Klasse "klasse" einen Setter für den Datentyp "c" definiert.  
	 * @param klasse
	 * @param c
	 * @return
	 */
	public static boolean hasSetterForType(Class<?> klasse, Class<?> c){
		Method[] methods = klasse.getMethods();

		for(Method m : methods){
			if(m.getName().startsWith("set")){
				Class<?>[] types = m.getParameterTypes();
				if(types.length != 1)
					return false;
				if(types[0].equals(c))
					return true;
			}
		}
		return false;
	}
	
	public static String removeChar(String s, char c){
		StringBuffer buf = new StringBuffer(s);
		while(s.indexOf(c) >= 0){
			buf.deleteCharAt(s.indexOf(c));
			s=buf.toString();
		}
		return s;
	}

}
