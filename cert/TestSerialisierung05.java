

import static org.junit.Assert.*;
import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Dienstleistung;
import geschaeftsobjekte.Produkt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSerialisierung05{

	private final String path = "data/Produkt.ser";
	private List<Produkt> produkte;

	@Before
	public void init(){
		// Objekte erzeugen und ausgeben
		produkte = new LinkedList<>();
		Artikel p1 = new Artikel(12345, "Arbeitsplatte", 89.90);
		p1.einlagern(1);
		Dienstleistung p2 = new Dienstleistung(100, "Kuechenmontage", 75., "h");
		Artikel p3 = new Artikel(98989876, "Akku-Handsauger", 129.90);
		p3.einlagern(3);
		Artikel p4 = new Artikel(5261, "Spax 6x100", 3.99);
		p4.einlagern(4);
		Artikel p5 = new Artikel(4593, "Coca Cola 12x1l", 12.69); 
		p5.einlagern(5);
		Artikel p6 = new Artikel(4594, "Capri-Sonne", 8.99);
		p6.einlagern(6);
		Artikel p7 = new Artikel(4595, "Jever Partyfass 5l", 8.99);
		p7.einlagern(7);
		Artikel p8 = new Artikel(12346, "Arbeitsplatte", 99.90);
		p8.einlagern(1);
		Artikel p9 = new Artikel(526, "Laminatbodenpack", 13.99);
		p9.einlagern(5);
		Dienstleistung p10 = new Dienstleistung(123, "Parkettmontage", 75.00, "qm");
		Dienstleistung p11 = new Dienstleistung(128, "Montage Sockelleisten", 5.59, "lfdm");

		produkte.add(p1);
		produkte.add(p2);
		produkte.add(p3);
		produkte.add(p4);
		produkte.add(p5);
		produkte.add(p6);
		produkte.add(p7);
		produkte.add(p8);
		produkte.add(p9);
		produkte.add(p10);
		produkte.add(p11);

		try {
			OutputStream outs = new FileOutputStream(path);
			ObjectOutputStream objout = new ObjectOutputStream(outs);
			objout.writeInt(produkte.size());
			for(Produkt p : produkte){
				objout.writeObject(p);
			}
			objout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGeschaeftsobjekt(){
		List<Produkt> lesen = Produkt.loadProducts(path);
		assertArrayEquals("Produkte werden nicht korrekt gelade ueber Produkt.loadProducts!", produkte.toArray(), lesen.toArray());
	}

	@After
	public void restore(){
		try{
			File file = new File(path);
			if(file.exists())
				file.delete();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
