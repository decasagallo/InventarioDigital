package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.model.Forma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaRepository extends JpaRepository<Forma, Long> {
}
