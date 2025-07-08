package org.demo.config.security.provider;

import org.demo.dto.LoginAttemptDto;
import org.demo.service.UserService;
import org.demo.service.kafka.KafkaSenderService;
import org.demo.service.telemetry.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static org.demo.utils.Constants.LOGIN_ATTEMPTS_CACHE;
import static org.demo.utils.Constants.LOGIN_ATTEMPTS_TOPIC;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("cacheManagerLogin")
    private CacheManager cacheManagerLogin;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaSenderService kafkaSenderService;

    @Autowired
    private MetricsService metricsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        final String username = authentication.getName();
        final var attemps = Objects.requireNonNull(cacheManagerLogin.getCache(LOGIN_ATTEMPTS_CACHE)).get(username);
        var attempsValue = 0;

        checkIfNumberOfPossibleAttemptsReached(attemps, username);

        var user = userService.loadUserByUsername(username);

        if (user != null) {
            var apiPass = new String(Base64.getDecoder().decode((String) authentication.getCredentials()));
            var dbPass = user.getPassword();

            if (username.equals(user.getUsername()) && passwordEncoder.matches(apiPass, dbPass)) {
                Objects.requireNonNull(cacheManagerLogin.getCache(LOGIN_ATTEMPTS_CACHE)).evict(username);

                final List<GrantedAuthority> authorities = new ArrayList<>();
                user.getAuthorities().forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));

                return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
            } else {

                if (Objects.isNull(attemps)) {
                    Objects.requireNonNull(cacheManagerLogin.getCache(LOGIN_ATTEMPTS_CACHE)).put(username, 1);
                    throw new BadCredentialsException("Incorrect username or password!");
                }

                attempsValue = (Integer) Objects.requireNonNull(attemps.get());
                Objects.requireNonNull(cacheManagerLogin.getCache(LOGIN_ATTEMPTS_CACHE)).put(username, attempsValue + 1);
                throw new BadCredentialsException("Incorrect username or password!");
            }
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

    private void checkIfNumberOfPossibleAttemptsReached(Cache.ValueWrapper attemps, String username) {
        metricsService.executeWithTracing("checkIfNumberOfPossibleAttemptsReached", () -> {
            if (Objects.nonNull(attemps)) {
                var attempsValue = (Integer) Objects.requireNonNull(attemps.get());
                if (attempsValue >= 5) {

                    var loginAttemptDto = LoginAttemptDto.builder()
                            .username(username)
                            .message("User reached maximum login attempts")
                            .attempts(attempsValue)
                            .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                            .build();

                    kafkaSenderService.sendMessage(LOGIN_ATTEMPTS_TOPIC, loginAttemptDto);
                    metricsService.incrementKafkaMessagesSent();
                    throw new BadCredentialsException("Number of possible attempts reached!");
                }
            }
        });
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
