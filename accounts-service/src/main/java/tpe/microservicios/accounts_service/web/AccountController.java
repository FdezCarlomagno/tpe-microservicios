package tpe.microservicios.accounts_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    // -------------------------------------------------------------------------
    @Operation(summary = "Checkeo del estado de la API User Account")
    @ApiResponse(responseCode = "200", description = "User Account API running")
    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("User account api running");
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener todas las cuentas de usuario",
            description = "Obtiene todas las cuentas registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de cuentas obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(){
        return ResponseEntity.ok(userAccountService.getAccounts());
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener cuenta de usuario por ID",
            description = "Permite recuperar la cuenta de usuario según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(
            @Parameter(description = "ID de la cuenta de usuario", example = "1")
            @PathVariable("id") long id){

        AccountResponseDTO a = userAccountService.getAccountById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(a);
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener usuarios asociados a una cuenta",
            description = "Devuelve la lista de usuarios vinculados a una cuenta específica"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida correctamente"
    )
    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserAccountResponseDTO>> getUsersFromAccount(
            @Parameter(description = "ID de la cuenta", example = "3")
            @PathVariable("id") long id) {

        return ResponseEntity.ok(userAccountService.getUsersFromAccount(id));
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Anular una cuenta de usuario",
            description = "Inhabilita una cuenta de usuario dejándola fuera de servicio"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta anulada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/{id}/anular")
    public ResponseEntity<AccountResponseDTO> anularCuenta(
            @Parameter(description = "ID de la cuenta", example = "3")
            @PathVariable("id") long id){

        AccountResponseDTO a = userAccountService.anularCuenta(id);
        if (a == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(a);
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Restaurar una cuenta de usuario",
            description = "Restaura una cuenta previamente anulada para volver a utilizarla"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta restaurada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/{id}/restaurar")
    public ResponseEntity<AccountResponseDTO> restaurarCuenta(
            @Parameter(description = "ID de la cuenta", example = "3")
            @PathVariable("id") Long id){

        AccountResponseDTO restored = userAccountService.restaurarCuenta(id);
        if (restored == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restored);
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Crear una nueva cuenta de usuario",
            description = "Permite crear una cuenta nueva asociada a un usuario existente"
    )
    @ApiResponse(responseCode = "200", description = "Cuenta creada correctamente")
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createUserAccount(
            @RequestBody UserAccountRequestDTO userAccountRequestDTO) {
        return ResponseEntity.ok(userAccountService.createUserAccount(userAccountRequestDTO));
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Actualizar saldo de una cuenta de usuario",
            description = "Modifica el saldo asociado a la cuenta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/saldo/{id}")
    public ResponseEntity<String> updateSaldo(
            @Parameter(description = "ID de la cuenta", example = "5")
            @PathVariable("id") long id,
            @RequestBody float saldo) {

        userAccountService.updateSaldo(saldo, id);
        return ResponseEntity.ok("Saldo actualizado");
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Vincular un usuario a una cuenta",
            description = "Asocia un usuario existente a una cuenta de usuario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario vinculado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta o usuario no encontrado")
    })
    @PutMapping("/{id}/link-user")
    public ResponseEntity<UserAccountResponseDTO> linkUserToUserAccount(
            @Parameter(description = "ID de la cuenta", example = "2")
            @PathVariable("id") long idUserAccount,
            @RequestBody long idUser){

        return ResponseEntity.ok(userAccountService.linkUserToUserAccount(idUserAccount, idUser));
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Desvincular un usuario de una cuenta",
            description = "Elimina la asociación entre un usuario y una cuenta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario desvinculado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta o usuario no encontrado")
    })
    @PutMapping("/{id}/unlink-user")
    public ResponseEntity<UserAccountResponseDTO> unlinkUserToUserAccount(
            @Parameter(description = "ID de la cuenta", example = "2")
            @PathVariable("id") long idUserAccount,
            @RequestBody long idUser){

        return ResponseEntity.ok(userAccountService.unlinkUserToUserAccount(idUserAccount, idUser));
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Eliminar una cuenta de usuario",
            description = "Elimina por completo la cuenta seleccionada del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAccount(
            @Parameter(description = "ID de la cuenta", example = "4")
            @PathVariable("id") long idUserAccount) {

        userAccountService.deleteAccount(idUserAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Consultar estado de la cuenta",
            description = "Indica si una cuenta se encuentra activa o anulada"
    )
    @ApiResponse(responseCode = "200", description = "Estado de la cuenta obtenido correctamente")
    @GetMapping("/{id}/estado")
    public ResponseEntity<EstadoCuentaDTO> isCuentaAnulada(
            @Parameter(description = "ID de la cuenta", example = "7")
            @PathVariable("id") Long id){

        return ResponseEntity.ok(userAccountService.isCuentaAnulada(id));
    }

}
