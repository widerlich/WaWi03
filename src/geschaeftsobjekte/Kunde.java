package geschaeftsobjekte;


public class Kunde extends Geschaeftsobjekt {
	String name;
	String ort;
	String strasse;
	
	public Kunde (int no, String na, String s, String o){
		super(no);
		name = na;
		ort = o;
		strasse = s;
	}
	
	@Override public String toString() {
		String s = "Kunde: " + super.nummer + "\n"; 
		s += this.name + "\n" + this.strasse +"\n" + this.ort + "\n";
		return s;
	}

	public String getName() {
		return name;
	}
	
	public String getStrasse() {
		return strasse;
	}
	
	public String getOrt() {
		return ort;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void setStrasse(String n) {
		this.strasse = n;
	}
	
	public void setOrt(String n) {
		this.ort = n;
	}
}
