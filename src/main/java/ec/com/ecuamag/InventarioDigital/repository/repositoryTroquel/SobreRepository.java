package ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Sobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SobreRepository extends JpaRepository<Sobre, Long> {

    @Query("SELECT s FROM Sobre s " +
            "WHERE (:tipoSobre IS NULL OR s.tipoSobre = :tipoSobre) " +  // 👈 Ahora es opcional            "AND (:orientacion IS NULL OR s.orientacion = :orientacion) " +
            "AND (:orientacion IS NULL OR s.orientacion = :orientacion) " +
            "AND (:tipoSolapa IS NULL OR s.tipoSolapa = :tipoSolapa) " +
            "AND (:ancho IS NULL OR CAST(s.ancho AS string) LIKE CONCAT(CAST(:ancho AS string), '%')) " +
            "AND (:largo IS NULL OR CAST(s.largo AS string) LIKE CONCAT(CAST(:largo AS string), '%')) " +
            "AND (:inventario IS NULL OR s.inventario = :inventario) " +  // 👈 Filtro por Inventario agregado
            "ORDER BY s.numero ASC")
    List<Sobre> filtrarSobres(@Param("tipoSobre") TipoSobre tipoSobre,
                              @Param("orientacion") Orientacion orientacion,
                              @Param("tipoSolapa") TipoSolapa tipoSolapa,
                              @Param("ancho") BigDecimal ancho,
                              @Param("largo") BigDecimal largo,
                              @Param("inventario") Inventario inventario);  // 👈 Agregado Inventario
    Optional<Sobre> findByNumero(Integer numero);

}
