package ec.com.ecuamag.InventarioDigital.model;

import ec.com.ecuamag.InventarioDigital.enums.TipoParte;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Parte extends Troquel {

    @Enumerated(EnumType.STRING)
    private TipoParte tipoParte;
}