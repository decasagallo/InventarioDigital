package ec.com.ecuamag.InventarioDigital.model;

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
    private TipoSobre tipoSobre; // NUEVO ATRIBUTO

    @Enumerated(EnumType.STRING)
    private Orientacion orientacion; // SOLO PARA RECTANGULARES

    @Enumerated(EnumType.STRING)
    private TipoSolapa tipoSolapa;

}
