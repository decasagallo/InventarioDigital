package ec.com.ecuamag.InventarioDigital.model.modelTroquel;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Bolsa extends Troquel {
    private BigDecimal alto;
}
