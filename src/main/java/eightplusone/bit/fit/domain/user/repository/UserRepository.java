package eightplusone.bit.fit.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eightplusone.bit.fit.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByProvider(String provider);

	boolean existsByEmail(String email);

	User findLoginUserByEmail(String email);

	@Query("""
		select u from User u left join fetch u.interests where u.email = :email
		""")
	User findLoginUserByEmailWithInterest(String email);
}
