package loginDialog;

@FunctionalInterface
public interface LoginHandler {
	void checkLogin(String benutzername, String passwort);

}