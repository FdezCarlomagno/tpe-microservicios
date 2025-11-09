package tpe.microservicios.accounts_service.web;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.accounts_service.domain.UserAccount;
import tpe.microservicios.accounts_service.service.UserAccountService;
import tpe.microservicios.accounts_service.service.dto.request.UserAccountRequestDTO;
import tpe.microservicios.accounts_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.accounts_service.service.dto.response.EstadoCuentaDTO;
import tpe.microservicios.accounts_service.service.dto.response.UserAccountResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("User account api running");
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(){
        return ResponseEntity.ok(userAccountService.getAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id){
        return ResponseEntity.ok(userAccountService.getAccountById(id));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserAccountResponseDTO>> getUsersFromAccount(@PathVariable("id") long id) {
        return ResponseEntity.ok(userAccountService.getUsersFromAccount(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<AccountResponseDTO> anularCuenta(@PathVariable("id") Long id){
        return ResponseEntity.ok(userAccountService.anularCuenta(id));
    }

    @PutMapping("/{id}/restaurar")
    public ResponseEntity<AccountResponseDTO> restaurarCuenta(@PathVariable("id") Long id){
        return ResponseEntity.ok(userAccountService.restaurarCuenta(id));
    }

    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccountRequestDTO userAccountRequestDTO) {
        return ResponseEntity.ok(userAccountService.createUserAccount(userAccountRequestDTO));
    }

    @PutMapping("/saldo/{id}")
    public ResponseEntity<String> updateSaldo(@PathVariable long id, @RequestBody float saldo) {
        userAccountService.updateSaldo(saldo, id);
        return ResponseEntity.ok("Saldo actualizado");
    }

    @PutMapping("/{id}/link-user")
    public ResponseEntity<UserAccountResponseDTO> linkUserToUserAccount(@PathVariable("id") long idUserAccount, @RequestBody long idUser){
        return ResponseEntity.ok(userAccountService.linkUserToUserAccount(idUserAccount, idUser));
    }

    @PutMapping("/{id}/unlink-user")
    public ResponseEntity<UserAccountResponseDTO> unlinkUserToUserAccount(@PathVariable("id") long idUserAccount, @RequestBody long idUser){
        return ResponseEntity.ok(userAccountService.unlinkUserToUserAccount(idUserAccount, idUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable("id") long idUserAccount) {
        userAccountService.deleteAccount(idUserAccount);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<EstadoCuentaDTO> isCuentaAnulada(@PathVariable("id") Long id){
        return ResponseEntity.ok(userAccountService.isCuentaAnulada(id));
    }
}
