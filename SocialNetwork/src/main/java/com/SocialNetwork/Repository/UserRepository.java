package com.SocialNetwork.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.SocialNetwork.Entity.User;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	@Query(value = "select e.user_id from users e where e.email=?1", nativeQuery = true)
	Integer findIdByEmail(String email);

	List<User> findByFullname(String fullname);
}
