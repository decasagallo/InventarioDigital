package ec.com.ecuamag.InventarioDigital.model.modelClise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clise")
public class Clise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "letra")
    private String letra;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "impresion")
    private Integer impresion;

    @Column(name = "repujado")
    private Integer repujado;
}