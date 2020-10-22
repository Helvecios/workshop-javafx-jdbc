package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	//Declarar um dependência "dao" e usar a factory para injetar a dependêncai
	private SellerDao dao = DaoFactory.createSellerDao();
		
	//Vai no BD e busca os departamentos 
	public List<Seller> findAll() {
		return dao.findAll();
	}
	
	//Metodo salvar ou atualizar um novo obj
	public void saveOrUpdate(Seller obj)  {
		if (obj.getId() == null) { //Se for nulo, insere um novo departamento
			dao.insert(obj);
		}
		else { //Fazer atualização do BD
			dao.update(obj);
		}
	}
	
	//Metodo para remover um departamento
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
}
