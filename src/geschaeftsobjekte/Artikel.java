package geschaeftsobjekte;
import exceptions.*;

public class Artikel extends Produkt{
	private String kurzbezeichnung;
	private int lagerbestand;
			
	public Artikel(int nr, String name, double p){
		super(nr, name, p);
		kurzbezeichnung = erzeugeKurzbezeichnung(this.nummer, this.name);
		lagerbestand = 0;
	}
	
	public int getNummer() {
		return this.nummer;
	}
	
	public String getBezeichnung() {
		return this.name;
	}
	
	public String getKurzbezeichnung() {
		return this.kurzbezeichnung;
	}
	
	public int getLagerbestand() {
		return this.lagerbestand;
	}
	
	public void setBezeichnung(String name) {
		this.name = name;
		//Kurzbezeichnung muss ebenfalls geändert werden
		this.kurzbezeichnung = erzeugeKurzbezeichnung(this.nummer, this.name);
	}
	
	public void einlagern(int diff) {
		this.lagerbestand += diff;
	}
	
	public void auslagern(int diff) throws OutOfStockException{
		if(this.lagerbestand >= diff) {
			this.lagerbestand -= diff;
		}
		else
			throw new OutOfStockException("Kein ausreichender Lagerbestand von " + this.name, this);
	}
	
	public static String erzeugeKurzbezeichnung(int nummer, String bezeichnung) {
		// Umwandlung Bezeichnung in Ziffernfolge
		int summe = 0;
		
		String kurzb = bezeichnung.replaceAll("[^\\w\\s]", "").replaceAll(" ", "");
		
		int i = 0;
		for(i = 0; i<kurzb.length();i++) {
			summe += kurzb.charAt(i);
		}
		summe %= 10000;
		
		Integer summeInt = new Integer(summe);
		String finaleSumme = summeInt.toString();
		int laengeSumme = finaleSumme.length();
		for(i = 4; i>laengeSumme; i--) {
			finaleSumme = "0"+finaleSumme;
		}
		
		//Umwandlung Artikelnummer in Ziffernfolge
		nummer %= 10000;
		Integer nummerInt = new Integer(nummer);
		String finaleNummer = nummerInt.toString();
		int laengeNummer = finaleNummer.length();
		for(i = 4; i>laengeNummer; i--) {
			finaleNummer = "0"+finaleNummer;
		}
		
		String bisher = finaleSumme+finaleNummer;
		
		//Pruefziffer
		int pruefziffer = 0;
		Integer pruefzifferInt = new Integer(bisher);
		pruefziffer = pruefzifferInt.intValue();
		int trueZiffer = 0;
		for(i = 0; i<bisher.length();i++) {
			trueZiffer += pruefziffer%10;
			pruefziffer /= 10;
		}
		trueZiffer %= 10;
		Integer trueZifferInt = new Integer(trueZiffer);
		//System.out.println(bisher+trueZifferInt.toString());
		return bisher+trueZifferInt.toString();
	}
	
	@Override public String toString(){
        String s = "";
        Integer num = new Integer(this.getNummer());
        Integer bes = new Integer(this.getLagerbestand());
        
        s +=  num + ", " +
        		this.getKurzbezeichnung() + ", " +
        		this.getBezeichnung() + ", " +
        		bes + " auf Lager";
        return s;
		/*String korrekt = "" + this.getNummer() + ", " + this.getKurzbezeichnung()
		+ ", " + this.getBezeichnung() + ", " + this.getLagerbestand()
		+ " auf Lager";
		return korrekt;*/
    }

	public static void main(String args[]){
		// Objekte erzeugen und ausgeben
		Artikel a = new Artikel(123, "Torx-Schrauben 6x35", 1.99);
		System.out.println(a);
		Artikel b = new Artikel(8, "Spax 8x55", 1.10);
		b.einlagern(17);
		System.out.println(b);
		// Erzeugen der Kurzbezeichnung testen
		System.out.println(erzeugeKurzbezeichnung(62873408, "Schloßschraube"));
	}
}