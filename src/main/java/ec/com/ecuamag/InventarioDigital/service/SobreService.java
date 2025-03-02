package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.repository.SobreRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SobreService {

    private final SobreRepository sobreRepository;

    public SobreService(SobreRepository sobreRepository) {
        this.sobreRepository = sobreRepository;
    }

    public List<Sobre> filtrarSobres(Orientacion orientacion, TipoSolapa tipoSolapa, Boolean medioSobre, BigDecimal ancho, BigDecimal largo) {
        Specification<Sobre> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (orientacion != null) {
                predicates.add(cb.equal(root.get("orientacion"), orientacion));
            }
            if (tipoSolapa != null) {
                predicates.add(cb.equal(root.get("tipoSolapa"), tipoSolapa));
            }
            if (medioSobre != null) {
                predicates.add(cb.equal(root.get("medioSobre"), medioSobre));
            }
            if (ancho != null) {
                predicates.add(cb.equal(root.get("ancho"), ancho));
            }
            if (largo != null) {
                predicates.add(cb.equal(root.get("largo"), largo));
            }

            query.orderBy(cb.asc(root.get("numero"))); // 🔹 Ordenar por número de menor a mayor

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return sobreRepository.findAll(spec);
    }
}
