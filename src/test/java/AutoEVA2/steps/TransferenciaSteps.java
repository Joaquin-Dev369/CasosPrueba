package AutoEVA2.steps;

// === Imports de Selenium y WebDriverManager ===
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Alert; // Import para la Alerta
import org.openqa.selenium.support.ui.Select; // Import para el Dropdown

// === NUEVOS IMPORTS PARA ESPERAS EXPLÍCITAS ===
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import java.time.Duration; // (Ya deberías tenerlo)


// === Imports de Cucumber ===
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

// === Imports de JUnit ===
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

// === Otros ===
// (java.time.Duration ya está arriba)

public class TransferenciaSteps {

    // Hacemos el driver estático para que sea accesible entre pasos
    static WebDriver driver;

    @Before
    public void setup() {
        // Configura WebDriverManager para descargar el driver
        WebDriverManager.chromedriver().setup();

        // Configura las opciones de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-infobars");
        options.addArguments("--user-data-dir=/tmp/chrome-test-profile");

        // Inicia el driver con las opciones
        driver = new ChromeDriver(options);

        // Maximiza la pantalla
        driver.manage().window().maximize();

        // Borra cookies
        driver.manage().deleteAllCookies();

        // ¡¡ESTA LÍNEA ES LA CLAVE!!
        // Le dice a Selenium que espere hasta 10 segundos por CUALQUIER
        // elemento antes de fallar. Esto soluciona los problemas de tiempo.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void teardown() throws Exception {
        // Cierra el navegador después de cada escenario
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("al navegar hasta la url {string}")
    public void al_navegar_hasta_la_url(String url) {
        driver.get(url);
    }

    @When("hacemos click en el link {string}")
    public void hacemos_click_en_el_link(String xpath) {
        // La espera implícita esperará aquí si el link no ha cargado
        driver.findElement(By.xpath(xpath)).click();
    }

    @When("coloca en el campo usuario {string} el texto {string}")
    public void coloca_en_el_campo_usuario_el_texto(String xpath, String username) {
        // La espera implícita esperará aquí
        driver.findElement(By.xpath(xpath)).click();
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(username);
    }

    @When("coloca en el campo password {string} el texto {string}")
    public void coloca_en_el_campo_password_el_texto(String xpath, String password) {
        // La espera implícita esperará aquí
        driver.findElement(By.xpath(xpath)).click();
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(password);
    }

    @When("hacer click sobre el boton Login {string}")
    public void hacer_click_sobre_el_boton_login(String xpath) {
        // La espera implícita esperará aquí
        driver.findElement(By.xpath(xpath)).click();
    }

    // ==========================================================
    // MÉTODO ACTUALIZADO CON WebDriverWait
    // ==========================================================
    @Then("la pagina debe contener el texto {string}")
    public void la_pagina_debe_contener_el_texto(String textoEsperado) {

        try {
            // 1. Crear el Wait. Esperará MÁXIMO 10 segundos.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 2. Esperar a que el texto sea visible DENTRO del body
            //    Esto reemplaza al Thread.sleep()
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), textoEsperado));

            // 3. Si el 'wait' termina sin error, el texto SÍ está.
            //    Hacemos la aserción final para estar 100% seguros.
            String bodyText = driver.findElement(By.tagName("body")).getText();
            Assert.assertTrue(
                    "Verificación fallida: El texto '" + textoEsperado + "' NO se encontró en la página.",
                    bodyText.contains(textoEsperado)
            );

        } catch (TimeoutException e) {
            // 4. Si el 'wait' falla (pasan 10s), el texto nunca apareció.
            Assert.fail(
                    "Verificación fallida: El texto '" + textoEsperado + "' NO se encontró en la página después de 10 seg."
            );
        }
    }

    // ==========================================================
    // MÉTODO ACTUALIZADO CON WebDriverWait
    // ==========================================================
    @Then("La pagina deve mostrar el aviso {string}")
    public void la_pagina_deve_mostrar_el_aviso(String textoEsperado) {
        // Obtenemos el texto usando el método auxiliar mejorado
        String textoActual = closeAlertAndGetItsText();

        // Lo comparamos con texto esperado
        assertEquals(textoEsperado, textoActual);
    }

    @When("seleccionamos en el dropdown {string} el texto visible {string}")
    public void seleccionamos_en_el_dropdown_el_texto_visible(String xpath, String textoVisible) {
        // La espera implícita esperará a que el dropdown exista
        Select dropdown = new Select(driver.findElement(By.xpath(xpath)));
        dropdown.selectByVisibleText(textoVisible);
    }

    // ==========================================================
    // MÉTODO AUXILIAR ACTUALIZADO CON WebDriverWait
    // ==========================================================
    private String closeAlertAndGetItsText() {
        try {
            // 1. Crear un WebDriverWait.
            // Esperará un MÁXIMO de 10 segundos.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 2. Esperar EXPLÍCITAMENTE a que la alerta aparezca.
            // Esto reemplaza al Thread.sleep(1000)
            // Si la alerta aparece en 0.5s, continúa inmediatamente.
            // Si no aparece en 10s, lanzará una 'TimeoutException'.
            wait.until(ExpectedConditions.alertIsPresent());

            // 3. Ahora que SABEMOS que la alerta está presente, cambiamos a ella
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;

        } catch (TimeoutException e) {
            // Esto se ejecuta si la alerta NUNCA apareció después de 10 seg
            Assert.fail("No se encontró ninguna alerta después de esperar 10 segundos: " + e.getMessage());
            return null;
        }
    }
}