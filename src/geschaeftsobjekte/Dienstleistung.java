package geschaeftsobjekte;


public class Dienstleistung extends Produkt {
	
	private String kurzbezeichnung;
	private String einheit;
	
	public Dienstleistung(int n, String bez, double p, String unit){
		super(n, bez, p);
		einheit = unit;
	}
	
	public int getNummer() {
		return this.nummer;
	}
	
	public String getKurzbezeichnung() {
		return this.kurzbezeichnung;
	}
	
	public String getEinheit() {
		return einheit;
	}
	
	public void setEinheit(String n) {
		this.einheit = n;
	}
	
}
