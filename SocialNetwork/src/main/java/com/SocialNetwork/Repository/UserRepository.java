<<<<<<< HEAD
package com.SocialNetwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
}

=======
package com.SocialNetwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
}

>>>>>>> b294e1654464e6366dd5c35e19e7cb01c33c5ba5
