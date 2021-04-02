package robic;



import static java.util.logging.Level.SEVERE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class Control {
    private File fic;

    public Button btnExecute;
    public Button btnQuit;
    public TextField txtIP;
    public TextField txtPort;
    public TextArea txtScript;
    public TextArea txtLogs;
    public ImageView imgResult;
    public Text txtError;
    
    public boolean ipOk = false, portOk = true, scriptOk = false;
    private static final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    
    public void initialize() {
    }

    public void btnExecute_exec(ActionEvent event) { 
    	//TODO
    	System.out.println("btnExecute_exec");
    	System.out.println("textArea1 = "+txtScript.getText());
    	txtLogs.appendText(">> une exécution sur "+txtIP.getText()+":"+txtPort.getText()+"\n");
    	//Image img = new Image("logo_robic.png");
    	//imgResult.setImage(img);
    }
    
    public void btnQuit_exec(ActionEvent event) {
    	System.out.println("btnQuit_exec");
    	Platform.exit();
    }
    
 // vérifie que le textField txtIP ne soit pas vide
    public void ipIsOk(KeyEvent event) {
    	//if(!txtIP.getText().equals("")) {
    	if(Pattern.matches(IPV4_PATTERN, txtIP.getText())) {
    		ipOk = true;
    	}else {
    		ipOk = false;
    	}
    	executeOk();
    }
    
    // vérifie que le textField txtPort soit valide
    public void portIsOk(KeyEvent event) {
    	//if(!txtIP.getText().equals("")) {(Pattern.matches("d+", txtPort.getText()) &&
    	if(txtPort.getText() == null || (Pattern.matches("\\d+", txtPort.getText()) && Integer.valueOf(txtPort.getText()) > 1000)) {
    		portOk = true;
    	}else {
    		portOk = false;
    	}
    	executeOk();
    }
    
    // vérifie que le textArea txtScript ne soit pas vide
    public void scriptIsOk(KeyEvent event) {
    	if(!txtScript.getText().equals("")) {
    		scriptOk = true;
    	}else {
    		scriptOk = false;
    	}
    	executeOk();
    }
    
    // appelé lors qu'on écrit dans un des 3 champs texte du formulaire
    // vérifie que le formulaire soit ok, si non, dit quel champ pose problème
    private void executeOk() {
		if(ipOk && portOk && scriptOk) {
			btnExecute.setDisable(false);
			txtError.setText("");
		} else {
			btnExecute.setDisable(true);
			if(!scriptOk) txtError.setText("script vide");
			if(!portOk) txtError.setText("port invalide");
			if(!ipOk) txtError.setText("@IP invalide");
		}
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
        	
        	//charger le fichier dans le textArea
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
