<<<<<<< HEAD
package com.SocialNetwork.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User customer = userRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername(customer.getEmail())
            .password(customer.getUser_password())
            .authorities("USER").build();
        return user;
    }
=======
package com.SocialNetwork.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User customer = userRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername(customer.getEmail())
            .password(customer.getUser_password())
            .authorities(customer.getRole()).build();
        return user;
    }
>>>>>>> b294e1654464e6366dd5c35e19e7cb01c33c5ba5
}