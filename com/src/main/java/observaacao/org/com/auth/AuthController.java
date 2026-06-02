package observaacao.org.com.auth;

import jakarta.validation.Valid;
import observaacao.org.com.auth.dto.LoginRequest;
import observaacao.org.com.auth.dto.RegisterRequest;
import observaacao.org.com.auth.dto.AuthResponse;
import observaacao.org.com.auth.dto.TokenResponse;
import observaacao.org.com.common.dto.UserResponse;
import observaacao.org.com.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<TokenResponse> signUp(@RequestBody @Valid RegisterRequest request){
        AuthResponse response = authService.signUp(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/{id}")
                .buildAndExpand(response.userId())
                .toUri();

        return ResponseEntity.created(uri).body(new TokenResponse(response.token()));
    }

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponse> signIn(@RequestBody @Valid LoginRequest request){
        TokenResponse token = authService.signIn(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(user));
    }
}
