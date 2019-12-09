package gui.lagerbestaendeSichtenDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Produkt;
// import gui.lagerbestandBearbeitenDialog.LagerbestandBearbeitenDialog; --> to be done
import gui.lagerbestandBearbeitenDialog.LagerbestandBearbeitenDialogCloseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LagerbestaendeSichtenDialog {
	private Stage stage;
	private TableView<Artikel> tableView;
	private LagerbestaendeSichtenDialogCloseHandler closeHandler;

	public LagerbestaendeSichtenDialog(List<Produkt> produkte) {
		// hier bitte eigenen Code einfügen
		
		List<Artikel> artikel = null; // bitte anpassen!
		Node tabelle = initTabelle(artikel);

		// hier bitte eigenen Code einfügen
		
	}
	
	public void setCloseHandler(LagerbestaendeSichtenDialogCloseHandler handler) {
		this.closeHandler = handler;
	}
	
	public void show() {
		stage.showAndWait();
	}
	
	public void hide() {
		stage.hide();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Erzeugt eine TableView und füllt sie mit Daten aus der übergebenen Artikel-Liste.
	 * Setzt einen Handler für das Doppelklick-Event auf einer Zeile
	 * 
	 * @param artikel Anzuzeigende Artikel-Liste
	 * @return Erzeugte und befüllte TableView
	 */
	private Node initTabelle(List<Artikel> artikel) {
		/*
		 * Artikel-Daten in observable array list setzen
		 */
		ObservableList<Artikel> liste = FXCollections.observableArrayList(artikel);
		
		/*
		 * TableView erzeugen und Daten setzen
		 */
		tableView = new TableView<>(liste);

		/*
		 * Spalten definieren und der tableView bekannt machen
		 */
        TableColumn<Artikel, Integer> nummerColumn = new TableColumn<>("Nummer");
        nummerColumn.setCellValueFactory(new PropertyValueFactory<Artikel, Integer>("nummer"));
        TableColumn<Artikel, String> bezeichnungColumn = new TableColumn<>("Bezeichnung");
		bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<Artikel, String>("bezeichnung"));
		TableColumn<Artikel, String> kurzbezeichnungColumn = new TableColumn<>("Kurzbezeichnung");
		kurzbezeichnungColumn.setCellValueFactory(new PropertyValueFactory<Artikel, String>("kurzbezeichnung"));
		TableColumn<Artikel, Integer> lagerbestandColumn = new TableColumn<>("Lagerbestand");
		lagerbestandColumn.setCellValueFactory(new PropertyValueFactory<Artikel, Integer>("lagerbestand"));
		tableView.getColumns().addAll(nummerColumn, bezeichnungColumn, kurzbezeichnungColumn, lagerbestandColumn);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        /**
         * Handler für Doppelklick-Event auf Tabellenzeile
         * Hier sollte showLagerbestandBearbeitenDialog() aufgerufen werden
         */
        tableView.setRowFactory( tv -> {
            TableRow<Artikel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {

                	// hier bitte eigenen Code einfügen
            		
                }
            });
            return row ;
        });
		return tableView;
	}

	/**
	 * Ruft den Lagerstand-Bearbeiten-Dialog auf.
	 * Zuvor wird ein Close-Handler definiert, der den neuen Lagerstand im Artikel-Array setzt
	 * und die Tabelle aktualisiert (hierfür tableView.refresh() aufrufen)
	 *
	 * @param selektierterArtikel Artikel, dessen Lagerbestand aktualisiert werden soll
	 */
	private void showLagerbestandBearbeitenDialog(Artikel selektierterArtikel) {
		// hier bitte eigenen Code einfügen
		
	}
}
