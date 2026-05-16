package ec.com.ecuamag.InventarioDigital.model.modelTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "troquel",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_troquel_inv_num_suf",
                columnNames = {"inventario", "numero", "sufijo"}
        )
)
public abstract class Troquel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    @Column(length = 5)
    private String sufijo = "";

    private String descripcion;

    @Column(precision = 4, scale = 1)
    private BigDecimal ancho;

    @Column(precision = 4, scale = 1)
    private BigDecimal largo;

    @Column(precision = 4, scale = 1)
    private BigDecimal tamanioCorteAncho;

    @Column(precision = 4, scale = 1)
    private BigDecimal tamanioCorteLargo;

    @Enumerated(EnumType.STRING)
    private Inventario inventario;

    @Enumerated(EnumType.STRING)
    private TipoTroquel tipo;

    @Transient
    public String getNumeroCompleto() {
        return numero + (sufijo == null ? "" : sufijo.trim());
    }
}
