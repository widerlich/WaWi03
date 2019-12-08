package exceptions;
import geschaeftsobjekte.Produkt;

public class OutOfStockException extends Exception {
	private Produkt produkt;
	
	public OutOfStockException(String s, Produkt p){
		super(s);
		this.produkt = p;
	}
	
	public Produkt getProdukt() {
		return this.produkt;
	}
}
