package ec.com.ecuamag.InventarioDigital.controller.cliseController;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.service.serviceClise.ClienteCliseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clises")
@CrossOrigin(origins = "*")
public class ClienteCliseController {

    private final ClienteCliseService service;

    public ClienteCliseController(ClienteCliseService service) {
        this.service = service;
    }

    // 🔍 Buscar por nombre
    @GetMapping("/buscar")
    public List<ClienteClise> buscar(@RequestParam(required = false) String nombre) {
        return service.buscarPorNombre(nombre);
    }

    // ➕ Agregar clisé
    @PostMapping("/agregar")
    public ClienteClise agregar(
            @RequestParam String nombreCliente,
            @RequestParam String tipo // IMPRESION o REPUJADO
    ) {
        return service.agregarClise(nombreCliente, tipo);
    }
}
