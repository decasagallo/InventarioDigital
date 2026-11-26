package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Bolsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BolsaRepository extends JpaRepository<Bolsa, Long> {
    @Query("SELECT b FROM Bolsa b WHERE " +
            "(:ancho IS NULL OR CAST(b.ancho AS string) LIKE CONCAT(CAST(:ancho AS string), '%')) AND " +
            "(:largo IS NULL OR CAST(b.largo AS string) LIKE CONCAT(CAST(:largo AS string), '%')) AND " +
            "(:alto IS NULL OR CAST(b.alto AS string) LIKE CONCAT(CAST(:alto AS string), '%')) AND " +
            "(:inventario IS NULL OR b.inventario = :inventario)")
    List<Bolsa> filtrarBolsas(
            @Param("ancho") BigDecimal ancho,
            @Param("largo") BigDecimal largo,
            @Param("alto") BigDecimal alto,
            @Param("inventario") Inventario inventario);

}
