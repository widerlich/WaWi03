package gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import geschaeftsobjekte.Artikel;
import geschaeftsobjekte.Dienstleistung;
import geschaeftsobjekte.Kunde;
import geschaeftsobjekte.Produkt;
import geschaeftsobjekte.Rechnung;

import gui.posDialog.POSDialog;
import gui.posDialog.POSDialogCheckoutHandler;
import gui.posDialog.POSDialogCloseHandler;
import gui.posDialog.POSDialogKundenauswahlHandler;
import gui.posDialog.POSDialogProduktButtonHandler;

import gui.loginDialog.LoginDialog;
import gui.loginDialog.LoginHandler;

import gui.posDialog.ProduktButton;
import javafx.application.Application;
import javafx.stage.Stage;



/**
 * Startet die Dialoge der Anwendung und definiert f�r jeden Dialog
 * die Handler-Methoden (=Lambdas) f�r die Behandlung der Ereignisse des
 * Dialogs.
 * Tritt ein Ereignis auf, ruft der Dialog den hier definierten
 * Handler auf. Dadurch entsteht eine Trennung (Modularisierung) zwischen
 * den Klassen der Benutzeroberfl�che und der Ereignisbehandlung (diese Datei).
 * Vorteile sind die getrennte Testbarkeit der Ereignisbehandlung sowie
 * der minimierte Aufwand beim Umstieg auf eine andere UI-Technologie. Letzteres
 * ergibt sich daraus, dass die gesch�ftslogik-Klassen keine Abh�ngigkeiten
 * zum UI-Framework (hier: JavaFX) besitzen (siehe Imports in dieser Datei).
 *
 */
public class WaWiApp extends Application {
	/**
	 * Ben�tigte Daten: Produkte und Kunden
	 */
	private List<Produkt> produkte;
	private List<Kunde> kunden;
	private Rechnung re;

	// passwords from database
	private static final String USER_KASSE = "Hugo";
	private static final String PW_KASSE = "123";
	private static final String USER_LAGER = "Admin";
	private static final String PW_LAGER = "123";

	/**
	 * Dialoge der App
	 */
	private POSDialog posDialog;
	private LoginDialog loginDialog;
	private int status;

	static public void main(String[] args) {
		launch(args); // Anwendung starten
	}

	@Override
	public void start(Stage stage) throws Exception {
		produkte = initialisiereProdukte();
		kunden = initialisiereKunden();

		try{
			posDialog = new POSDialog(produkte, kunden);
			loginDialog = new LoginDialog();
			showLoginDialog();
			loginDialog.show();
		}catch(Exception e) {
            e.printStackTrace();
		}
	}

	/**
	 * Erzeugt eine Instanz von POSDialog, setzt f�r diese die ben�tigten
	 * Ereignis-Handler und zeigt den Dialog an.
	 * Aufgabenteilung:
	 * - POSDialog zeigt den Dialog an und erlaubt Eingaben
	 * - die Reaktion auf Eingaben wird *hier* vorgenommen,
	 *   indem entsprechende Lambda-Ausdr�cke definiert werden,
	 *   die zu den geforderten Interfaces passen:
	 * 		- Kundenauswahl (Interface POSDialogKundenauswahlHandler)
	 * 		- Checkoutbutton geklickt (Interface POSDialogCheckoutHandler)
	 * 		- "Dialog schliessen"-Button geklickt (Interface POSDialogCloseHandler)
	 * 		- Produktbutton geklickt (Interface POSDialogProduktButtonHandler)
	 *   Die Lambdas werden hier definiert und dem POSDialog bekannt gemacht (per Setter-Aufruf)
	 */
	private void showPOSDialog() {
		POSDialogCloseHandler closeHandler = () -> {
			posDialog.hide();
		};

		POSDialogKundenauswahlHandler kundenHandler = (Kunde k) -> {
			if(posDialog.getState() == AppStates.KUNDENAUSWAHL) {
				if(k.getNummer() == 0) {
					this.re = new Rechnung();
					posDialog.setLabel(re.toString());
					posDialog.setState(AppStates.PRODUKTAUSWAHL);
					posDialog.show();
				}
				else if(k.getNummer() > 0) {
					this.re = new Rechnung(k);
					posDialog.setLabel(re.toString());
					posDialog.setState(AppStates.PRODUKTAUSWAHL);
					posDialog.show();
				}
			}
		};

		POSDialogProduktButtonHandler produktHandler = (ProduktButton p) ->
		{
			Produkt prod = p.getProdukt();
			try {
				re.addRechnungsposition(1, prod);
			}catch(Exception e) {
				int x = re.getRechnungsposition(prod).getAnzahl();
				posDialog.disableButton(p, x);
				e.printStackTrace();
			}
			posDialog.setLabel(re.toString());
			posDialog.show();
		};

		POSDialogCheckoutHandler checkoutHandler = () ->
		{
			try {
				/*for (Produkt a : produkte) {
					System.out.println(a);
				}*/
				re.buchen();
				/*for (Produkt a : produkte) {
					System.out.println(a);
				}*/

			}catch(Exception e) {
	            e.printStackTrace();
			}
			posDialog.setState(AppStates.KUNDENAUSWAHL);
			posDialog.show();
		};

		posDialog.setCloseHandler(closeHandler);
		posDialog.setCheckoutHandler(checkoutHandler);
		posDialog.setKundenauswahlHandler(kundenHandler);
		posDialog.setProduktButtonHandler(produktHandler);

		posDialog.show();
	}

	// show login dialog
	public void showLoginDialog() {
		LoginHandler login = (String user , String pw) -> {

			if(user.trim().isEmpty())
			{
				loginDialog.resetUserPW();
				loginDialog.setError("Bitte geben Sie einen Benutzernamen ein!", true);
			}
			else if(pw.trim().isEmpty()){
				loginDialog.resetUserPW();
				loginDialog.setError("Bitte geben Sie ein Passwort ein!", true);
			}
			else if (user.equals(USER_KASSE) && pw.equals(PW_KASSE))
			{
				loginDialog.setError("POS Login korrekt", false);
				posDialog.setState(AppStates.KUNDENAUSWAHL);
				this.showPOSDialog();
			}
			else if (user.equals(USER_LAGER) && pw.equals(PW_LAGER)){
				loginDialog.setError("Lager-Login korrekt", false);
				//WaWiApp.showLagerDialog(); --> kommt noch
			}
			else{
				loginDialog = new LoginDialog();
				loginDialog.setError("Ungueltige Benutzername/Passwort-Kombination!", true);
			}

		};

		loginDialog.setLoginHandler(login);
		loginDialog.show();
	}

	/**
	 * Erzeugt und initialisiert eine Kundenliste und gibt diese zur�ck
	 * @return Kundenliste
	 */
	private List<Kunde> initialisiereKunden() {
		List<Kunde> kunden = new ArrayList<>();

		kunden.add(new Kunde(-1, "<bitte ausw�hlen>", "", ""));
		kunden.add(new Kunde(0, "Barverkauf", "", ""));
		kunden.add(new Kunde(1, "Madonna", "Sunset Boulevard 1", "Hollywood"));
		kunden.add(new Kunde(2, "Heidi Klum", "Modelwalk 13", "L.A."));

		return kunden;
	}

	/**
	 * Erzeugt und initialisiert eine Produktliste und gibt diese zur�ck
	 * @return Produktliste
	 */
	private List<Produkt> initialisiereProdukte() {
		List<Produkt> produkte = new LinkedList<>();

		Artikel p1 = new Artikel(12345, "Arbeitsplatte A1", 89.90);
		p1.einlagern(0);
		Dienstleistung p2 = new Dienstleistung(100, "K�chenmontage", 75., "h");
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
		Artikel p8 = new Artikel(12346, "Arbeitsplatte A2", 99.90);
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

		return produkte;
	}

}
