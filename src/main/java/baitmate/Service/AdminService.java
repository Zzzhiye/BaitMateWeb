package baitmate.Service;

import baitmate.model.Admin;

public interface AdminService {
	
	Admin searchUserByUserName(String username);
	
	Admin getAdminById(int id);
	
	Admin updateAdmin(Admin admin);
}
