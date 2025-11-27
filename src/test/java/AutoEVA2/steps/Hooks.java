package AutoEVA2.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class Hooks {

    private static WebDriver driver;

    @Before
    public void setUp() {
        // Configuración unificada del navegador
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        // Usar solo WebDriverWait en los steps, pero mantenemos un implicit bajo por seguridad
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @After
    public void tearDown(Scenario scenario) {
        // === GENERACIÓN DE EVIDENCIA (Estudiante 1) ===
        // Si el escenario falla, tomamos foto y la adjuntamos al reporte
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Evidencia de Fallo - " + scenario.getName());
        }

        // Opcional: Tomar foto siempre al final (descomentar si se requiere evidencia de éxito también)
        // final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        // scenario.attach(screenshot, "image/png", "Evidencia Final - " + scenario.getName());

        if (driver != null) {
            driver.quit();
        }
    }

    // Método estático para que los Steps puedan usar el driver
    public static WebDriver getDriver() {
        return driver;
    }
}