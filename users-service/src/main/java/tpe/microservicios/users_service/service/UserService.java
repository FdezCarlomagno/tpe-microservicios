package tpe.microservicios.users_service.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.users_service.domain.User;
import tpe.microservicios.users_service.repository.UserRepository;
import tpe.microservicios.users_service.service.dto.user.request.UserRequestDTO;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> findAll() {
        return userRepository.findUsers();
    }



    public UserResponseDTO findById(long id) {
        return userRepository.findById(id);
    }

    public User save(UserRequestDTO userRequestDTO) {
        return userRepository.save(new User(userRequestDTO));
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO, Long id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userToUpdate.setApellido(userRequestDTO.apellido());
        userToUpdate.setNombre(userRequestDTO.nombre());
        userToUpdate.setEmail(userRequestDTO.email());
        userToUpdate.setTelefono(userRequestDTO.telefono());

        userRepository.save(userToUpdate);

        return new UserResponseDTO(
                id,
                userToUpdate.getNombre(),
                userToUpdate.getApellido(),
                userToUpdate.getTelefono(),
                userToUpdate.getEmail()
        );
    }
}
