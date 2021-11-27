package com.SocialNetwork.Repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SocialNetwork.Sheet.CountLogInSheet;

public interface LoggedInCount extends JpaRepository<com.SocialNetwork.Entity.LoggedInCount, Integer> {
	@Query(name="LoggedInCount.countLogByDate",nativeQuery=true)
	List<CountLogInSheet> countLogByDate();
}
