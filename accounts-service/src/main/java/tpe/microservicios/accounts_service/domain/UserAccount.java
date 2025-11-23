package tpe.microservicios.accounts_service.domain;

import jakarta.persistence.*;
import lombok.*;
import tpe.microservicios.accounts_service.service.dto.request.UserAccountRequestDTO;
import tpe.microservicios.accounts_service.utils.AccountType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column
    private LocalDate fechaDeAlta;

    @Column
    private float saldo;

    @ElementCollection
    private List<String> idUsers;

    @Column
    private boolean cuentaAnulada;

    public UserAccount(UserAccountRequestDTO userAccountRequestDTO){
        this.type = userAccountRequestDTO.type();
        this.fechaDeAlta = LocalDate.now();
        this.saldo = 0; //empieza en 0
        if(userAccountRequestDTO.idUsers() == null){
            this.idUsers = new ArrayList<>();
        } else {
            this.idUsers = new ArrayList<>(userAccountRequestDTO.idUsers());
        }
    }


}
