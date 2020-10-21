package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	//Criar a lista de departamento e a operação findAll 
	public List<Department> findAll() {
	
	//Moch em programação é retornar os dados de "mentira" simulação
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		return list;
		
	}

}
