package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.Carpeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Long> {

    @Query("SELECT c FROM Carpeta c " +
            "WHERE (:tipoCarpeta IS NULL OR c.tipoCarpeta = :tipoCarpeta) " +
            "AND (:ancho IS NULL OR c.ancho = :ancho) " +
            "AND (:largo IS NULL OR c.largo = :largo) " +
            "AND (:inventario IS NULL OR c.inventario = :inventario) " +
            "ORDER BY c.numero ASC")
    List<Carpeta> filtrarCarpetas(@Param("tipoCarpeta") TipoCarpeta tipoCarpeta,
                                  @Param("ancho") BigDecimal ancho,
                                  @Param("largo") BigDecimal largo,
                                  @Param("inventario") Inventario inventario);
}
