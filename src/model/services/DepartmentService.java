package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	//Declarar um dependência "dao" e usar a factory para injetar a dependêncai
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
		
	//Vai no BD e busca os departamentos 
	public List<Department> findAll() {
		return dao.findAll();
	}
	
	//Metodo salvar ou atualizar um novo obj
	public void saveOrUpdate(Department obj)  {
		if (obj.getId() == null) { //Se for nulo, insere um novo departamento
			dao.insert(obj);
		}
		else { //Fazer atualização do BD
			dao.update(obj);
		}
	}
	
	//Metodo para remover um departamento
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
}
