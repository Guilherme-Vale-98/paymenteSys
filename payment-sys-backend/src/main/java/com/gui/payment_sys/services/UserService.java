package com.gui.payment_sys.services;

import com.gui.payment_sys.entities.Provider;
import com.gui.payment_sys.entities.Role;
import com.gui.payment_sys.entities.User;
import com.gui.payment_sys.repository.RoleRepository;
import com.gui.payment_sys.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User processOAuthPostLogin(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider(Provider.GOOGLE);
                    newUser.setEnabled(true);

                    // Add default USER role
                    Role userRole = roleRepository.findByName("USER")
                            .orElseThrow(() -> new RuntimeException("Role USER not found"));
                    newUser.getRoles().add(userRole);

                    return userRepository.save(newUser);
                });

        return user;
    }
}