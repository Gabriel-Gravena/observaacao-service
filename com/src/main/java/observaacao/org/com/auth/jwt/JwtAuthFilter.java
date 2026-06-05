package observaacao.org.com.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import observaacao.org.com.user.User;
import observaacao.org.com.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtCookieService jwtCookieService;
    private final UserService userService;

    public JwtAuthFilter(
            JwtService jwtService,
            JwtCookieService jwtCookieService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.jwtCookieService = jwtCookieService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtCookieService.extractTokenFromCookies(request);

        if(token == null || !jwtService.isTokenValid(token)){
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(token);

        User user = userService.findByEmail(email).orElse(null);

        if(user == null){
            filterChain.doFilter(request, response);
            return;
        }

        var authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        var authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
