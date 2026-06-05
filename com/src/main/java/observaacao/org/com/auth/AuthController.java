package observaacao.org.com.auth;

import jakarta.validation.Valid;
import observaacao.org.com.auth.jwt.JwtCookieService;
import observaacao.org.com.auth.dto.LoginRequest;
import observaacao.org.com.auth.dto.RegisterRequest;
import observaacao.org.com.auth.dto.AuthResponse;
import observaacao.org.com.common.dto.UserResponse;
import observaacao.org.com.user.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtCookieService jwtCookieService;

    public AuthController(AuthService authService, JwtCookieService jwtCookieService) {
        this.authService = authService;
        this.jwtCookieService = jwtCookieService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid RegisterRequest request){
        AuthResponse response = authService.signUp(request);
        ResponseCookie cookie = jwtCookieService.createAuthCookie(response.token());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/{id}")
                .buildAndExpand(response.user().getId())
                .toUri();

        return ResponseEntity.created(uri)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserResponse(response.user()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> signIn(@RequestBody @Valid LoginRequest request){
        AuthResponse response = authService.signIn(request);
        ResponseCookie cookie = jwtCookieService.createAuthCookie(response.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserResponse(response.user()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = jwtCookieService.createLogoutCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(user));
    }
}
