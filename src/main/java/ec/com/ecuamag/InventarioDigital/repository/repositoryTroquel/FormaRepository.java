package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Forma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FormaRepository extends JpaRepository<Forma, Long> {

    @Query("SELECT f FROM Forma f " +
            "WHERE (:tipoForma IS NULL OR f.tipoForma = :tipoForma) " +
            "AND (:ancho IS NULL OR f.ancho = :ancho) " +
            "AND (:largo IS NULL OR f.largo = :largo) " +
            "AND (:inventario IS NULL OR f.inventario = :inventario) " +
            "ORDER BY f.numero ASC")
    List<Forma> filtrarFormas(@Param("tipoForma") TipoForma tipoForma,
                              @Param("ancho") BigDecimal ancho,
                              @Param("largo") BigDecimal largo,
                              @Param("inventario") Inventario inventario);

}
