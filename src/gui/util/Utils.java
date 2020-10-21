package gui.util;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


public class Utils {
	
	//Função "currentStage()" (Palco atual)
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	//Metodo para converte o valor da caixinha para um número inteiro
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null; //Se acontecer algum problema na conversao ele devolde um "null"
		}
	}
}
