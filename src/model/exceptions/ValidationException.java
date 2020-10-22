package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	//Atributo e instancia��o para guardar os erros de cada campo do formul�rio vai indicar a mensgem de erro
	//O primeiro String vai indicar o nome do campo e o segundo String 
	private Map<String, String> errors = new HashMap<>();
	
	//Instancia��o da exce��o como String
	public ValidationException(String msg) {
		super(msg);
	}
	
	//M�todo get
	public Map<String, String> getErrors() {
		return errors;
	}

	//M�todo para adiconar os erros
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
