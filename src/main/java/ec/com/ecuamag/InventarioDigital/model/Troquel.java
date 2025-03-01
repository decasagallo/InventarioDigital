package ec.com.ecuamag.InventarioDigital.model;

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
public abstract class Troquel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;
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
}
