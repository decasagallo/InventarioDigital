package ec.com.ecuamag.InventarioDigital.repository;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

    public interface TroquelRepository extends JpaRepository<Troquel, Long>, JpaSpecificationExecutor<Troquel> {

        // Filtrar por inventario y ordenar por número
        List<Troquel> findByInventarioOrderByNumeroAsc(Inventario inventario);

        // Filtrar por inventario y tipo de troquel, ordenar por número
        List<Troquel> findByInventarioAndTipoOrderByNumeroAsc(Inventario inventario, TipoTroquel tipo);

    }
