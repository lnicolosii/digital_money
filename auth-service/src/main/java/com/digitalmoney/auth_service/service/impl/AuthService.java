package com.digitalmoney.auth_service.service.impl;

import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;
import com.digitalmoney.auth_service.dto.User.UserDto;
import com.digitalmoney.auth_service.repository.FeignUserRepository;
import com.digitalmoney.auth_service.jwt.JwtUtil;
import com.digitalmoney.auth_service.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService implements IAuthService {
    @Autowired
    private FeignUserRepository userRepository;
    //    @Autowired
//    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public String signup(UserCreateDto data) throws Exception {
        UserDto userExists = userRepository.findByEmail(data.getEmail()).getBody();
        if (userExists != null) {
            throw new Exception("This user does exist");
        }
//        Set<Role> roles = new HashSet<>();
//        for (String role : data.getRoles()) {
//            Role findRole = roleRepository.findByName(role);
//            if (findRole == null) throw new Exception("Role " + role + " not found");
//            roles.add(findRole);
//        }
        data.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
//        newUser.setRoles(roles);

        UserDto user = userRepository.createUser(data);
        return jwtUtil.generateToken(user.getUserId(), user);
    }

    @Override
    public String login(LoginDto data) {
        UserDto user = userRepository.findByEmail(data.getEmail()).getBody();
        assert user != null;
        return jwtUtil.generateToken(user.getUserId(), user);
    }

    @Override
    public Long validateToken(String token) throws Exception {
        String email = jwtUtil.extractUserEmail(token);
        UserDto user = userRepository.findByEmail(email).getBody();

        if (user == null) {
            throw new Exception("Token not valid: user not found");
        }

        if (!jwtUtil.validateToken(token, user)) {
            throw new Exception("Token not valid: invalid signature or expired");
        }

        return user.getUserId();
    }

}
