package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Caja;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.CajaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;

    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    public List<Caja> filtrarCajas(BigDecimal ancho, BigDecimal largo, BigDecimal alto, Inventario inventario) {
        return cajaRepository.filtrarCajas(ancho, largo, alto, inventario);
    }
}
