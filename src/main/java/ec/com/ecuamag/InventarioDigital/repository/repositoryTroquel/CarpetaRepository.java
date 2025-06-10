package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Carpeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Long> {

    @Query("SELECT c FROM Carpeta c " +
            "WHERE (:ancho IS NULL OR c.ancho = :ancho) " +
            "AND (:largo IS NULL OR c.largo = :largo) " +
            "AND (:inventario IS NULL OR c.inventario = :inventario) " +
            "ORDER BY c.numero ASC")
    List<Carpeta> filtrarCarpetas(@Param("ancho") BigDecimal ancho,
                                  @Param("largo") BigDecimal largo,
                                  @Param("inventario") Inventario inventario);
}
