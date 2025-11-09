package tpe.microservicios.accounts_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tpe.microservicios.accounts_service.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
