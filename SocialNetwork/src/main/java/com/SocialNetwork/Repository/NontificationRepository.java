package com.SocialNetwork.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.Nontifications;

@Repository
public interface NontificationRepository extends JpaRepository<Nontifications, Integer> {

	@Query(value="select n.non_id,n.has_seen,n.text,n.date,n.time,n.type,n.post_id,n.sender_id,n.receiver_id from nontifications n where n.has_seen=0 and n.receiver_id=?1",nativeQuery=true)
	List<Nontifications> hasntSeen(Integer userId);
}
