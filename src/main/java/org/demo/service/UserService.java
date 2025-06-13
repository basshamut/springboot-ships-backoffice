package org.demo.service;

import lombok.AllArgsConstructor;
import org.demo.dto.AuthDto;
import org.demo.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Repository
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AuthDto loadUserByUsername(String username) {
        final Set<String> authorities = new HashSet<>();

        var user = userRepository.findByEmail(username);

        if (Objects.nonNull(user)) {
            authorities.add("ROLE_" + user.getRole());
            var pass = new String(Base64.getDecoder().decode(user.getPassword()));

            return new AuthDto(user.getEmail(), pass, authorities);
        }

        return null;
    }

}
