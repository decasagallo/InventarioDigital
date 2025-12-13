package ec.com.ecuamag.EcuamagInventario.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Bolsa extends Troquel {
    private Integer largo;
}
