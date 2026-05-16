package ec.com.ecuamag.InventarioDigital.controller.placaController;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.service.seguridad.AuthService;
import ec.com.ecuamag.InventarioDigital.service.seguridad.RegistroInventarioService;
import ec.com.ecuamag.InventarioDigital.service.servicePlaca.PlacaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placas")
@CrossOrigin(origins = "*")
public class PlacaController {

    private final PlacaService placaService;
    private final AuthService authService;
    private final RegistroInventarioService registroService;

    public PlacaController(PlacaService placaService, AuthService authService, RegistroInventarioService registroService) {
        this.placaService = placaService;
        this.authService = authService;
        this.registroService = registroService;
    }

    @GetMapping("/buscar")
    public List<Placa> buscar(
            @RequestParam(required = false) String q
    ) {
        return placaService.buscar(q);
    }

    @GetMapping("/ultimo-numero")
    public int obtenerUltimoNumero() {
        return placaService.getUltimoNumero();
    }

    @PostMapping
    public Placa crear(@RequestBody Placa placa, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        Placa guardada = placaService.guardar(placa);
        registroService.registrar(
                usuario,
                "CREAR",
                "PLACA",
                String.valueOf(guardada.getNumero()),
                guardada.getCliente(),
                "Creó placa #" + guardada.getNumero() + " - " + guardada.getDescripcion()
        );
        return guardada;
    }

    @GetMapping("/sugerencias")
    public List<Placa> sugerencias(
            @RequestParam(required = false) String cliente,
            @RequestParam(required = false) String descripcion
    ) {
        return placaService.buscarSugerencias(cliente, descripcion);
    }

    @PostMapping("/{id}/sumar")
    public Placa sumarCantidad(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            HttpServletRequest request
    ) {
        String usuario = authService.requerirUsuario(request);
        Placa guardada = placaService.sumarCantidad(id, cantidad);
        registroService.registrar(
                usuario,
                "EDITAR",
                "PLACA",
                String.valueOf(guardada.getNumero()),
                guardada.getCliente(),
                "Sumó " + cantidad + " a placa #" + guardada.getNumero() + ". Cantidad actual: " + guardada.getCantidad()
        );
        return guardada;
    }

    @PutMapping("/{id}")
    public Placa actualizar(@PathVariable Long id, @RequestBody Placa placa, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        Placa guardada = placaService.actualizar(id, placa);
        registroService.registrar(
                usuario,
                "EDITAR",
                "PLACA",
                String.valueOf(guardada.getNumero()),
                guardada.getCliente(),
                "Actualizó placa #" + guardada.getNumero() + " - " + guardada.getDescripcion()
        );
        return guardada;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        Placa eliminada = placaService.eliminar(id);
        registroService.registrar(
                usuario,
                "ELIMINAR",
                "PLACA",
                String.valueOf(eliminada.getNumero()),
                eliminada.getCliente(),
                "Eliminó placa #" + eliminada.getNumero() + " - " + eliminada.getDescripcion()
        );
    }
}
