package eightplusone.bit.fit.domain.mysession.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eightplusone.bit.fit.domain.mysession.entity.MySession;

public interface MySessionRepository extends JpaRepository<MySession, Long> {
	@Query(""" 
		select ms from MySession ms left join fetch ms.session where ms.user.id = :userId
		""")
	Optional<MySession> findSessionByUserId(Long userId);
}
