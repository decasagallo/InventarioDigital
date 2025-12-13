package ec.com.ecuamag.EcuamagInventario.model;

import ec.com.ecuamag.EcuamagInventario.enums.Orientacion;
import ec.com.ecuamag.EcuamagInventario.enums.TipoSolapa;
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
    private Orientacion orientacion;

    @Enumerated(EnumType.STRING)
    private TipoSolapa tipoSolapa;

    private boolean medioSobre;
}
