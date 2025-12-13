package ec.com.ecuamag.EcuamagInventario.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Caja extends Troquel {
    private Integer largo;
}
