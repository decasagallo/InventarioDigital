package ec.com.ecuamag.InventarioDigital.repository.seguridad;

import ec.com.ecuamag.InventarioDigital.model.seguridad.RegistroInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroInventarioRepository extends JpaRepository<RegistroInventario, Long> {
    List<RegistroInventario> findTop100ByOrderByFechaDesc();
}
