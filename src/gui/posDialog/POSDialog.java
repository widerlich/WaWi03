package gui.posDialog;
import java.util.LinkedList;
import java.util.List;
import gui.AppStates;
import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Dienstleistung;
import geschaeftsobjekte.Kunde;
import geschaeftsobjekte.Produkt;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;

/**
 * Klasse POSDialog
 * Initialisiert und zeigt einen POS-Dialog zur Auswahl der Produkte (Dienstleistungen und 
 * Artikel) an. 
 */
public class POSDialog {
	
	private Stage stage;
	private POSDialogCloseHandler closeHandler;
	private POSDialogKundenauswahlHandler kundenauswahlHandler;
	private POSDialogCheckoutHandler checkoutHandler;
	private POSDialogProduktButtonHandler produktButtonHandler;
	
	// elements of gui to be set active / inactive
	private Button checkout;
	private Label rechnungsText;
	private ScrollPane anzeige;
	private ChoiceBox<String> wahlKunde;
	private List<ProduktButton> pOptions;
	
	// all possible options (database)
	private List<Kunde> kunden;
	private List<Produkt> produkte;
	
	// attributes for app
	private int myState;
	private Kunde myKunde;
	//private List<Produkt> myProducts;
	//private Produkt myProduct;
	
	public POSDialog(List<Produkt> produkte, List<Kunde> kunden) {
		this.kunden = kunden;
		this.produkte = produkte;
		
		stage = new Stage();
		stage.setWidth(820.);
		stage.setHeight(700.);
		stage.initModality(Modality.APPLICATION_MODAL);
		initDialog();
	}

	/**
	 * Initialisiert den Dialog, d.h.
	 * - erzeugt die benötigten Panes und UI Controls
	 * - kümmert sich um das Layout, d.h. verbindet die Panes und Controls entsprechend
	 * - installiert die von aussen (der App-Klasse) gesetzen Methoden für die Ereignisbehandlung
	 */
	private void initDialog() {
		
		stage.setTitle("Kasse");
		GridPane pane = new GridPane();
		pane.setGridLinesVisible(false);
		
		//Layout Design
		ColumnConstraints colConst = new ColumnConstraints();
		RowConstraints rowConst = new RowConstraints();
		colConst.setPercentWidth(45.);
		rowConst.setPercentHeight(100.);
		colConst.setFillWidth(true);
		pane.getColumnConstraints().add(colConst);
		pane.getRowConstraints().add(rowConst);
		
		// left side
		// choicebox
		ChoiceBox<String> auswahl = new ChoiceBox<String>();
		auswahl.prefWidthProperty().bind(pane.widthProperty());
		GridPane.setHalignment(auswahl, HPos.CENTER);
		GridPane.setValignment(auswahl, VPos.TOP);
		this.wahlKunde = auswahl;
		setDefaultKunde();
		
		// label
		ScrollPane scrollable = new ScrollPane();
		Label lab = new Label();
		lab.setFont(new Font("Courier New", 14));
		//lab.setText("Test\n");		
		scrollable.setPadding(new Insets(30, 10, 30, 10));
		lab.prefWidthProperty().bind(scrollable.widthProperty());
		lab.setWrapText(true);
		scrollable.setContent(lab);
		scrollable.prefWidthProperty().bind(pane.widthProperty());
		GridPane.setHalignment(scrollable, HPos.LEFT);
		GridPane.setValignment(scrollable, VPos.CENTER);
		this.anzeige = scrollable;
		this.rechnungsText = lab;
		
		
		// checkout button
		Button but = new Button("CHECKOUT");
		but.prefWidthProperty().bind(pane.widthProperty());
		GridPane.setHalignment(but, HPos.CENTER);
		GridPane.setValignment(but, VPos.BOTTOM);
		this.checkout = but;
		
		// right side
		FlowPane vb = new FlowPane();
		vb.prefWidthProperty().bind(pane.widthProperty());
		vb.setPadding(new Insets(20,20,20,20));
		vb.setHgap(20);
		vb.setVgap(20);
		
		int size = produkte.size();
		produkte.sort((p1, p2) -> p1.compareTo(p2));
		pOptions = new LinkedList<>();
		for(int i = 0; i < size; i++) {
			pOptions.add(new ProduktButton(produkte.get(i)));
		}
		vb.getChildren().addAll(pOptions);
		GridPane.setHalignment(vb, HPos.CENTER);
		GridPane.setValignment(vb, VPos.CENTER);
		
		// add elements to pane
		pane.add(vb, 1,0);
		pane.getChildren().add(anzeige);
		pane.getChildren().add(wahlKunde);
		pane.getChildren().add(checkout);
		
		// add pane to stage
		Scene scene = new Scene(pane, 820, 700);
		stage.setScene(scene);	
		
		// set all handlers
		stage.setOnCloseRequest(e -> {
			closeHandler.handleCloseEvent();
		});
		
		but.setOnAction(e -> {
			checkoutHandler.handleCheckout();
		});
		
		auswahl.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			for (Kunde k : kunden) {
				if(k.getName().equals(newV)){
					kundenauswahlHandler.kundeAusgewaehlt(k);
				}
			}
			
			});
		
		for (ProduktButton b : pOptions) {
			b.setOnAction(e -> {
			produktButtonHandler.handleClick(b);
			});
		}
		
		this.myState = AppStates.KUNDENAUSWAHL;
		
	}
	
	/**
	 * Zeigt den Dialog an
	 */
	public void show() {
		
		switch(myState) {
			case AppStates.KUNDENAUSWAHL: {
				wahlKunde.setDisable(false);
				checkout.setDisable(true);
				anzeige.setDisable(true);
				for (ProduktButton p : pOptions) {
					p.setDisable(true);
				}
			} break;
			
			case AppStates.PRODUKTAUSWAHL: {
				wahlKunde.setDisable(true);
				checkout.setDisable(false);
				anzeige.setDisable(false);
				for (ProduktButton p : pOptions) {
					if(checkLagerbestand(p.getProdukt()))
						p.setDisable(false);
					else
						p.setDisable(true);
				}
			} break;
			
			default: {
				wahlKunde.setDisable(false);
				setDefaultKunde(); // reset in "bitte auswählen"
				checkout.setDisable(true);
				anzeige.setDisable(true);
				for (ProduktButton p : pOptions) {
					p.setDisable(true);
				}
			} break;
		}
		
		stage.show();
		
	}

	/**
	 * Verbirgt den Dialog
	 */
	public void hide() {
		// reset all fields
		this.initDialog();
	}	
	
	
	// getters and setters
	
	public Stage getStage() {
		return this.stage;
	}
	
	public void setKundenauswahlHandler(POSDialogKundenauswahlHandler k) {
		this.kundenauswahlHandler = k;
	}
	
	public void setCheckoutHandler(POSDialogCheckoutHandler c) {
		this.checkoutHandler = c;
	}
	
	public void setProduktButtonHandler(POSDialogProduktButtonHandler p) {
		this.produktButtonHandler = p;
	}
	
	public void setCloseHandler(POSDialogCloseHandler c) {
		this.closeHandler = c;
	}
	
	public Kunde getKunde() {
		return myKunde;
	}
	
	public int getState() {
		return myState;
	}
	
	public void setState(int s) {
		this.myState = s;
	}
	
	public void setLabel(String s) {
		this.rechnungsText.setText(s);
		this.anzeige.setContent(rechnungsText);
	}
	
	
	// helper methods
	
	private void setDefaultKunde() {
		for (Kunde k : kunden) {
			wahlKunde.getItems().add(k.getName());
			if(k.getNummer() == -1) {
				//set default
				wahlKunde.setValue(k.getName());
			}
		}
	}
	
	private boolean checkLagerbestand(Produkt p) {
		if(p instanceof Artikel && ((Artikel) p).getLagerbestand() == 0)
			return false;
		else
			return true;
	}
	
}


