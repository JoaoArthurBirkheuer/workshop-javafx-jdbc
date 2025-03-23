package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller s) {
		if(s.getId() == null) {
			dao.insert(s);
		}
		else {
			dao.update(s);
		}
	}
	
	public void remove(Seller s) {
		dao.deleteById(s.getId());
	}
}
