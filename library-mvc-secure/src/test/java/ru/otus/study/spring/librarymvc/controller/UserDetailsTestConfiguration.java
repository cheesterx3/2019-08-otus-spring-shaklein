package ru.otus.study.spring.librarymvc.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.study.spring.librarymvc.domain.Role;
import ru.otus.study.spring.librarymvc.domain.User;
import ru.otus.study.spring.librarymvc.security.CustomUserPrincipal;

import java.util.Collections;

/**
 * Создание бина {@link UserDetailsService} в данной конфигурации обусловлено тем, что необходимо, чтобы на некоторых
 * отображаемых страницах выводилось имя пользователя(не логин), а т.к. {@link org.springframework.security.test.context.support.WithMockUser}
 * создаёт дефолтный экземпляр Principal, не содержащий description приходится для некоторых тестов обходиться более
 * упрощённой моделью работы с UserDetails
 */
@TestConfiguration
public class UserDetailsTestConfiguration {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        final Role userRole = new Role("ROLE_USER");
        final Role adminRole = new Role("ROLE_ADMIN");

        final User basicUser = new User("User", "user", "password", Collections.singletonList(userRole));
        final User adminUser = new User("Admin", "admin", "password", Collections.singletonList(adminRole));
        final CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(basicUser);
        final CustomUserPrincipal customAdminPrincipal = new CustomUserPrincipal(adminUser);

        return userName -> {
            if (userName != null) {
                if (userName.equalsIgnoreCase(basicUser.getLogin())) {
                    return customUserPrincipal;
                } else if (userName.equalsIgnoreCase(adminUser.getLogin())) {
                    return customAdminPrincipal;
                }
            }
            throw new UsernameNotFoundException("User not found");
        };
    }


}
