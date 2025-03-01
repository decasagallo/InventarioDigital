package ec.com.ecuamag.InventarioDigital.repository;

import ec.com.ecuamag.InventarioDigital.model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
}
