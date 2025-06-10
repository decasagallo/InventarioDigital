package ec.com.ecuamag.InventarioDigital.controller.cliseController;
import ec.com.ecuamag.InventarioDigital.model.modelClise.Clise;
import ec.com.ecuamag.InventarioDigital.service.serviceClise.CliseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clises")
@CrossOrigin(origins = "*")
public class CliseController {

    private final CliseService cliseService;

    public CliseController(CliseService cliseService) {
        this.cliseService = cliseService;
    }

    @GetMapping("/filtrar")
    public List<Clise> filtrarClises(
            @RequestParam(required = false) String letra,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String nombre
    ) {
        return cliseService.filtrarClises(letra, numero, nombre);
    }
}