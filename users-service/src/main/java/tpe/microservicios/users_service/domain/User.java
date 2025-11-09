package tpe.microservicios.users_service.domain;

import jakarta.persistence.*;
import lombok.*;
import tpe.microservicios.users_service.service.dto.user.request.UserRequestDTO;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String telefono;

    @Column(unique = true)
    private String email;




    public User(UserRequestDTO userRequestDTO) {
        this.email = userRequestDTO.email();
        this.nombre = userRequestDTO.nombre();
        this.apellido = userRequestDTO.apellido();
        this.telefono = userRequestDTO.telefono();
    }

    public UserResponseDTO toDTO(){
        return new UserResponseDTO(
                this.id,
             this.nombre,
             this.apellido,
             this.telefono,
             this.email
        );
    }

}
