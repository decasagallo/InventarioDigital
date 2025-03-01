package ec.com.ecuamag.InventarioDigital.specification;

import ec.com.ecuamag.InventarioDigital.model.Troquel;
import org.springframework.data.jpa.domain.Specification;

public class TroquelSpecification {

    public static Specification<Troquel> filtrarPorDimensiones(Integer ancho, Integer alto, Integer largo) {
        return (root, query, criteriaBuilder) -> {
            if (ancho == null && alto == null && largo == null) {
                return criteriaBuilder.conjunction(); // No aplica filtros
            }

            var predicate = criteriaBuilder.conjunction();

            if (ancho != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("ancho"), ancho));
            }
            if (alto != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("alto"), alto));
            }
            if (largo != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("largo"), largo));
            }

            return predicate;
        };
    }
}
