package eightplusone.bit.fit.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eightplusone.bit.fit.domain.session.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
