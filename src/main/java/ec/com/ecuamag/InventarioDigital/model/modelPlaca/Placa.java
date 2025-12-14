package ec.com.ecuamag.InventarioDigital.model.modelPlaca;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "placa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"numero"})
)
public class Placa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero; // consecutivo global

    @Column(name = "cliente", nullable = false)
    private String cliente;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;
}
