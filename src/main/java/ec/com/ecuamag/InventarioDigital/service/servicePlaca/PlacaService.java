package ec.com.ecuamag.InventarioDigital.service.sevicePlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca.PlacaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacaService {

    private final PlacaRepository repo;

    public PlacaService(PlacaRepository repo) {
        this.repo = repo;
    }

    public List<Placa> buscar(String cliente, String descripcion) {
        String c = (cliente == null || cliente.isBlank()) ? null : cliente.trim();
        String d = (descripcion == null || descripcion.isBlank()) ? null : descripcion.trim();
        return repo.buscar(c, d);
    }

    // Crea una placa nueva con número consecutivo
    public Placa crear(String cliente, String descripcion, Integer cantidad) {
        int nextNumero = repo.findTopByOrderByNumeroDesc()
                .map(p -> p.getNumero() + 1)
                .orElse(1);

        Placa p = new Placa();
        p.setNumero(nextNumero);
        p.setCliente(cliente.trim());
        p.setDescripcion(descripcion.trim());
        p.setCantidad(cantidad == null || cantidad < 1 ? 1 : cantidad);

        return repo.save(p);
    }
}
