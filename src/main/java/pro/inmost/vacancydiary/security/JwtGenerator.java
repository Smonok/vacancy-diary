package pro.inmost.vacancydiary.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.service.UserService;

@Component
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String secret;
    private final UserService userService;

    @Autowired
    public JwtGenerator(UserService userService) {
        this.userService = userService;
    }

    public String generate(UserDetails userDetails) {
        User userByEmail = userService.findByEmail(userDetails.getUsername());
        String subject = userByEmail.getEmail();
        Claims claims = Jwts.claims()
            .setSubject(subject);
        claims.put("userId", userByEmail.getId());

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
}
