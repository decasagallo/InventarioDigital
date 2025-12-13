package ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlacaRepository extends JpaRepository<Placa, Long> {

    @Query("""

            SELECT p FROM Placa p
WHERE (:q IS NULL
   OR LOWER(p.cliente) LIKE LOWER(CONCAT('%', :q, '%'))
   OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :q, '%')))
ORDER BY p.numero
""")
    List<Placa> buscar(@Param("q") String q);
}