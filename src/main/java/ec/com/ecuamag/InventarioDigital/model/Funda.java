package ec.com.ecuamag.InventarioDigital.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("FUNDA") // Si usas herencia con una sola tabla
public class Funda extends Troquel {
}
