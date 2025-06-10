package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Funda;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.FundaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundaService {

    private final FundaRepository fundaRepository;

    public FundaService(FundaRepository fundaRepository) {
        this.fundaRepository = fundaRepository;
    }

    public List<Funda> filtrarFundas(BigDecimal ancho, BigDecimal largo, Inventario inventario) {
        return fundaRepository.filtrarFundas( ancho, largo, inventario);
    }
}
