package gui.loginDialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginDialog {
	
	// references to GUI elements
	private Stage stage;
	private TextField userField;
	private PasswordField pwField;
	private Label errorText;
	
	private LoginHandler loginHandler;
	
	public LoginDialog()
	{
		this.stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		initDialog();
	}

	private void initDialog()
	{	
		// set pane layout
		stage.setTitle("WaWiLogin");
		BorderPane loginPane = new BorderPane();
		loginPane.setPadding(new Insets(20, 40, 10, 30));
		
		// add user-related fields
		FlowPane fpU = new FlowPane();
		Label userLabel = new Label("Benutzername:");
		userLabel.setPadding(new Insets(10, 10, 10, 10));
		userField = new TextField();
		fpU.getChildren().addAll(userLabel, userField);
		fpU.setAlignment(Pos.CENTER);
		loginPane.setTop(fpU);
		
		// add password-related fields
		FlowPane fpP = new FlowPane();
		Label pwLabel = new Label("        Passwort:");
		pwLabel.setPadding(new Insets(10, 10, 10, 10));
		pwField = new PasswordField();
		fpP.getChildren().addAll(pwLabel, pwField);
		fpP.setAlignment(Pos.CENTER);
		loginPane.setCenter(fpP);
		fpP.setPadding(new Insets(0, 30, 0, 30));
		
		// add error label and button
		VBox fpB = new VBox();
		fpB.setPadding(new Insets(5, 0, 0, 60));
		
		this.errorText = new Label();
		//errorText.setText("Test");
		errorText.setTextAlignment(TextAlignment.CENTER);
		errorText.setPadding(new Insets(10, 10, 10, 10));
		
		Button loginButton = new Button("Login");
		loginButton.setPadding(new Insets(10, 10, 10, 10));
		loginButton.setMinWidth(250);
		
		fpB.getChildren().addAll(errorText, loginButton);
		fpB.setAlignment(Pos.CENTER);
		loginPane.setBottom(fpB);		
		
		stage.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(loginPane);
		stage.setWidth(400);
		stage.setScene(scene);
		show();
		
		// add listener for input evalutation
		
		loginButton.setOnAction(e ->
		{
			try {
				loginHandler.checkLogin(userField.getText(), pwField.getText());
			}catch(Exception ex) {
				if(userField.getText() != null)
					loginHandler.checkLogin(userField.getText(), " ");
				else
					loginHandler.checkLogin(" ", pwField.getText());
			}
		});
	}
	
	// getters and setters
	
	public void setLoginHandler(LoginHandler h){
		this.loginHandler = h;
	}
	
	public void setError(String s, boolean error) {
		if(error) {
			errorText.setTextFill(Color.web("red"));
		}else {
			errorText.setTextFill(Color.web("black"));
		}
		this.errorText.setText(s);
	}
	
	public void resetUserPW() {
		this.pwField.clear();
		this.userField.clear();
	}
	
	
	// show and hide operations
	
	public void show(){
		stage.show();
	}
	
	public void hide() {
		resetUserPW();
		setError("", false);
		stage.hide();
	}

}
