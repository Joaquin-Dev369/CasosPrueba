package AutoEVA2.steps;

// Imports de Selenium y WebDriverManager
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.Select;

// Imports para Esperas Explícitas
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import java.time.Duration;

// Imports de Cucumber
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

// Imports de JUnit
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

public class TransferenciaSteps {

    // Driver estático
    static WebDriver driver;

    @Before
    public void setup() {
        // Configura WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Opciones de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-infobars");
        // ¡LÍNEA PROBLEMÁTICA ELIMINADA!

        // Inicia el driver
        driver = new ChromeDriver(options);

        // Maximiza
        driver.manage().window().maximize();

        // Borra cookies
        driver.manage().deleteAllCookies();

        // Espera implícita de 10s
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void teardown() throws Exception {
        // Cierra el driver
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
        driver.findElement(By.xpath(xpath)).click();
    }

    @When("coloca en el campo usuario {string} el texto {string}")
    public void coloca_en_el_campo_usuario_el_texto(String xpath, String username) {
        driver.findElement(By.xpath(xpath)).click();
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(username);
    }

    @When("coloca en el campo password {string} el texto {string}")
    public void coloca_en_el_campo_password_el_texto(String xpath, String password) {
        driver.findElement(By.xpath(xpath)).click();
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(password);
    }

    @When("hacer click sobre el boton Login {string}")
    public void hacer_click_sobre_el_boton_login(String xpath) {
        // Espera explícita de 10s
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Espera a que el elemento sea visible Y esté habilitado para hacer clic
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    @Then("la pagina debe contener el texto {string}")
    public void la_pagina_debe_contener_el_texto(String textoEsperado) {

        try {
            // Espera 10s
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Espera por el texto
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), textoEsperado));

            // Verifica el texto
            String bodyText = driver.findElement(By.tagName("body")).getText();
            Assert.assertTrue(
                    "Verificación fallida: El texto '" + textoEsperado + "' NO se encontró en la página.",
                    bodyText.contains(textoEsperado)
            );

        } catch (TimeoutException e) {
            // Falla en timeout
            Assert.fail(
                    "Verificación fallida: El texto '" + textoEsperado + "' NO se encontró en la página después de 10 seg."
            );
        }
    }

    @Then("La pagina deve mostrar el aviso {string}")
    public void la_pagina_deve_mostrar_el_aviso(String textoEsperado) {
        // Obtiene texto de alerta
        String textoActual = closeAlertAndGetItsText();

        // Compara texto
        assertEquals(textoEsperado, textoActual);
    }

    @When("seleccionamos en el dropdown {string} el texto visible {string}")
    public void seleccionamos_en_el_dropdown_el_texto_visible(String xpath, String textoVisible) {
        // Selecciona del dropdown
        Select dropdown = new Select(driver.findElement(By.xpath(xpath)));
        dropdown.selectByVisibleText(textoVisible);
    }

    private String closeAlertAndGetItsText() {
        try {
            // Espera 10s
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Espera por alerta
            wait.until(ExpectedConditions.alertIsPresent());

            // Obtiene texto de alerta
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;

        } catch (TimeoutException e) {
            // Falla en timeout
            Assert.fail("No se encontró ninguna alerta después de esperar 10 segundos: " + e.getMessage());
            return null;
        }
    }
}