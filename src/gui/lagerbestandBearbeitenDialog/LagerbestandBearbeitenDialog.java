package gui.lagerbestandBearbeitenDialog;

import geschaeftsobjekte.Artikel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class LagerbestandBearbeitenDialog {
	
	private Stage stage;
	private LagerbestandBearbeitenDialogCloseHandler closeHandler;
	
	public LagerbestandBearbeitenDialog(Artikel a) {
		
		this.stage = new Stage();
		stage.setTitle("Lagerbestand bearbeiten");
		
		GridPane pane = new GridPane();
		ColumnConstraints colConst = new ColumnConstraints();
		RowConstraints rowConst = new RowConstraints();
		colConst.setPercentWidth(40.);
		rowConst.setPercentHeight(15.);
		colConst.setFillWidth(true);
		pane.getColumnConstraints().add(colConst);
		//pane.getRowConstraints().add(rowConst);
		
		Label nrLabel = new Label("Nummer");
		Label bezLabel = new Label("Bezeichnung");
		Label kBezLabel = new Label("Kurzbezeichnung");
		Label lagerAlt = new Label("alter Lagerbestand");
		Label lagerNeu = new Label("neuer Lagerbestand");
		nrLabel.setPadding(new Insets(10, 0, 0, 10));
		bezLabel.setPadding(new Insets(10, 0, 0, 10));
		kBezLabel.setPadding(new Insets(10, 0, 0, 10));
		lagerAlt.setPadding(new Insets(10, 0, 10, 10));
		lagerNeu.setPadding(new Insets(10, 0, 0, 10));
		pane.add(nrLabel, 0, 0);
		pane.add(bezLabel, 0, 1);
		pane.add(kBezLabel, 0, 2);
		pane.add(lagerAlt, 0, 3);
		pane.add(lagerNeu, 0, 4);
		
		Label thisNr = new Label(""+a.getNummer());
		Label thisBez = new Label(a.getBezeichnung());
		Label thisKBez = new Label(a.getKurzbezeichnung());
		Label thisBesAlt = new Label(""+a.getLagerbestand());
		TextField thisBesNeu = new TextField();
		thisNr.setPadding(new Insets(10, 0, 0, 10));
		thisBez.setPadding(new Insets(10, 0, 0, 10));
		thisKBez.setPadding(new Insets(10, 0, 0, 10));
		thisBesAlt.setPadding(new Insets(10, 0, 10, 10));
		
		pane.add(thisNr, 1, 0);
		pane.add(thisBez, 1, 1);
		pane.add(thisKBez, 1, 2);
		pane.add(thisBesAlt, 1, 3);
		pane.add(thisBesNeu, 1, 4);
		
		pane.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(pane, 400, 200);
		stage.setScene(scene);
		
		stage.setOnCloseRequest(e -> {
			int value = 0;
			try {
				value = Integer.parseInt(thisBesNeu.getText());
			}catch(Exception ex) {
				System.out.println("Eingabe falsch, nur ganze Zahlen erlaubt. Keine Änderung.");
			}
			if (!(value > 0)) {
				value = 0;
			}
			closeHandler.handleCloseEvent(value);
			hide();
		});
	}
	
	public void show() {
		stage.show();
	}
	
	public void hide() {
		stage.hide();
	}
	
	public void setCloseHandler(LagerbestandBearbeitenDialogCloseHandler c) {
		this.closeHandler = c;
	}

}
