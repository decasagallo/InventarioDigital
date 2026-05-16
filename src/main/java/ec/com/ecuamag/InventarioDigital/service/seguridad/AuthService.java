package ec.com.ecuamag.InventarioDigital.service.seguridad;

import ec.com.ecuamag.InventarioDigital.model.seguridad.Usuario;
import ec.com.ecuamag.InventarioDigital.repository.seguridad.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Map<String, String> sesiones = new ConcurrentHashMap<>();

    public AuthService(UsuarioRepository usuarioRepository, JdbcTemplate jdbcTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void crearAdminInicial() {
        crearTablasSeguridad();
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("Diego");
            admin.setPasswordHash(hash("Casagallo2016"));
            admin.setActivo(true);
            usuarioRepository.save(admin);

            // SEGUNDO USUARIO
            Usuario admin2 = new Usuario();
            admin2.setUsername("Camila");
            admin2.setPasswordHash(hash("Sevilla"));
            admin2.setActivo(true);

            usuarioRepository.save(admin2);
            
        }
    }

    private void crearTablasSeguridad() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                    id BIGSERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password_hash VARCHAR(64) NOT NULL,
                    activo BOOLEAN NOT NULL DEFAULT TRUE
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS registro_inventario (
                    id BIGSERIAL PRIMARY KEY,
                    usuario VARCHAR(50) NOT NULL,
                    accion VARCHAR(20) NOT NULL,
                    inventario VARCHAR(30) NOT NULL,
                    numero VARCHAR(30) NOT NULL,
                    nombre VARCHAR(150),
                    detalle VARCHAR(500),
                    fecha TIMESTAMP NOT NULL
                )
                """);
    }

    public LoginResponse login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .filter(Usuario::isActivo)
                .filter(u -> u.getPasswordHash().equals(hash(password)))
                .orElseThrow(() -> new IllegalArgumentException("Usuario o clave incorrectos"));

        String token = UUID.randomUUID().toString();
        sesiones.put(token, usuario.getUsername());
        return new LoginResponse(token, usuario.getUsername());
    }

    public void logout(String token) {
        if (token != null) {
            sesiones.remove(token);
        }
    }

    public String requerirUsuario(HttpServletRequest request) {
        String token = request.getHeader("X-Auth-Token");
        String username = sesiones.get(token);
        if (username == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta acción");
        }
        return username;
    }

    public String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo calcular el hash", e);
        }
    }

    public record LoginResponse(String token, String username) {
    }
}
