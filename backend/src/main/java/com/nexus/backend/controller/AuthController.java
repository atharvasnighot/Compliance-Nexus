package com.nexus.backend.controller;

import com.nexus.backend.config.TokenProvider;
import com.nexus.backend.dto.AuthResponse;
import com.nexus.backend.dto.LoginRequest;
import com.nexus.backend.entity.User;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import com.nexus.backend.repository.*;
import com.nexus.backend.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StateRepository stateRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();

        User isUser = userRepository.findByEmail(email);
        if (isUser != null)
            throw new Exception("Email Id already registered");

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setUsername(username);
        createdUser.setPassword(passwordEncoder.encode(password));

        if (user.getIsAdmin() == null){
            createdUser.setIsAdmin(false);
        } else {
            createdUser.setIsAdmin(user.getIsAdmin());
        }

        if (user.getOrganization() != null)
            createdUser.setOrganization(user.getOrganization());
        if (user.getContact() != null)
            createdUser.setContact(user.getContact());


        if (user.getMinistry() != null) {
            Ministry ministry = user.getMinistry().getId() != null
                    ? ministryRepository.findById(user.getMinistry().getId()).orElse(null)
                    : null;
            createdUser.setMinistry(ministry);
        }

        if (user.getIndustry() != null) {
            Industry industry = user.getIndustry().getId() != null
                    ? industryRepository.findById(user.getIndustry().getId()).orElse(null)
                    : null;
            createdUser.setIndustry(industry);
        }

        if (user.getCategory() != null) {
            Category category = user.getCategory().getId() != null
                    ? categoryRepository.findById(user.getCategory().getId()).orElse(null)
                    : null;
            createdUser.setCategory(category);
        }

        if (user.getState() != null) {
            State state = user.getState().getId() != null
                    ? stateRepository.findById(user.getState().getId()).orElse(null)
                    : null;
            createdUser.setState(state);
        }

        userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse response = AuthResponse.builder()
                .jwt(jwt)
                .isAuth(true)
                .build();
        System.out.println("Email Registered");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request){
        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse(jwt, true);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String username, String password){

        UserDetails userDetails =  customUserService.loadUserByUsername(username);

        if (userDetails == null)
            throw new BadCredentialsException("Invalid Username or Password");

        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password or Username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
