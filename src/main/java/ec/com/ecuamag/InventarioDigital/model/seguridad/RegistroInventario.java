package ec.com.ecuamag.InventarioDigital.model.seguridad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "registro_inventario")
public class RegistroInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(nullable = false, length = 20)
    private String accion;

    @Column(nullable = false, length = 30)
    private String inventario;

    @Column(nullable = false, length = 30)
    private String numero;

    @Column(length = 150)
    private String nombre;

    @Column(length = 500)
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
