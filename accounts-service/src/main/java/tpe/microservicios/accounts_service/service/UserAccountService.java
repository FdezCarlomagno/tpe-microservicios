package tpe.microservicios.accounts_service.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.accounts_service.clients.UserClient;
import tpe.microservicios.accounts_service.domain.UserAccount;
import tpe.microservicios.accounts_service.repository.UserAccountRepository;
import tpe.microservicios.accounts_service.service.dto.request.UserAccountRequestDTO;
import tpe.microservicios.accounts_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.accounts_service.service.dto.response.EstadoCuentaDTO;
import tpe.microservicios.accounts_service.service.dto.response.UserAccountResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserClient userClient;

    // get all accounts
    public List<AccountResponseDTO> getAccounts(){
        return userAccountRepository.findAll().stream().map(this::convertToAccountResponseDTO).toList();
    }

    public AccountResponseDTO getAccountById(Long id){
        return convertToAccountResponseDTO(userAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada")));
    }

    private AccountResponseDTO convertToAccountResponseDTO(UserAccount account){
        return new AccountResponseDTO(
                account.getId(),
                account.getType(),
                account.getSaldo(),
                account.getIdUsers(),
                account.isCuentaAnulada()
        );
    }

    public AccountResponseDTO anularCuenta(Long id){
        UserAccount account = userAccountRepository.findById(id).orElse(null);
        if (account == null){
            throw new RuntimeException("Cuenta no encontrada");
        }

        account.setCuentaAnulada(true);

        userAccountRepository.save(account);

        return convertToAccountResponseDTO(account);
    }

    public AccountResponseDTO restaurarCuenta(Long id){
        UserAccount account = userAccountRepository.findById(id).orElse(null);
        if (account == null){
            throw new RuntimeException("Cuenta no encontrada");
        }

        account.setCuentaAnulada(false);

        userAccountRepository.save(account);
        return convertToAccountResponseDTO(account);
    }

    public EstadoCuentaDTO isCuentaAnulada(Long idUser){
        UserAccount account = userAccountRepository.findById(idUser).orElse(null);
        if (account == null){
            throw new RuntimeException("Cuenta no encontrada");
        }
        return new EstadoCuentaDTO(
                account.getId(),
                account.isCuentaAnulada()
        );
    }

    // puede haber muchos usuarios asociados a una misma cuenta
    public List<UserAccountResponseDTO> getUsersFromAccount(Long idAccount){
        UserAccount account = userAccountRepository.findById(idAccount).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return account.getIdUsers().stream().map(idUser -> {
            var user = userClient.getUserById(idUser);
            if(user == null){ throw new RuntimeException("Usuario con id:" + idUser + "no encontrado");}
            return new UserAccountResponseDTO(
                    user.nombre(),
                    user.email(),
                    account.getType(),
                    account.getSaldo()
            );
        }).toList();
    }

    public UserAccount createUserAccount(UserAccountRequestDTO userAccountRequestDTO) {
        return userAccountRepository.save(new UserAccount(userAccountRequestDTO));
    }

    public void updateSaldo(float saldo, long idAccount){
        UserAccount userAccount = userAccountRepository.findById(idAccount).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        if(saldo < 0) throw new RuntimeException("Saldo invalido");

        userAccount.setSaldo(saldo);
        userAccountRepository.save(userAccount);
    }

    public UserAccountResponseDTO linkUserToUserAccount(Long idUserAccount, Long idUser){
        // check if user account exists
        UserAccount userAccount = userAccountRepository.findById(idUserAccount).orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        //check if user exists in the users-service
        var user = userClient.getUserById(idUser);
        if(user == null) throw new RuntimeException("Usuario no encontrado");

        //check if user is already linked
        if(userAccount.getIdUsers().contains(idUser)){
            throw new RuntimeException("Usuario ya esta asociado a esta cuenta");
        }

        //link user
        userAccount.getIdUsers().add(idUserAccount);
        userAccountRepository.save(userAccount);

        return new UserAccountResponseDTO(
              user.nombre(),
              user.email(),
              userAccount.getType(),
              userAccount.getSaldo()
        );
    }

    public UserAccountResponseDTO unlinkUserToUserAccount(Long idUserAccount, Long idUser){
        // check if user account exists
        UserAccount userAccount = userAccountRepository.findById(idUserAccount).orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        //check if user exists in the users-service
        var user = userClient.getUserById(idUser);
        if(user == null) throw new RuntimeException("Usuario no encontrado");

        //check if user is not linked to the account
        if(!userAccount.getIdUsers().contains(idUser)){
            throw new RuntimeException("Usuario ya esta asociado a esta cuenta");
        }

        //link user
        userAccount.getIdUsers().remove(idUserAccount);
        userAccountRepository.save(userAccount);

        return new UserAccountResponseDTO(
                user.nombre(),
                user.email(),
                userAccount.getType(),
                userAccount.getSaldo()
        );
    }

    public void deleteAccount(Long idUserAccount){
        userAccountRepository.deleteById(idUserAccount);
    }
}
