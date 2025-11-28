package AutoEVA2.steps;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Ruta a tus features
        glue = "AutoEVA2.steps", // Paquete donde están tus Steps y Hooks
        plugin = {
                "pretty",
                "json:target/cucumber.json", // Importante: Genera el JSON para el reporte
                "html:target/cucumber-reports.html" // Reporte básico de respaldo
        },
        monochrome = true
)
public class RunCucumberTest {
    // Esta clase queda vacía, solo sirve para ejecutar las opciones de arriba
}
