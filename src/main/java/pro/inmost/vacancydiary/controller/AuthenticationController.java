package pro.inmost.vacancydiary.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.AuthenticationRequest;
import pro.inmost.vacancydiary.security.JwtGenerator;
import pro.inmost.vacancydiary.service.impl.UserDetailsServiceImpl;

@Slf4j
@RestController
@RequestMapping("/diary/")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtGenerator jwtGenerator;

    public AuthenticationController(AuthenticationManager authenticationManager,
        UserDetailsServiceImpl userDetailsService, JwtGenerator jwtGenerator) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("authenticate")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        log.info("Starting authentication...");
        authenticationManager.authenticate(token);
        log.info("Authentication complete");

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String authenticationToken = jwtGenerator.generate(userDetails);
        log.info("Authentication token generated for user with email: {}", email);

        return ResponseEntity.ok(authenticationToken);
    }
}
