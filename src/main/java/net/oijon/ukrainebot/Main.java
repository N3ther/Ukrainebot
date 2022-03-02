package net.oijon.ukrainebot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Hello world!
 *
 */
public class Main extends Application {
    /*
     * Checks if all keys have been supplied. Used later to open a popup.
     */
    public void createKeyPopup(Stage primaryStage, Properties properties) {
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
        discordTextField.setText(properties.getProperty("discord-key"));
        grid.add(discordTextField, 1, 1);


        Label ibmKey = new Label("IBM Cloud Key:");
        grid.add(ibmKey, 0, 2);

        TextField ibmTextField = new TextField();
        ibmTextField.setText(properties.getProperty("ibm-key"));
        grid.add(ibmTextField, 1, 2);

        Scene dialogScene = new Scene(grid, 350, 220);
        dialog.setScene(dialogScene);
        dialog.show();

        Button btn = new Button("Set Keys");
        btn.setOnAction(new EventHandler < ActionEvent > () {

            @Override
            public void handle(ActionEvent event) {
                try (BufferedReader br = new BufferedReader(new FileReader(getAppdata() + "/config.properties"))) {
                    properties.load(br);
                    properties.setProperty("discord-key", discordTextField.getText());
                    properties.setProperty("ibm-key", ibmTextField.getText());
                    System.out.println("---SET PROPERTIES---");
                    System.out.println("ibm-key - " + properties.get("ibm-key"));
                    System.out.println("discord-key - " + properties.get("discord-key"));
                    System.out.println("---END PROPERTIES---");
                    FileOutputStream fos = new FileOutputStream(getAppdata() + "/config.properties");
                    properties.store(fos, null);
                    fos.close();
                } catch (IOException e) {
                    System.err.println("[ERROR] " + e.toString());
                    e.printStackTrace();
                }
            }

        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
    }

    private String getAppdata() {
        String workingDirectory;
        //here, we assign the name of the OS, according to Java, to a variable...
        String OS = (System.getProperty("os.name")).toUpperCase();
        //to determine what the workingDirectory is.
        //if it is some version of Windows
        if (OS.contains("WIN")) {
            //it is simply the location of the "AppData" folder
            workingDirectory = System.getenv("AppData");
        }
        //Otherwise, we assume Linux or Mac
        else {
            //in either case, we would start in the user's home directory
            workingDirectory = System.getProperty("user.home");
            //if we are on a Mac, we are not done, we look for "Application Support"
            workingDirectory += "/Library/Application Support";
        }
        //we are now free to set the workingDirectory to the subdirectory that is our 
        //folder.

        workingDirectory += "/Ukrainebot";

        return workingDirectory;

    }

    public static Boolean hasAllKeys(Properties p) {
        Boolean hasAllKeys = null;

        String ibmKey = p.getProperty("ibm-key");
        String discordKey = p.getProperty("discord-key");

        hasAllKeys = false;
        if (ibmKey != null) {
            if (discordKey != null) {
                hasAllKeys = true;
            }
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
        BufferedReader br = new BufferedReader(new FileReader(getAppdata() + "/config.properties"));
        Properties properties = new Properties();
        try {
            properties.load(br);
            System.out.println("---PROPERTIES---");
            System.out.println("ibm-key - " + properties.get("ibm-key"));
            System.out.println("discord-key - " + properties.get("discord-key"));
            System.out.println("---END PROPERTIES---");
            br.close();
        } catch (IOException e) {
            System.out.println("WARNING: You do not have a config file! Creating one now...");
            new File(getAppdata()).mkdirs();
            File config = new File(getAppdata() + "/config.properties");
            try {
                PrintStream myWriter = new PrintStream(config);
                myWriter.println("ibm-key=");
                myWriter.println("discord-key=");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e2) {
                System.out.println("An error occurred.");
                e2.printStackTrace();
            }
        }



        Boolean hasAllKeys = hasAllKeys(properties);

        primaryStage.setTitle("UkraineBot Panel");
        Button btn = new Button();
        btn.setText("Configure Keys");
        btn.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                createKeyPopup(primaryStage, properties);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 853, 480));

        if (hasAllKeys == false) {
            createKeyPopup(primaryStage, properties);
        }

        primaryStage.show();

    }
}