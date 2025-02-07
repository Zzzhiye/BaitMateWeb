package baitmate.ImplementationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import baitmate.Repository.AdminRepository;
import baitmate.Service.AdminService;
import baitmate.model.Admin;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public Admin searchUserByUserName(String username) {
		// TODO Auto-generated method stub
		Admin a = adminRepo.searchUserByUserName(username);
		return a;
	}
	
	@Override
	public Admin getAdminById(int id) {
		return adminRepo.findById(id).orElse(null);
	}
	
	@Override
	public Admin updateAdmin(Admin admin) {
		return adminRepo.save(admin);
	}
}
