package ec.com.ecuamag.InventarioDigital.model.modelClise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Clise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ✅ Este es el identificador correcto

    @Column(name = "letra", columnDefinition = "varchar(255)")
    private String letra;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "nombre", columnDefinition = "varchar(255)")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "varchar(255)")
    private String descripcion;
}
