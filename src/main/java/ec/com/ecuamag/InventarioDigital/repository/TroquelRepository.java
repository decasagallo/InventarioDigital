package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TroquelRepository extends JpaRepository<Troquel, Long> {

    // Consulta para filtrar por inventario (grande o pequeño)
    public List<Troquel> findByInventarioOrderByNumeroAsc(Inventario inventario);

    // Consulta para filtrar por inventario y tipo de troquel
    public List<Troquel> findByInventarioAndTipoOrderByNumeroAsc(Inventario inventario, TipoTroquel tipo);
}
