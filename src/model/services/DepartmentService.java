package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	//Declarar um dependÍncia "dao" e usar a factory para injetar a dependÍncai
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
		
	//Vai no BD e busca os departamentos 
	public List<Department> findAll() {
		return dao.findAll();
	}
}
