package ec.com.ecuamag.InventarioDigital.service.seguridad;

import ec.com.ecuamag.InventarioDigital.model.seguridad.RegistroInventario;
import ec.com.ecuamag.InventarioDigital.repository.seguridad.RegistroInventarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroInventarioService {

    private final RegistroInventarioRepository repository;

    public RegistroInventarioService(RegistroInventarioRepository repository) {
        this.repository = repository;
    }

    public void registrar(String usuario, String accion, String inventario, String numero, String nombre, String detalle) {
        RegistroInventario registro = new RegistroInventario();
        registro.setUsuario(usuario);
        registro.setAccion(accion);
        registro.setInventario(inventario);
        registro.setNumero(numero);
        registro.setNombre(nombre);
        registro.setDetalle(detalle);
        registro.setFecha(LocalDateTime.now());
        repository.save(registro);
    }

    public List<RegistroInventario> ultimos() {
        return repository.findTop100ByOrderByFechaDesc();
    }
}
