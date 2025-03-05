package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SobreRepository extends JpaRepository<Sobre, Long> {

    @Query("SELECT s FROM Sobre s " +
            "WHERE (:tipoSobre IS NULL OR s.tipoSobre = :tipoSobre) " +  // 👈 Ahora es opcional            "AND (:orientacion IS NULL OR s.orientacion = :orientacion) " +
            "AND (:orientacion IS NULL OR s.orientacion = :orientacion) " +
            "AND (:tipoSolapa IS NULL OR s.tipoSolapa = :tipoSolapa) " +
            "AND (:ancho IS NULL OR s.ancho = :ancho) " +
            "AND (:largo IS NULL OR s.largo = :largo) " +
            "AND (:inventario IS NULL OR s.inventario = :inventario) " +  // 👈 Filtro por Inventario agregado
            "ORDER BY s.numero ASC")
    List<Sobre> filtrarSobres(@Param("tipoSobre") TipoSobre tipoSobre,
                              @Param("orientacion") Orientacion orientacion,
                              @Param("tipoSolapa") TipoSolapa tipoSolapa,
                              @Param("ancho") BigDecimal ancho,
                              @Param("largo") BigDecimal largo,
                              @Param("inventario") Inventario inventario);  // 👈 Agregado Inventario
}
