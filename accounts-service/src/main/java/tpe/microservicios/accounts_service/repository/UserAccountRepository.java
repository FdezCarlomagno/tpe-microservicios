package tpe.microservicios.accounts_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tpe.microservicios.accounts_service.domain.UserAccount;
import tpe.microservicios.accounts_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.accounts_service.service.dto.response.UserAccountResponseDTO;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query("SELECT a FROM UserAccount a WHERE :idUser IN elements(a.idUsers)")
    Optional<UserAccount> getAccountByUserID(@Param("idUser") String idUser);
}

