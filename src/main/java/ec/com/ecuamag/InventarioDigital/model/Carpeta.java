package ec.com.ecuamag.InventarioDigital.model;

import ec.com.ecuamag.InventarioDigital.enums.TipoCarpeta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Carpeta extends Troquel {

    @Enumerated(EnumType.STRING)
    private TipoCarpeta tipoCarpeta;
}
