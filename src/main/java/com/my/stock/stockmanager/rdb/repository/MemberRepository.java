package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickName(String nickName);
}
