package ec.com.ecuamag.InventarioDigital.repository.repositoryClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.Clise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CliseRepository extends JpaRepository<Clise, Long> {

    @Query("SELECT c FROM Clise c " +
            "WHERE (:letra IS NULL OR LOWER(c.letra) = LOWER(:letra)) " +
            "AND (:numero IS NULL OR c.numero = :numero) " +
            "AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Clise> filtrarClises(
            @Param("letra") String letra,
            @Param("numero") Integer numero,
            @Param("nombre") String nombre
    );
}
