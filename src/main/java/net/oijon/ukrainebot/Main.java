package net.oijon.ukrainebot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	s.close();
    	
    	Boolean hasAllKeys = hasAllKeys(properties);
    	
    	Stage stage = new Stage();
    	stage.setTitle("UkraineBot Panel");
    	
    	if (hasAllKeys) {
    		
    	} else {
    		
    	}
    	
    	stage.show();
		
	}
}
