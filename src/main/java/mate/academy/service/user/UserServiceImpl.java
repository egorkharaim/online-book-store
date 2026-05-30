package mate.academy.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.user.Role;
import mate.academy.model.user.RoleName;
import mate.academy.model.user.User;
import mate.academy.repository.user.RoleRepository;
import mate.academy.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        User user = userMapper.toModel(requestDto);

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("This email: " + user.getEmail() + " is already taken");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RegistrationException("Can't find default role USER"));

        user.setRoles(Set.of(userRole));

        return userMapper.toDto(userRepository.save(user));

    }

}
