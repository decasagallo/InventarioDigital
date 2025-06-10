package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {

    @Query("SELECT c FROM Caja c WHERE " +
            "(:ancho IS NULL OR c.ancho = :ancho) AND " +
            "(:largo IS NULL OR c.largo = :largo) AND " +
            "(:alto IS NULL OR c.alto = :alto) AND " +
            "(:inventario IS NULL OR c.inventario = :inventario)")
    List<Caja> filtrarCajas(
            @Param("ancho") BigDecimal ancho,
            @Param("largo") BigDecimal largo,
            @Param("alto") BigDecimal alto,
            @Param("inventario") Inventario inventario
    );
}
