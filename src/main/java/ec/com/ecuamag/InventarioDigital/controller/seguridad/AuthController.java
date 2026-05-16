package ec.com.ecuamag.InventarioDigital.controller.seguridad;

import ec.com.ecuamag.InventarioDigital.service.seguridad.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthService.LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.username(), request.password());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = "X-Auth-Token", required = false) String token) {
        authService.logout(token);
    }

    public record LoginRequest(String username, String password) {
    }
}
