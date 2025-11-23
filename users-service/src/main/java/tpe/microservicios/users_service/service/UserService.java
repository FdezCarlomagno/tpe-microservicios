package tpe.microservicios.users_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.users_service.clients.AccountClient;
import tpe.microservicios.users_service.domain.User;
import tpe.microservicios.users_service.exceptions.BadRequestException;
import tpe.microservicios.users_service.exceptions.NotFoundException;
import tpe.microservicios.users_service.repository.UserRepository;
import tpe.microservicios.users_service.service.dto.account.response.AccountResponseDTO;
import tpe.microservicios.users_service.service.dto.user.request.UserRequestDTO;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountClient accountClient;

    // Clase interna para manejar validaciones
    private static class UserValidator {

        public static void validateForCreate(UserRequestDTO userRequestDTO) {
            validateRequiredFields(userRequestDTO);
            validateFieldNotEmpty(userRequestDTO.nombre(), "nombre");
            validateFieldNotEmpty(userRequestDTO.apellido(), "apellido");
            validateFieldNotEmpty(userRequestDTO.email(), "email");
            validateFieldNotEmpty(userRequestDTO.telefono(), "teléfono");
            validateEmailFormat(userRequestDTO.email());
            validatePhoneFormat(userRequestDTO.telefono());
        }

        public static void validateForUpdate(UserRequestDTO userRequestDTO) {
            if (userRequestDTO == null) {
                throw new BadRequestException("Datos de usuario no pueden ser nulos");
            }

            // Para update, los campos son opcionales, pero si vienen no deben estar vacíos
            if (userRequestDTO.nombre() != null) {
                validateFieldNotEmpty(userRequestDTO.nombre(), "nombre");
            }
            if (userRequestDTO.apellido() != null) {
                validateFieldNotEmpty(userRequestDTO.apellido(), "apellido");
            }
            if (userRequestDTO.email() != null) {
                validateFieldNotEmpty(userRequestDTO.email(), "email");
                validateEmailFormat(userRequestDTO.email());
            }
            if (userRequestDTO.telefono() != null) {
                validateFieldNotEmpty(userRequestDTO.telefono(), "teléfono");
                validatePhoneFormat(userRequestDTO.telefono());
            }
        }

        private static void validateRequiredFields(UserRequestDTO userRequestDTO) {
            if (userRequestDTO == null) {
                throw new BadRequestException("Datos de usuario no pueden ser nulos");
            }

            if (userRequestDTO.nombre() == null ||
                    userRequestDTO.apellido() == null ||
                    userRequestDTO.email() == null ||
                    userRequestDTO.telefono() == null) {
                throw new BadRequestException("Todos los campos son obligatorios");
            }
        }

        private static void validateFieldNotEmpty(String value, String fieldName) {
            if (value == null || value.trim().isEmpty()) {
                throw new BadRequestException(String.format("El campo %s no puede estar vacío", fieldName));
            }
        }

        private static void validateEmailFormat(String email) {
            if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new BadRequestException("Formato de email inválido");
            }
        }

        private static void validatePhoneFormat(String phone) {
            if (phone != null && !phone.matches("^[+]?[0-9]{10,15}$")) {
                throw new BadRequestException("Formato de teléfono inválido");
            }
        }
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        UserValidator.validateForCreate(userRequestDTO);

        // Validar unicidad del email
        if (userRepository.existsByEmail(userRequestDTO.email())) {
            throw new BadRequestException("El email ya está registrado");
        }

        User saved = userRepository.save(new User(userRequestDTO));
        return convertToDTO(saved);
    }

    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO, String id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserValidator.validateForUpdate(userRequestDTO);

        // Validar unicidad del email si se está actualizando
        if (userRequestDTO.email() != null &&
                !userRequestDTO.email().equals(userToUpdate.getEmail()) &&
                userRepository.existsByEmail(userRequestDTO.email())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Actualizar campos si vienen en el DTO
        updateUserFields(userToUpdate, userRequestDTO);

        User updated = userRepository.save(userToUpdate);
        return convertToDTO(updated);
    }

    private void updateUserFields(User user, UserRequestDTO dto) {
        if (dto.nombre() != null) {
            user.setNombre(dto.nombre().trim());
        }
        if (dto.apellido() != null) {
            user.setApellido(dto.apellido().trim());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email().trim());
        }
        if (dto.telefono() != null) {
            user.setTelefono(dto.telefono().trim());
        }
    }

    // Los demás métodos permanecen igual...
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
                user.getEmail()
        );
    }

    public UserResponseDTO findById(String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return convertToDTO(u);
    }

    public void delete(String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        // Hay que eliminarlo tambien del lado de account

        try {
            AccountResponseDTO account = accountClient.getAccountByUserID(id);
            if (account != null){
                // esta asociado a una cuenta
                accountClient.unlinkUser(account.idAccount(), id);
            }
        } catch(Exception e) {
            //si no hay nada asociado no hacemos nada
        }

        userRepository.delete(u);
    }
}

