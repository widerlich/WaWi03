package gui.posDialog;
import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Produkt;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProduktButton extends Button{
	private Produkt p;
	private final String IMAGE_PATH = "/pic/";
	
	public ProduktButton(Produkt p){
		super(p.getBezeichnung());
		this.p = p;
		
		try {
			Image icon = null;
			if (p instanceof Artikel){
				String filename = IMAGE_PATH + ((Artikel)p).getKurzbezeichnung() + ".jpg";
				icon = new Image(filename);
			} else {
				icon = new Image(IMAGE_PATH + "generic_service.jpg");
			}
			setGraphic(new ImageView(icon));
			setContentDisplay(ContentDisplay.TOP);
			setPrefSize(100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Produkt getProdukt(){
		return p;
	}
}
