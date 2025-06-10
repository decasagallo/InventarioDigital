package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Funda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FundaRepository extends JpaRepository<Funda, Long> {

    @Query("SELECT f FROM Funda f " +
            "WHERE (:ancho IS NULL OR f.ancho = :ancho) " +
            "AND (:largo IS NULL OR f.largo = :largo) " +
            "AND (:inventario IS NULL OR f.inventario = :inventario) " +
            "ORDER BY f.numero ASC")
    List<Funda> filtrarFundas(@Param("ancho") BigDecimal ancho,
                                  @Param("largo") BigDecimal largo,
                                  @Param("inventario") Inventario inventario);

}
