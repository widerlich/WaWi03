package gui.posDialog;

import geschaeftsobjekte.Kunde;

@FunctionalInterface
public interface POSDialogKundenauswahlHandler {
	void kundeAusgewaehlt(Kunde kunde);
}
