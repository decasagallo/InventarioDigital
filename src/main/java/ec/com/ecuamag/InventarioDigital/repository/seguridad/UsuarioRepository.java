package ec.com.ecuamag.InventarioDigital.repository.seguridad;

import ec.com.ecuamag.InventarioDigital.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameIgnoreCase(String username);
}
