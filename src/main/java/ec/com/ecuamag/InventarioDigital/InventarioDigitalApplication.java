package ec.com.ecuamag.InventarioDigital;

import ec.com.ecuamag.InventarioDigital.service.serviceClise.CsvImportClienteCliseService;
import ec.com.ecuamag.InventarioDigital.service.serviceClise.CsvImportCliseService;
import ec.com.ecuamag.InventarioDigital.service.servicePlaca.CsvImportPlacaService;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.CsvImportTroquelService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventarioDigitalApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		SpringApplication.run(InventarioDigitalApplication.class, args);
	}

	@Bean
	CommandLineRunner importarTroqueles(CsvImportTroquelService csvImportTroquelService) {
		return args -> {
			csvImportTroquelService.importarDatosDesdeCsv(); // Eliminado el parámetro filePath
			System.out.println("📥 Importación de troqueles desde CSV completada.");
		};
	}


	@Bean
	CommandLineRunner importarClises(CsvImportTroquelService csvImportTroquelService,
						  CsvImportCliseService csvImportCliseService) {
		return args -> {
			csvImportCliseService.importarDatosDesdeCsv(); // 🆕 se ejecuta al iniciar
			System.out.println("📥 Importación de clises desde csv completada.");
		};
	}

	@Bean
	CommandLineRunner importarClienteClises(
			CsvImportClienteCliseService service
	) {
		return args -> {
			service.importarDesdeCsv();
			System.out.println("📥 Importación de clises  desde csv completada.");
		};
	}

	@Bean
	CommandLineRunner importarPlacas(CsvImportPlacaService service) {
		return args -> {
			service.importarDesdeCsv();
			System.out.println("📥 Importación de placas desde CSV completada.");
		};
	}



}
