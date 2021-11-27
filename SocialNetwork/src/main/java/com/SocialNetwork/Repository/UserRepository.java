package com.SocialNetwork.Repository;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Sheet.CountUserSheet;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	@Query(value = "select e.user_id from users e where e.email=?1", nativeQuery = true)
	Integer findIdByEmail(String email);

	List<User> findByFullname(String fullname);
	
	@Query(nativeQuery=true,name="User.countUserByDate")
	List<CountUserSheet> countUserByDate();
	
	@Modifying
	@Query(value="update users set enabled = ?1 where user_id = ?2",nativeQuery=true)
	void ActiveAndLock(int status,int userId);
}
