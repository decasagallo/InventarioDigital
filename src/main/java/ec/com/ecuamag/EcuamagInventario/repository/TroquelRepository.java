package ec.com.ecuamag.EcuamagInventario.repository;

import ec.com.ecuamag.EcuamagInventario.model.Troquel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TroquelRepository extends JpaRepository<Troquel, Long>, JpaSpecificationExecutor<Troquel> {
}
