package ec.com.ecuamag.InventarioDigital.controller.seguridad;

import ec.com.ecuamag.InventarioDigital.model.seguridad.RegistroInventario;
import ec.com.ecuamag.InventarioDigital.service.seguridad.AuthService;
import ec.com.ecuamag.InventarioDigital.service.seguridad.RegistroInventarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "*")
public class RegistroInventarioController {

    private final RegistroInventarioService registroService;
    private final AuthService authService;

    public RegistroInventarioController(RegistroInventarioService registroService, AuthService authService) {
        this.registroService = registroService;
        this.authService = authService;
    }

    @GetMapping
    public List<RegistroInventario> ultimos(HttpServletRequest request) {
        authService.requerirUsuario(request);
        return registroService.ultimos();
    }
}
