package ru.otus.study.spring.librarymvc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.librarymvc.domain.User;
import ru.otus.study.spring.librarymvc.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomizedUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByLogin(login);
        return optionalUser.map(CustomUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

}
