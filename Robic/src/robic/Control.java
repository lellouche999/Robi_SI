package robic;



import static java.util.logging.Level.SEVERE;

import java.awt.Label;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class Control {
    private File fic;

    public Button btnExecute;
    public Button btnQuit;
    public TextArea txtIP;
    public TextArea txtPort;
    public TextArea txtScript;
    public TextArea txtLogs;
    
    public void initialize() {
    }

    public void btnExecute_exec(ActionEvent event) { 
    	//TODO
    	System.out.println("btnExecute_exec");
    	System.out.println("textArea1 = "+txtScript.getText());
    	txtLogs.appendText(">> une exécution\n");
    }
    
    public void btnQuit_exec(ActionEvent event) {
    	System.out.println("btnQuit_exec");
    	Platform.exit();
    }
    
    public void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //only allow text files to be selected using chooser
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );

        //set initial directory somewhere user will recognise
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //let user select file
        File fileToLoad = fileChooser.showOpenDialog(null);
        
        if (fileToLoad != null) {
        	System.out.println("file = "+fileToLoad.getPath());
        	
        	txtScript.setText("");
        	BufferedReader buff = null;
        	  try {
        	       buff = new BufferedReader(new FileReader(fileToLoad.getPath()));
        	       String str;
        	       while ((str = buff.readLine()) != null) {
        	       txtScript.appendText("\n"+str);
        	   }
        	 } catch (IOException e) {
        	  } finally {
        	    try { buff.close(); } catch (Exception ex) { }
        	    }
        }
        else {
        	System.out.println("file = null");
        }

     }


    public void saveFile(ActionEvent event) {
    }
}