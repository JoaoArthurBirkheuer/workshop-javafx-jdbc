package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department d) {
		if(d.getId() == null) {
			dao.insert(d);
		}
		else {
			dao.update(d);
		}
	}
	
	public void remove(Department department) {
		dao.deleteById(department.getId());
	}
}
