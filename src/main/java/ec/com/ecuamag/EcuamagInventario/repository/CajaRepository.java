package ec.com.ecuamag.EcuamagInventario.repository;

import ec.com.ecuamag.EcuamagInventario.model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
}
