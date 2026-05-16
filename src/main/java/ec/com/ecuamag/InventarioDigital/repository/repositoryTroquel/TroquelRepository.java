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
import java.util.Optional;

@Repository
public interface TroquelRepository extends JpaRepository<Troquel, Long> {

    List<Troquel> findByInventario(Inventario inventario, Sort sort);

    List<Troquel> findByInventarioAndTipo(Inventario inventario, TipoTroquel tipo, Sort sort);

    Optional<Troquel> findTopByInventarioOrderByNumeroDesc(Inventario inventario);

    @Query("""
        SELECT t FROM Troquel t
        WHERE t.inventario = :inventario
        AND t.numero = :numero
        AND COALESCE(t.sufijo, '') = :sufijo
    """)
    Optional<Troquel> buscarPorNumeroCompleto(
            @Param("inventario") Inventario inventario,
            @Param("numero") Integer numero,
            @Param("sufijo") String sufijo
    );

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