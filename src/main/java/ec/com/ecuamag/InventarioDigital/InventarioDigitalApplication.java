package ec.com.ecuamag.InventarioDigital;

import ec.com.ecuamag.InventarioDigital.service.serviceClise.CsvImportClienteCliseService;
import ec.com.ecuamag.InventarioDigital.service.serviceClise.CsvImportCliseService;
import ec.com.ecuamag.InventarioDigital.service.servicePlaca.CsvImportPlacaService;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.CsvImportTroquelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventarioDigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioDigitalApplication.class, args);
	}

	@Bean
	CommandLineRunner importarTroqueles(CsvImportTroquelService service) {
		return args -> service.importarDatosDesdeCsv();
	}

	@Bean
	CommandLineRunner importarClises(CsvImportCliseService service) {
		return args -> service.importarDatosDesdeCsv();
	}

	@Bean
	CommandLineRunner importarClienteClises(CsvImportClienteCliseService service) {
		return args -> service.importarDesdeCsv();
	}

	@Bean
	CommandLineRunner importarPlacas(CsvImportPlacaService service) {
		return args -> service.importarDesdeCsv();
	}
}
