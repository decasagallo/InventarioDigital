package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Bolsa;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.BolsaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class BolsaService {

    private final BolsaRepository bolsaRepository;

    public BolsaService(BolsaRepository bolsaRepository) {
        this.bolsaRepository = bolsaRepository;
    }

    public List<Bolsa> filtrarBolsas(BigDecimal ancho, BigDecimal largo, BigDecimal alto, Inventario inventario) {
        return bolsaRepository.filtrarBolsas( ancho, largo, alto, inventario);
    }
}
