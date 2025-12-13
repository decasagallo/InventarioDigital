package ec.com.ecuamag.EcuamagInventario.model;

import ec.com.ecuamag.EcuamagInventario.enums.Inventario;
import ec.com.ecuamag.EcuamagInventario.enums.TipoTroquel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Troquel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String descripcion;

    private Integer ancho;
    private Integer alto;

    @Enumerated(EnumType.STRING)
    private Inventario inventario;

    @Enumerated(EnumType.STRING)
    private TipoTroquel tipo;
}
