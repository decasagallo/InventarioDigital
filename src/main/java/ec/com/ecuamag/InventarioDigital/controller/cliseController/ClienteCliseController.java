package ec.com.ecuamag.InventarioDigital.controller.cliseController;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.service.seguridad.AuthService;
import ec.com.ecuamag.InventarioDigital.service.seguridad.RegistroInventarioService;
import ec.com.ecuamag.InventarioDigital.service.serviceClise.ClienteCliseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clises")
@CrossOrigin(origins = "*")
public class ClienteCliseController {

    private final ClienteCliseService service;
    private final AuthService authService;
    private final RegistroInventarioService registroService;

    public ClienteCliseController(ClienteCliseService service, AuthService authService, RegistroInventarioService registroService) {
        this.service = service;
        this.authService = authService;
        this.registroService = registroService;
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
            @RequestParam String tipo,
            HttpServletRequest request
    ) {
        String usuario = authService.requerirUsuario(request);
        ClienteClise guardado = service.agregarClise(nombreCliente, tipo);
        registrarClise(usuario, guardado, "Agregó 1 " + tipo.toLowerCase());
        return guardado;
    }

    @PostMapping("/agregar-cantidades")
    public ClienteClise agregarCantidades(
            @RequestParam String nombreCliente,
            @RequestParam(defaultValue = "0") Integer impresion,
            @RequestParam(defaultValue = "0") Integer repujado,
            HttpServletRequest request
    ) {
        String usuario = authService.requerirUsuario(request);
        ClienteClise guardado = service.agregarCantidades(nombreCliente, impresion, repujado);
        registrarClise(usuario, guardado, "Agregó impresión: " + impresion + ", repujado: " + repujado);
        return guardado;
    }

    @GetMapping("/sugerencias")
    public List<ClienteClise> sugerencias(@RequestParam String nombre) {
        return service.buscarSugerencias(nombre);
    }

    @PutMapping("/{id}")
    public ClienteClise actualizar(@PathVariable Long id, @RequestBody ClienteClise clise, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        ClienteClise guardado = service.actualizar(id, clise);
        registroService.registrar(
                usuario,
                "EDITAR",
                "CLISE",
                guardado.getLetra() + guardado.getNumero(),
                guardado.getNombreCliente(),
                "Actualizó clisé. Impresión: " + guardado.getImpresion() + ", repujado: " + guardado.getRepujado()
        );
        return guardado;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        ClienteClise eliminado = service.eliminar(id);
        registroService.registrar(
                usuario,
                "ELIMINAR",
                "CLISE",
                eliminado.getLetra() + eliminado.getNumero(),
                eliminado.getNombreCliente(),
                "Eliminó clisé"
        );
    }

    private void registrarClise(String usuario, ClienteClise clise, String detalle) {
        registroService.registrar(
                usuario,
                "EDITAR",
                "CLISE",
                clise.getLetra() + clise.getNumero(),
                clise.getNombreCliente(),
                detalle + ". Totales: impresión " + clise.getImpresion() + ", repujado " + clise.getRepujado()
        );
    }
}
