package ec.com.ecuamag.InventarioDigital.service.serviceClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.repository.repositoryClise.ClienteCliseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ClienteCliseService {

    private final ClienteCliseRepository repository;

    public ClienteCliseService(ClienteCliseRepository repository) {
        this.repository = repository;
    }

    // 🔍 Buscar por nombre
    public List<ClienteClise> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(ClienteClise::getLetra)
                            .thenComparing(ClienteClise::getNumero))
                    .toList();
        }
        return repository.findByNombreClienteContainingIgnoreCase(nombre);
    }

    // ➕ Agregar clisé (impresión o repujado)
    public ClienteClise agregarClise(String nombreCliente, String tipo) {

        ClienteClise cliente = repository.findByNombreClienteIgnoreCase(nombreCliente)
                .orElseGet(() -> crearNuevoCliente(nombreCliente));

        if ("IMPRESION".equalsIgnoreCase(tipo)) {
            cliente.setImpresion(cliente.getImpresion() + 1);
        } else if ("REPUJADO".equalsIgnoreCase(tipo)) {
            cliente.setRepujado(cliente.getRepujado() + 1);
        }

        return repository.save(cliente);
    }

    // 🔢 Crear cliente nuevo con letra y número automático
    private ClienteClise crearNuevoCliente(String nombreCliente) {
        String letra = nombreCliente.substring(0, 1).toUpperCase();

        int siguienteNumero = repository.findAll().stream()
                .filter(c -> c.getLetra().equals(letra))
                .map(ClienteClise::getNumero)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        ClienteClise cliente = new ClienteClise();
        cliente.setNombreCliente(nombreCliente);
        cliente.setLetra(letra);
        cliente.setNumero(siguienteNumero);
        cliente.setImpresion(0);
        cliente.setRepujado(0);

        return cliente;
    }
}
