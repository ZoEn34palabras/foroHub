package com.forohub.security;

import com.forohub.entity.Profile;
import com.forohub.entity.User;
import com.forohub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String correoElectronico) throws UsernameNotFoundException {
        User user = userRepo.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + correoElectronico));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCorreoElectronico())
                .password(user.getContrasena())
                .authorities(
                        user.getProfiles().stream()
                                .map(Profile::getNombre)
                                .toArray(String[]::new)
                )
                .build();
    }
}
