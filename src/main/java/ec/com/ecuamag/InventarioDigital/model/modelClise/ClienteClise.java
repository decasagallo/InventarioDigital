package ec.com.ecuamag.InventarioDigital.model.modelClise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "cliente_clise",
        uniqueConstraints = @UniqueConstraint(columnNames = {"letra", "numero"})
)
public class ClienteClise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(nullable = false, length = 1)
    private String letra;   // A, B, C...

    @Column(nullable = false)
    private Integer numero; // correlativo por letra

    @Column(nullable = false)
    private Integer impresion = 0;

    @Column(nullable = false)
    private Integer repujado = 0;
}
