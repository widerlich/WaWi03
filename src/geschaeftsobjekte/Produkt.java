package geschaeftsobjekte;
import java.io.*;
import java.util.*;

abstract public class Produkt extends Geschaeftsobjekt implements Comparable<Produkt>{
	String name;
	double preis;
	
	Produkt (int no, String na, double p){
		super(no);
		name = na;
		preis = p;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if (!(o instanceof Produkt)) { 
            return false; 
        }
		// System.out.println(this.getClass());
		if(this.getClass().equals(o.getClass()))
			if(this.getNummer() == ((Geschaeftsobjekt) o).getNummer())
				return true;
		return false;
	}

	@Override
	public int compareTo(Produkt that){
		if(this.getClass().equals(that.getClass())) {
			
			if(this.getBezeichnung() == null || that.getBezeichnung() == null) {
				if(this.getBezeichnung() == null && that.getBezeichnung() == null) {
					return Double.compare(this.getPreis(), that.getPreis());
				}
				else if (this.getBezeichnung() == null) {
					return -1;
				}
				else {
					return 1;
				}
			}
			
			if(this.getBezeichnung().equals(that.getBezeichnung())) {
				return Double.compare(this.getPreis(), that.getPreis());
			}
			
			return this.getBezeichnung().compareTo(that.getBezeichnung());
		}
		
		else {
			if(this instanceof Artikel) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}

	public String getBezeichnung() {
		return name;
	}
	
	public void setBezeichnung(String n) {
		this.name = n;
	}
	
	public double getPreis() {
		return preis;
	}
	
	public void setPreis(double n) {
		this.preis = n;
	}
	
	public static List<Produkt> loadProducts(String SERIALIZATION_PATH){
		List<Produkt> produkte = new LinkedList<>();
		try {
			// oeffnet die Datei "data/Produkt.ser" zum lesen
			InputStream ins = new FileInputStream(SERIALIZATION_PATH);
			ObjectInputStream objin = new ObjectInputStream(ins);
			// als erstes Objekt wird ein Integer-Objekt gelesen,
			// das angibt, wie viele Produkt-Objekte kommen werden
			int i=objin.readInt();
			int z=0;
			while(z++ < i){
				// lese das nächste Produkt-Objekt aus der Datei
				Produkt p = (Produkt) objin.readObject();
				produkte.add(p); // und füge es der Liste hinzu
			}
			// die Datei muss wieder geschlossen/freigegeben werden
			objin.close();
			ins.close();
			} catch (Exception e) {
				e.printStackTrace(); // im Fehlerfall Fehlermeldung ausgeben
			}
		return produkte; // Liste der Produkte zurueckgeben
		}

}
