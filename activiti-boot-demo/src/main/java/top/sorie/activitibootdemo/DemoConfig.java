package top.sorie.activitibootdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class DemoConfig {

    @Bean
    public UserDetailsService myUserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        String[][] usersGroupAndRoles = {
                { "jack", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                { "rose", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                { "tom", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                { "jerry", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                { "other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                { "zhangsan", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                { "system", "password", "ROLE_ACTIVITI_USER"},
                { "admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };
        Arrays.stream(usersGroupAndRoles).forEach(p -> {
            List<String> authorites = Arrays.asList(Arrays.copyOfRange(p, 2, p.length));
            inMemoryUserDetailsManager.createUser(new User(p[0], passwordEncoder().encode(p[1]),
                    authorites.stream().map(str -> new SimpleGrantedAuthority(str)).collect(Collectors.toList())
            ));
        });

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
