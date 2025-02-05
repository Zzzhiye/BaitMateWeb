package baitmate.Repository;

import baitmate.model.Admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	@Query("Select a from Admin a where a.username =:username")
	public Admin searchUserByUserName(@Param("username") String username);

}
