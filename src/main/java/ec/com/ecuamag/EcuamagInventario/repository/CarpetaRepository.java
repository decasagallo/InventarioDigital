package ec.com.ecuamag.EcuamagInventario.repository;

import ec.com.ecuamag.EcuamagInventario.model.Carpeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Long> {
}
