package ec.com.ecuamag.InventarioDigital.repository.repositoryClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteCliseRepository extends JpaRepository<ClienteClise, Long> {

    List<ClienteClise> findByNombreClienteContainingIgnoreCase(String nombre);

    Optional<ClienteClise> findByNombreClienteIgnoreCase(String nombre);

    boolean existsByLetraAndNumero(String letra, Integer numero);
}
