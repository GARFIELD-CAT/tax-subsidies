package ru.utmn.dyagunov.tax_subsidies.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JpaUserDetailsService implements UserDetailsService {
    PersonRepository personRepository;
    PasswordEncoder encoder;

    public JpaUserDetailsService(PersonRepository personRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.encoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Person person = personRepository.findByNameIgnoreCase(username);

        if (person != null) {
            String password = encoder.encode(person.getPassword());
            return User
                    .withUsername(person.getName())
                    .accountLocked(!person.isEnabled())
                    .password(password)
                    .roles(person.getRole())
                    .build();
        }
        throw new UsernameNotFoundException(username);
    }
}
