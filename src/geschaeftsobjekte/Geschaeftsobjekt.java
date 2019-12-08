package geschaeftsobjekte;
import java.io.*;

public abstract class Geschaeftsobjekt implements Serializable {
	final int nummer;
	
	public Geschaeftsobjekt(int n){
		nummer = n;
	}
	
	public int getNummer() {
		return nummer;
	}
	
}
