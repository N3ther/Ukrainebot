package net.oijon.ukrainebot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Hello world!
 *
 */
public class Main extends Application
{
	/*
	 * Checks if all keys have been supplied. Used later to open a popup.
	 */
	public static Boolean hasAllKeys(Properties p) {
		Boolean hasAllKeys = null;
		
		String ibmKey = p.getProperty("ibm-key");
    	String discordKey = p.getProperty("discord-key");
    	
    	if (ibmKey != null) {
    		if (discordKey != null) {
    			hasAllKeys = true;
    		}
    	} else {
    		hasAllKeys = false;
    	}
		return hasAllKeys;
	}
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		/*
    	 * loads properties
    	 * 
    	 * this class.getClassLoader() thing is needed because the config file is technically not
    	 * in the build path (at least to my understanding), and as such this makes sure it
    	 * actually loads.
    	 */
    	System.out.println("Looking for config.properties in config.properties");
    	InputStream s = Main.class.getClassLoader().getResourceAsStream("config.properties");
    	Properties properties = new Properties();
    	try {
			properties.load(s);
			System.out.println("---PROPERTIES---");
			System.out.println("ibm-key - " + properties.get("ibm-key"));
			System.out.println("discord-key - " + properties.get("discord-key"));
			System.out.println("---END PROPERTIES---");
		} catch (IOException e) {
			System.err.println("[CRITICAL] " + e.toString());
			e.printStackTrace();
		}
    	
    	s.close();
    	
    	Boolean hasAllKeys = hasAllKeys(properties);
    	
    	primaryStage.setTitle("UkraineBot Panel");
    	Button btn = new Button();
    	btn.setText("Configure Keys");
    	btn.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event) {
                 final Stage dialog = new Stage();
                 dialog.initModality(Modality.APPLICATION_MODAL);
                 dialog.initOwner(primaryStage);
                 dialog.setTitle("Insert Keys");
                 
                 GridPane grid = new GridPane();
                 grid.setAlignment(Pos.CENTER);
                 grid.setHgap(10);
                 grid.setVgap(10);
                 grid.setPadding(new Insets(25, 25, 25, 25));
                 
                 Text scenetitle = new Text("Insert Keys");
                 scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                 grid.add(scenetitle, 0, 0, 2, 1);

                 Label discordKey = new Label("Discord Bot Token:");
                 grid.add(discordKey, 0, 1);

                 TextField discordTextField = new TextField();
                 grid.add(discordTextField, 1, 1);

                 Label ibmKey = new Label("IBM Cloud Key:");
                 grid.add(ibmKey, 0, 2);

                 TextField ibmTextField = new TextField();
                 grid.add(ibmTextField, 1, 2);
                 
                 Scene dialogScene = new Scene(grid, 350, 220);
                 dialog.setScene(dialogScene);
                 dialog.show();
                 
                 Button btn = new Button("Set Keys");
                 HBox hbBtn = new HBox(10);
                 hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                 hbBtn.getChildren().add(btn);
                 grid.add(hbBtn, 1, 4);
                 
                 
             }
         });
         
         StackPane root = new StackPane();
         root.getChildren().add(btn);
         primaryStage.setScene(new Scene(root, 853, 480));
         
    	//if (hasAllKeys) {
    		
    	//} else {
    		
    	//}
    	
    	primaryStage.show();
		
	}
}
