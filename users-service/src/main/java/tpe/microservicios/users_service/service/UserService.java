package tpe.microservicios.users_service.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.users_service.domain.User;
import tpe.microservicios.users_service.exceptions.NotFoundException;
import tpe.microservicios.users_service.repository.UserRepository;
import tpe.microservicios.users_service.service.dto.user.request.UserRequestDTO;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

import java.util.List;

@Service
@Transactional(dontRollbackOn = NotFoundException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getTelefono(),
                user.getEmail()   // ✔ corrección
        );
    }

    public UserResponseDTO findById(long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return convertToDTO(u);
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        User saved = userRepository.save(new User(userRequestDTO));
        return convertToDTO(saved);   // ✔ ahora devuelve DTO
    }

    public void delete(long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        userRepository.delete(u);  // ✔ elimina entidad
    }

    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO, Long id) {

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Validación y actualización de nombre
        if (userRequestDTO.nombre() != null && !userRequestDTO.nombre().trim().isEmpty()) {
            userToUpdate.setNombre(userRequestDTO.nombre().trim());
        }

        // Validación y actualización de apellido
        if (userRequestDTO.apellido() != null && !userRequestDTO.apellido().trim().isEmpty()) {
            userToUpdate.setApellido(userRequestDTO.apellido().trim());
        }

        // Validación y actualización de email
        if (userRequestDTO.email() != null && !userRequestDTO.email().trim().isEmpty()) {
            userToUpdate.setEmail(userRequestDTO.email().trim());
        }

        // Validación y actualización de teléfono
        if (userRequestDTO.telefono() != null && !userRequestDTO.telefono().trim().isEmpty()) {
            userToUpdate.setTelefono(userRequestDTO.telefono().trim());
        }

        User updated = userRepository.save(userToUpdate);

        return convertToDTO(updated);
    }

}

