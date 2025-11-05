package AutoEVA2.steps;

// === Imports de Selenium y WebDriverManager ===
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Alert; // Import para la Alerta
import org.openqa.selenium.support.ui.Select; // Import para el Dropdown

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
import java.time.Duration;

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

    @Then("la pagina debe contener el texto {string}")
    public void la_pagina_debe_contener_el_texto(String textoEsperado) {
        // Agregamos una pequeña pausa de 1 seg solo para asegurar
        // que el texto dinámico se refresque después de una acción.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // La espera implícita esperará a que el "body" exista
        String bodyText = driver.findElement(By.tagName("body")).getText();

        // Verificamos si el texto esperado está en la página
        Assert.assertTrue(
                "Verificación fallida: El texto '" + textoEsperado + "' NO se encontró en la página.",
                bodyText.contains(textoEsperado)
        );
    }

    @Then("La pagina deve mostrar el aviso {string}")
    public void la_pagina_deve_mostrar_el_aviso(String textoEsperado) {
        String textoActual = closeAlertAndGetItsText(); //obtenemos el texto
        assertEquals(textoEsperado, textoActual); //lo comparamos con texto esperado
    }

    @When("seleccionamos en el dropdown {string} el texto visible {string}")
    public void seleccionamos_en_el_dropdown_el_texto_visible(String xpath, String textoVisible) {
        // La espera implícita esperará a que el dropdown exista
        Select dropdown = new Select(driver.findElement(By.xpath(xpath)));
        dropdown.selectByVisibleText(textoVisible);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private String closeAlertAndGetItsText() {
        try {
            // Pausa "tonta" para esperar que la alerta aparezca
            Thread.sleep(1000);

            // Cambiar el foco del driver a la alerta
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;

        } catch (Exception e) {
            Assert.fail("No se encontró ninguna alerta: " + e.getMessage());
            return null;
        }
    }
}