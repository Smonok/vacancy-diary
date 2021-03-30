package pro.inmost.vacancydiary.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.inmost.vacancydiary.security.exception.MissingJwtTokenException;
import pro.inmost.vacancydiary.security.util.JwtUtil;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final String BEARER_TOKEN_HEADER = "Bearer";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHENTICATE_REQUEST_PATH = "/diary/authenticate/";
    private final JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthenticationTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        log.info("Authorization header: {}", authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_TOKEN_HEADER)) {
            throw new MissingJwtTokenException("JWT Bearer token is missing");
        }

        String authenticationToken = authorizationHeader.substring(BEARER_TOKEN_HEADER.length());
        String login = jwtUtil.getUsernameFromToken(authenticationToken);
        log.info("Authorization token: {}", authenticationToken);
        log.info("Login: {}", login);

        if (login != null && !isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);

            if (jwtUtil.validateToken(authenticationToken, userDetails)) {
                UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return AUTHENTICATE_REQUEST_PATH.equals(path);
    }
}
