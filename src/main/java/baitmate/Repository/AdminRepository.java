package baitmate.Repository;

import baitmate.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	@Query("Select a from Admin a where a.username =:username")
	public Admin searchUserByUserName(@Param("username") String username);



	@Query("SELECT a FROM Admin a WHERE a.email = :email")
	Optional<Admin> findAdminByEmail(@Param("email") String email);

	@Query("SELECT a FROM Admin a WHERE a.username = :username AND a.email = :email")
	Optional<Admin> findAdminByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	@Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.username = :username AND a.email = :email")
	boolean existsByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

	List<Admin> findAll();

	//Optional<Admin> findById(long id); No need to write query, spring data jpa provides.

}