package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Troquel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TroquelRepository extends JpaRepository<Troquel, Long> {
    List<Troquel> findByInventario(Inventario inventario, Sort sort);
    List<Troquel> findByInventarioAndTipo(Inventario inventario, TipoTroquel tipo, Sort sort);

    // Buscar troqueles que contengan la palabra clave en la descripción (sin importar mayúsculas/minúsculas)
    List<Troquel> findByDescripcionContainingIgnoreCase(String descripcion);

    @Query("SELECT t FROM Troquel t " +
            "WHERE (:inventario IS NULL OR t.inventario = :inventario) " +
            "AND (:tipo IS NULL OR t.tipo = :tipo) " +
            "AND (:ancho IS NULL OR t.ancho = :ancho) " +
            "AND (:largo IS NULL OR t.largo = :largo) " +
            "ORDER BY t.numero ASC")
    List<Troquel> filtrarTroquelesGenerico(
            @Param("inventario") Inventario inventario,
            @Param("tipo") TipoTroquel tipo,
            @Param("ancho") BigDecimal ancho,
            @Param("largo") BigDecimal largo
    );

}
