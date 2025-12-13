package ec.com.ecuamag.EcuamagInventario.repository;

import ec.com.ecuamag.EcuamagInventario.model.Forma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaRepository extends JpaRepository<Forma, Long> {
}
