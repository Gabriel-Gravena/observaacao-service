package observaacao.org.com.auth;

import observaacao.org.com.auth.dto.LoginRequest;
import observaacao.org.com.auth.dto.RegisterRequest;
import observaacao.org.com.auth.dto.AuthResponse;
import observaacao.org.com.auth.dto.TokenResponse;
import observaacao.org.com.auth.jwt.JwtService;
import observaacao.org.com.common.enums.Role;
import observaacao.org.com.user.User;
import observaacao.org.com.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signUp(RegisterRequest request){
        String email = request.email();
        if(userService.findByEmail(email).isPresent()){
            throw new RuntimeException("Este usuario ja existe!");
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.name(),
                request.email(),
                request.cpf(),
                request.telefone(),
                encryptedPassword,
                Role.CIDADAO
        );

        userService.createUser(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(user.getId(), token);
    }

    public TokenResponse signIn(LoginRequest request){
        String reqEmail = request.email();
        String reqPassword = request.password();

        User user = userService.findByEmail(reqEmail)
                .orElseThrow(() -> new RuntimeException("Credenciais invalidas"));

        boolean validPassword = passwordEncoder.matches(
                reqPassword,
                user.getPassword()
        );

        if(!validPassword){
            throw new RuntimeException("Credenciais invalidas");
        }

        String token = jwtService.generateToken(user);

        return new TokenResponse(token);
    }

}
