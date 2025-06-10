package ec.com.ecuamag.InventarioDigital.model.modelTroquel;

import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Forma extends Troquel {
    @Enumerated(EnumType.STRING)
    private TipoForma tipoForma;
}
