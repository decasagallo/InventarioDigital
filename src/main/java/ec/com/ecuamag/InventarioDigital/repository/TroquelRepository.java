package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TroquelRepository extends JpaRepository<Troquel, Long> {
    List<Troquel> findByInventario(Inventario inventario, Sort sort);
    List<Troquel> findByInventarioAndTipo(Inventario inventario, TipoTroquel tipo, Sort sort);
}
