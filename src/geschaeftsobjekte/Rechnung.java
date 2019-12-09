package geschaeftsobjekte;
import java.util.*;
import exceptions.*;

public class Rechnung extends Geschaeftsobjekt {
	static int number = 0;
	Rechnungsstatus status = Rechnungsstatus.IN_ERSTELLUNG;
	Kunde kunde = null;
	List <Rechnungsposition> positionen = new LinkedList<>();	
	
	public Rechnung() {
		super(number+1);
		number += 1;
	}
	
	public Rechnung(Kunde k) {
		super(number+1);
		number +=1;
		kunde = k;
	}
	
	@Override public String toString() {
		String s = "Rechnung: ";
		s += s.format("%d\n", this.number);
		
		if(kunde != null) {
			s+= kunde.toString() + "\n";
		}
		else {
			s+= "Barverkauf\n\n";
		}
		
		int i = 0;
		Iterator<Rechnungsposition> iterator = this.positionen.iterator();
		while (i < positionen.size()) {	
			s+=positionen.get(i)+"\n";
			i += 1;
		}
		
		s+= "---------------------------------------\n";
		s+= s.format(Locale.GERMANY, "%34.2f EURO", this.getGesamtpreis());
		
		return s;
	}
	
	public Rechnungsposition addRechnungsposition(int anzahl, Produkt p) throws OutOfStockException, BookingException{		
		
		if(this.status != Rechnungsstatus.IN_ERSTELLUNG) {
			throw new BookingException("Rechnungsstatus nicht \"IN_ERSTELLUNG\"");
		}		
		
		if(p instanceof Artikel){
			if(((Artikel) p).getLagerbestand() >= anzahl) {
				Rechnungsposition pos = new Rechnungsposition(anzahl, p);
			
				//Liste ist leer
				if(positionen.isEmpty()) {
					this.positionen.add(pos);
					return pos;
				}
				
				//Liste ist nicht leer
				for(int i = 0; i < positionen.size(); i++) {
					if(positionen.get(i).getProdukt() instanceof Artikel) {	
						Artikel art = (Artikel)(positionen.get(i).getProdukt());
						if(p.equals(art)){
							int menge = positionen.get(i).getAnzahl();
							if(art.getLagerbestand() >= menge+anzahl) {
								positionen.get(i).setAnzahl(menge+anzahl);
							}
							else {
								throw new OutOfStockException("Kein ausreichender Lagerbestand von " + p, p);
							}
							return positionen.get(i);
						}
					}
				}
				positionen.add(pos);
				return pos;
			}
			else {
				throw new OutOfStockException("Kein ausreichender Lagerbestand von " + p, p);
			}
		}
		
		if(p instanceof Dienstleistung) {
			for(int i = 0; i < positionen.size(); i++) {
				if(p.equals(positionen.get(i).getProdukt())) {
					int menge = positionen.get(i).getAnzahl();
					positionen.get(i).setAnzahl(menge+anzahl);
					return positionen.get(i);
				}
			}
			Rechnungsposition pos = new Rechnungsposition(anzahl, p);
			positionen.add(pos);
			return pos;
		}
		
		return null;
	}
	
	public double getGesamtpreis() {
		double p = 0.;
		Iterator<Rechnungsposition> iterator = this.positionen.iterator();
		while (iterator.hasNext()) {
			p += iterator.next().getPreis();
		}
		return p;
	}
	
	public Rechnungsposition getRechnungsposition(Produkt p) {
		int i = 0;
		Iterator<Rechnungsposition> iterator = this.positionen.iterator();
		while (iterator.hasNext()) {	
			if(p.equals(iterator.next().getProdukt())){
				return positionen.get(i);
			}
			i += 1;
		}
		return new Rechnungsposition(0, p);
	}
	
	public int getAnzahlRechnungspositionen() {
		return positionen.size();
	}
	
	public List<Rechnungsposition> getRechnungspositionen(){
		return positionen;
	}
	
	public void buchen() throws BookingException, OutOfStockException{
		if(this.status != Rechnungsstatus.IN_ERSTELLUNG) {
			throw new BookingException("Rechnungsstatus nicht \"IN_ERSTELLUNG\"");
		}
		this.status = Rechnungsstatus.GEBUCHT;
		Iterator<Rechnungsposition> iterator = this.positionen.iterator();
		while (iterator.hasNext()) {
			Rechnungsposition p = iterator.next();
			if(p.getProdukt() instanceof Artikel) {
				Artikel a = (Artikel)p.getProdukt();
				a.auslagern(p.getAnzahl());
			}
		}		
	}

	public static int getNumber() {
		return number;
	}

	public Rechnungsstatus getRechnungsstatus() {
		return status;
	}

	public Kunde getKunde() {
		return kunde;
	}
}
