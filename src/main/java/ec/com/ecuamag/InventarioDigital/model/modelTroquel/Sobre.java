package ec.com.ecuamag.InventarioDigital.model.modelTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Sobre extends Troquel {

    @Enumerated(EnumType.STRING)
    private TipoSobre tipoSobre;

    @Enumerated(EnumType.STRING)
    private Orientacion orientacion;

    @Enumerated(EnumType.STRING)
    private TipoSolapa tipoSolapa;

}
