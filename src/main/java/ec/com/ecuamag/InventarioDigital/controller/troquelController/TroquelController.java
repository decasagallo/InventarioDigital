package ec.com.ecuamag.InventarioDigital.controller.troquelController;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.*;
import ec.com.ecuamag.InventarioDigital.service.seguridad.AuthService;
import ec.com.ecuamag.InventarioDigital.service.seguridad.RegistroInventarioService;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.TroquelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/troqueles")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class TroquelController {

    private final TroquelService troquelService;
    private final AuthService authService;
    private final RegistroInventarioService registroService;

    public TroquelController(TroquelService troquelService, AuthService authService, RegistroInventarioService registroService) {
        this.troquelService = troquelService;
        this.authService = authService;
        this.registroService = registroService;
    }

    // Obtener todos los troqueles ordenados de menor a mayor por número
    @GetMapping
    public List<Troquel> obtenerTodosLosTroqueles() {
        return troquelService.getTodosLosTroqueles();
    }

    // Filtrar por inventario y devolver ordenado de menor a mayor por número
    @GetMapping("/filtrar/inventario")
    public List<Troquel> filtrarPorInventario(@RequestParam Inventario inventario) {
        return troquelService.getTroquelesPorInventario(inventario);
    }

    // Filtrar por inventario y tipo, devolver ordenado de menor a mayor por número
    @GetMapping("/filtrar/inventario-y-tipo")
    public List<Troquel> filtrarPorInventarioYTipo(@RequestParam Inventario inventario,
                                                   @RequestParam TipoTroquel tipo) {
        return troquelService.getTroquelesPorInventarioYTipo(inventario, tipo);
    }

    @GetMapping("/buscar")
    public List<Troquel> buscarTroqueles(@RequestParam String descripcion) {
        return troquelService.buscarPorDescripcion(descripcion);
    }

    @GetMapping("/filtrar")
    public List<Troquel> filtrarTroquelesGenerico(
            @RequestParam(required = false) Inventario inventario,
            @RequestParam(required = false) TipoTroquel tipo,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo) {
        return troquelService.filtrarTroquelesGenerico(inventario, tipo, ancho, largo);
    }

    @GetMapping("/ultimo-numero")
    public int obtenerUltimoNumero(@RequestParam Inventario inventario) {
        return troquelService.getUltimoNumeroPorInventario(inventario);
    }

    @PostMapping
    public Troquel crearTroquel(@RequestBody CrearTroquelRequest request, HttpServletRequest httpRequest) {
        String usuario = authService.requerirUsuario(httpRequest);
        Troquel troquel = crearInstanciaPorTipo(request.getTipo());
        troquel.setNumero(request.getNumero());
        troquel.setSufijo(request.getSufijo() == null ? "" : request.getSufijo().trim().toUpperCase());
        troquel.setDescripcion(request.getDescripcion());
        troquel.setAncho(request.getAncho());
        troquel.setLargo(request.getLargo());
        troquel.setTamanioCorteAncho(request.getTamanioCorteAncho());
        troquel.setTamanioCorteLargo(request.getTamanioCorteLargo());
        troquel.setInventario(request.getInventario());
        troquel.setTipo(request.getTipo());

        if (troquel instanceof Sobre sobre) {
            sobre.setTipoSobre(request.getTipoSobre());
            sobre.setOrientacion(request.getOrientacion());
            sobre.setTipoSolapa(request.getTipoSolapa());
        } else if (troquel instanceof Forma forma) {
            forma.setTipoForma(request.getTipoForma());
        } else if (troquel instanceof Bolsa bolsa) {
            bolsa.setAlto(request.getAlto());
        } else if (troquel instanceof Caja caja) {
            caja.setAlto(request.getAlto());
        }

        Troquel guardado = troquelService.guardarTroquel(troquel);
        registroService.registrar(
                usuario,
                "CREAR",
                "TROQUEL",
                guardado.getNumeroCompleto(),
                guardado.getDescripcion(),
                "Creó troquel " + guardado.getNumeroCompleto() + " de tipo " + guardado.getTipo()
        );
        return guardado;
    }

    @PutMapping("/{id}")
    public Troquel actualizarTroquel(@PathVariable Long id, @RequestBody CrearTroquelRequest request, HttpServletRequest httpRequest) {
        String usuario = authService.requerirUsuario(httpRequest);
        Troquel troquel = troquelService.getPorId(id);
        troquel.setNumero(request.getNumero());
        troquel.setSufijo(request.getSufijo() == null ? "" : request.getSufijo().trim().toUpperCase());
        troquel.setDescripcion(request.getDescripcion());
        troquel.setAncho(request.getAncho());
        troquel.setLargo(request.getLargo());
        troquel.setTamanioCorteAncho(request.getTamanioCorteAncho());
        troquel.setTamanioCorteLargo(request.getTamanioCorteLargo());
        troquel.setInventario(request.getInventario());

        if (troquel instanceof Sobre sobre) {
            sobre.setTipoSobre(request.getTipoSobre());
            sobre.setOrientacion(request.getOrientacion());
            sobre.setTipoSolapa(request.getTipoSolapa());
        } else if (troquel instanceof Forma forma) {
            forma.setTipoForma(request.getTipoForma());
        } else if (troquel instanceof Bolsa bolsa) {
            bolsa.setAlto(request.getAlto());
        } else if (troquel instanceof Caja caja) {
            caja.setAlto(request.getAlto());
        }

        Troquel guardado = troquelService.guardarTroquel(troquel);
        registroService.registrar(
                usuario,
                "EDITAR",
                "TROQUEL",
                guardado.getNumeroCompleto(),
                guardado.getDescripcion(),
                "Actualizó troquel " + guardado.getNumeroCompleto()
        );
        return guardado;
    }

    @DeleteMapping("/{id}")
    public void eliminarTroquel(@PathVariable Long id, HttpServletRequest request) {
        String usuario = authService.requerirUsuario(request);
        Troquel eliminado = troquelService.eliminar(id);
        registroService.registrar(
                usuario,
                "ELIMINAR",
                "TROQUEL",
                eliminado.getNumeroCompleto(),
                eliminado.getDescripcion(),
                "Eliminó troquel " + eliminado.getNumeroCompleto() + " de tipo " + eliminado.getTipo()
        );
    }

    private Troquel crearInstanciaPorTipo(TipoTroquel tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de troquel es obligatorio");
        }

        return switch (tipo) {
            case SOBRE -> new Sobre();
            case BOLSA -> new Bolsa();
            case CAJA -> new Caja();
            case CARPETA -> new Carpeta();
            case FORMA -> new Forma();
            case FUNDA -> new Funda();
            default -> throw new IllegalArgumentException("Tipo de troquel no soportado: " + tipo);
        };
    }

    public static class CrearTroquelRequest {
        private Integer numero;
        private String sufijo;
        private String descripcion;
        private BigDecimal ancho;
        private BigDecimal largo;
        private BigDecimal tamanioCorteAncho;
        private BigDecimal tamanioCorteLargo;
        private BigDecimal alto;
        private TipoSobre tipoSobre;
        private Orientacion orientacion;
        private TipoSolapa tipoSolapa;
        private TipoForma tipoForma;
        private Inventario inventario;
        private TipoTroquel tipo;

        public Integer getNumero() {
            return numero;
        }

        public void setNumero(Integer numero) {
            this.numero = numero;
        }

        public String getSufijo() {
            return sufijo;
        }

        public void setSufijo(String sufijo) {
            this.sufijo = sufijo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public BigDecimal getAncho() {
            return ancho;
        }

        public void setAncho(BigDecimal ancho) {
            this.ancho = ancho;
        }

        public BigDecimal getLargo() {
            return largo;
        }

        public void setLargo(BigDecimal largo) {
            this.largo = largo;
        }

        public BigDecimal getTamanioCorteAncho() {
            return tamanioCorteAncho;
        }

        public void setTamanioCorteAncho(BigDecimal tamanioCorteAncho) {
            this.tamanioCorteAncho = tamanioCorteAncho;
        }

        public BigDecimal getTamanioCorteLargo() {
            return tamanioCorteLargo;
        }

        public void setTamanioCorteLargo(BigDecimal tamanioCorteLargo) {
            this.tamanioCorteLargo = tamanioCorteLargo;
        }

        public BigDecimal getAlto() {
            return alto;
        }

        public void setAlto(BigDecimal alto) {
            this.alto = alto;
        }

        public TipoSobre getTipoSobre() {
            return tipoSobre;
        }

        public void setTipoSobre(TipoSobre tipoSobre) {
            this.tipoSobre = tipoSobre;
        }

        public Orientacion getOrientacion() {
            return orientacion;
        }

        public void setOrientacion(Orientacion orientacion) {
            this.orientacion = orientacion;
        }

        public TipoSolapa getTipoSolapa() {
            return tipoSolapa;
        }

        public void setTipoSolapa(TipoSolapa tipoSolapa) {
            this.tipoSolapa = tipoSolapa;
        }

        public TipoForma getTipoForma() {
            return tipoForma;
        }

        public void setTipoForma(TipoForma tipoForma) {
            this.tipoForma = tipoForma;
        }

        public Inventario getInventario() {
            return inventario;
        }

        public void setInventario(Inventario inventario) {
            this.inventario = inventario;
        }

        public TipoTroquel getTipo() {
            return tipo;
        }

        public void setTipo(TipoTroquel tipo) {
            this.tipo = tipo;
        }
    }

}

