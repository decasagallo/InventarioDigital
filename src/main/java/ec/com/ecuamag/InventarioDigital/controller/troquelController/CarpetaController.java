package ec.com.ecuamag.InventarioDigital.controller.troquelController;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Carpeta;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.CarpetaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carpetas")
@CrossOrigin(origins = "*")
public class CarpetaController {

    private final CarpetaService carpetaService;

    public CarpetaController(CarpetaService carpetaService) {
        this.carpetaService = carpetaService;
    }

    @GetMapping("/filtrar")
    public List<Carpeta> filtrarCarpetas(
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) Inventario inventario) {

        return carpetaService.filtrarCarpetas( ancho, largo, inventario);
    }
}
