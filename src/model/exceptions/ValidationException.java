package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	//Atributo e instanciação para guardar os erros de cada campo do formulário vai indicar a mensgem de erro
	//O primeiro String vai indicar o nome do campo e o segundo String 
	private Map<String, String> errors = new HashMap<>();
	
	//Instanciação da exceção como String
	public ValidationException(String msg) {
		super(msg);
	}
	
	//Método get
	public Map<String, String> getErrors() {
		return errors;
	}

	//Método para adiconar os erros
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
