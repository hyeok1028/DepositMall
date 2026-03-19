package com.hana8.hanaro.repository;

import com.hana8.hanaro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Member> findByNicknameContaining(String nickname);

    List<Member> findByEmailContaining(String email);

    List<Member> findByIsActive(Boolean isActive);

    List<Member> findByNicknameContainingAndIsActive(String nickname, Boolean isActive);

    List<Member> findByEmailContainingAndIsActive(String email, Boolean isActive);
}