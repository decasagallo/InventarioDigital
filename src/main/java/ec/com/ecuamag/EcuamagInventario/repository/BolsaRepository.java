package ec.com.ecuamag.EcuamagInventario.repository;

import ec.com.ecuamag.EcuamagInventario.model.Bolsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BolsaRepository extends JpaRepository<Bolsa, Long> {
}
