package ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlacaRepository extends JpaRepository<Placa, Long> {

    List<Placa> findAllByOrderByNumero();

    @Query("""
        SELECT p FROM Placa p
        WHERE lower(p.cliente) LIKE lower(concat('%', :q, '%'))
           OR lower(p.descripcion) LIKE lower(concat('%', :q, '%'))
        ORDER BY p.numero
    """)
    List<Placa> buscar(@Param("q") String q);

}
