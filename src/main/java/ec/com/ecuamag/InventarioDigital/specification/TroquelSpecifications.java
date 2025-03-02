package ec.com.ecuamag.InventarioDigital.specification;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class TroquelSpecifications {

    public static Specification<Troquel> tieneInventario(Inventario inventario) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("inventario"), inventario);
    }

    public static Specification<Troquel> tieneTipo(TipoTroquel tipo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipo"), tipo);
    }

    public static Specification<Troquel> tieneOrientacion(Orientacion orientacion) {
        return (root, query, criteriaBuilder) -> {
            // Usamos "root.get" para acceder a la subclase "Sobre"
            return criteriaBuilder.equal(root.get("orientacion"), orientacion);
        };
    }

    public static Specification<Troquel> tieneTipoSolapa(TipoSolapa tipoSolapa) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("tipoSolapa"), tipoSolapa);
        };
    }

    public static Specification<Troquel> tieneMedioSobre(Boolean medioSobre) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("medioSobre"), medioSobre);
        };
    }

    public static Specification<Troquel> tieneAncho(BigDecimal ancho) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("ancho"), ancho);
        };
    }

    public static Specification<Troquel> tieneLargo(BigDecimal largo) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("largo"), largo);
        };
    }
}
