package tpe.microservicios.users_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import tpe.microservicios.users_service.service.dto.user.request.UserRequestDTO;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

@Document(collection = "users")  // Cambio: @Entity → @Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Id  // Cambio: @Id de Spring Data MongoDB
    private String id;  // Cambio: long → String (MongoDB usa ObjectId como String)

    private String nombre;
    private String apellido;
    private String telefono;

    @Indexed(unique = true)  // Cambio: @Column(unique = true) → @Indexed(unique = true)
    private String email;

    public User(UserRequestDTO userRequestDTO) {
        this.email = userRequestDTO.email();
        this.nombre = userRequestDTO.nombre();
        this.apellido = userRequestDTO.apellido();
        this.telefono = userRequestDTO.telefono();
    }

    public UserResponseDTO toDTO(){
        return new UserResponseDTO(
                this.id,  // Ahora es String
                this.nombre,
                this.apellido,
                this.telefono,
                this.email
        );
    }
}