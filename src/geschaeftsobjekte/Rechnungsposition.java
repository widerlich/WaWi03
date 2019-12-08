package geschaeftsobjekte;

import java.util.Locale;

public class Rechnungsposition{
		Produkt produkt;
		int anzahl;
		double preis;
		
		public Rechnungsposition (int no, Produkt p){
			anzahl = no;
			produkt = p;
			preis = no * p.getPreis();
		}
		
		public double getPreis() {
			return preis;
		}
		
		public int getAnzahl() {
			return anzahl;
		}
		
		public Produkt getProdukt() {
			return produkt;
		}
		
		public void setAnzahl(int anz) {
			this.anzahl = anz;
			this.preis = anz * this.produkt.getPreis();
		}
		
		@Override public String toString(){
	        String s = "";
	        //s += s.format("%-34s\n", produkt.getBezeichnung());
	        s += produkt.getBezeichnung();
	        if(s.length() > 34)
	        	s = s.substring(0, 34);
	        s += s.format("\n%4d", anzahl);
	        if(produkt instanceof Dienstleistung && ((Dienstleistung) produkt).getEinheit().length() > 3) {
	        	s += s.format(" %-3s x", ((Dienstleistung)produkt).getEinheit().substring(0, 3));
	        } else if(produkt instanceof Dienstleistung && ((Dienstleistung) produkt).getEinheit().length() <= 3){
	        	s += s.format(" %-3s x", ((Dienstleistung)produkt).getEinheit());
	        } else {
	        	s  += s.format("%6s", "x");
	        }
	        s += s.format(Locale.GERMAN, "%9.2f = %12.2f EURO", produkt.getPreis(), this.getPreis());
	        return s;
	    }
		
		public static void main (String[]args) {
			Rechnungsposition r = new Rechnungsposition(999, new Dienstleistung(2, "Dienstleistungsbezeichnungdiesuperlangeistfastschonzulange", 12345.12, "lfdm"));
			System.out.println(r);
		}
}
