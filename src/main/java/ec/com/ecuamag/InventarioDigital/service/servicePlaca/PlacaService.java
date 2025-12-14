package ec.com.ecuamag.InventarioDigital.service.servicePlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca.PlacaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacaService {

    private final PlacaRepository placaRepository;

    public PlacaService(PlacaRepository placaRepository) {
        this.placaRepository = placaRepository;
    }

    // ✅ LISTAR TODAS
    public List<Placa> listarTodas() {
        return placaRepository.findAllByOrderByNumero();
    }

    // 🔍 BUSCAR
    public List<Placa> buscar(String q) {
        if (q == null || q.isBlank()) {
            return placaRepository.findAllByOrderByNumero();
        }
        return placaRepository.buscar(q);
    }
}
