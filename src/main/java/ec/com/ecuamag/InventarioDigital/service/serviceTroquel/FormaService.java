package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Forma;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.FormaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FormaService {

    private final FormaRepository formaRepository;

    public FormaService(FormaRepository formaRepository) {
        this.formaRepository = formaRepository;
    }

    public List<Forma> filtrarFormas(TipoForma tipoForma, BigDecimal ancho, BigDecimal largo, Inventario inventario) {
        return formaRepository.filtrarFormas(tipoForma, ancho, largo, inventario);
    }

}
