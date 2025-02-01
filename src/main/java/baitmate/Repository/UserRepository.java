package baitmate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import baitmate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("Select u from User u where u.Id=:userId")
	public User searchByUserId(@Param("userId") long userId);

}
