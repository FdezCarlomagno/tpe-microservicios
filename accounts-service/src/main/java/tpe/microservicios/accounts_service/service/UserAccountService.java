package tpe.microservicios.accounts_service.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.accounts_service.clients.UserClient;
import tpe.microservicios.accounts_service.domain.UserAccount;
import tpe.microservicios.accounts_service.exceptions.ForbiddenException;
import tpe.microservicios.accounts_service.exceptions.NotFoundException;
import tpe.microservicios.accounts_service.repository.UserAccountRepository;
import tpe.microservicios.accounts_service.service.dto.request.UserAccountRequestDTO;
import tpe.microservicios.accounts_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.accounts_service.service.dto.response.EstadoCuentaDTO;
import tpe.microservicios.accounts_service.service.dto.response.UserAccountResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(dontRollbackOn = NotFoundException.class)
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserClient userClient;

    // get all accounts
    public List<AccountResponseDTO> getAccounts(){
        return userAccountRepository.findAll().stream().map(this::convertToAccountResponseDTO).toList();
    }

    public AccountResponseDTO getAccountById(Long id){
        return convertToAccountResponseDTO(userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada")));
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
        UserAccount account = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        account.setCuentaAnulada(true);

        userAccountRepository.save(account);

        return convertToAccountResponseDTO(account);
    }

    public AccountResponseDTO restaurarCuenta(Long id){
        UserAccount account = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        account.setCuentaAnulada(false);

        userAccountRepository.save(account);
        return convertToAccountResponseDTO(account);
    }

    public EstadoCuentaDTO isCuentaAnulada(Long idUser){
        UserAccount account = userAccountRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        return new EstadoCuentaDTO(
                account.getId(),
                account.isCuentaAnulada()
        );
    }

    // puede haber muchos usuarios asociados a una misma cuenta
    public List<UserAccountResponseDTO> getUsersFromAccount(Long idAccount){
        UserAccount account = userAccountRepository.findById(idAccount).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        return account.getIdUsers().stream().map(idUser -> {
            var user = userClient.getUserById(idUser);
            return new UserAccountResponseDTO(
                    user.nombre(),
                    user.email(),
                    account.getType(),
                    account.getSaldo()
            );
        }).toList();
    }

    public AccountResponseDTO createUserAccount(UserAccountRequestDTO userAccountRequestDTO){
        return convertToAccountResponseDTO(userAccountRepository.save(new UserAccount(userAccountRequestDTO)));
    }

    public void updateSaldo(float saldo, long idAccount){
        UserAccount userAccount = userAccountRepository.findById(idAccount).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new NotFoundException("Cuenta anulada");
        }

        if(saldo < 0) throw new IllegalArgumentException("Saldo invalido");

        userAccount.setSaldo(saldo);
        userAccountRepository.save(userAccount);
    }

    public UserAccountResponseDTO linkUserToUserAccount(Long idUserAccount, Long idUser){
        // check if user account exists
        UserAccount userAccount = userAccountRepository.findById(idUserAccount).orElseThrow(() -> new NotFoundException("Cuenta de usuario no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new ForbiddenException("Cuenta anulada");
        }

        //check if user exists in the users-service
        var user = userClient.getUserById(idUser);
        if(user == null) throw new NotFoundException("Usuario no encontrado");

        //check if user is already linked
        if(userAccount.getIdUsers().contains(idUser)){
            throw new ForbiddenException("Usuario ya esta asociado a esta cuenta");
        }

        //link user
        userAccount.getIdUsers().add(idUser);
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
        UserAccount userAccount = userAccountRepository.findById(idUserAccount).orElseThrow(() -> new NotFoundException("Cuenta de usuario no encontrada"));

        if (userAccount.isCuentaAnulada()){
            throw new ForbiddenException("Cuenta anulada");
        }

        //check if user exists in the users-service
        var user = userClient.getUserById(idUser);
        if(user == null) throw new NotFoundException("Usuario no encontrado");

        //check if user is not linked to the account
        if(!userAccount.getIdUsers().contains(idUser)){
            throw new ForbiddenException("Usuario no esta asociado a esta cuenta");
        }

        //unlink user
        userAccount.getIdUsers().remove(idUser);
        userAccountRepository.save(userAccount);

        return new UserAccountResponseDTO(
                user.nombre(),
                user.email(),
                userAccount.getType(),
                userAccount.getSaldo()
        );
    }

    public void deleteAccount(Long idUserAccount){
        userAccountRepository.findById(idUserAccount).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        userAccountRepository.deleteById(idUserAccount);
    }
}
