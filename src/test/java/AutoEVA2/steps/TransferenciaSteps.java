package AutoEVA2.steps;
// Imports de Selenium y WebDriverManager
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// Imports de Cucumber
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

// Import de Assert (para verificar)
import org.junit.Assert;

// Import para el tiempo de espera (Duration)
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.Select;

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

        //espera hasta 10 segundos si no encuentra un elemento.
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
        driver.findElement(By.xpath(xpath)).click();
    }

    @Then("la pagina debe contener el texto {string}")
    public void la_pagina_debe_contener_el_texto(String textoEsperado) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Obtenemos tod0 el texto del cuerpo de la página
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
        // Encontramos el elemento Select (el dropdown)
        Select dropdown = new Select(driver.findElement(By.xpath(xpath)));

        // Seleccionamos la opción usando el texto visible
        dropdown.selectByVisibleText(textoVisible);
    }

    private String closeAlertAndGetItsText() {
        try {
            // 1. Esperar un poco a que la alerta aparezca (Opcional pero recomendado)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // 2. Cambiar el foco del driver a la alerta
            Alert alert = driver.switchTo().alert();

            // 3. Obtener el texto de la alerta
            String alertText = alert.getText();

            // 4. Aceptar la alerta (presionar OK)
            alert.accept(); // O usa alert.dismiss() si quieres cancelarla

            // 5. Devolver el texto que leíste
            return alertText;

        } catch (Exception e) {
            // Manejar el caso de que no haya ninguna alerta
            System.out.println("No se encontró ninguna alerta: " + e.getMessage());
            return null;
        }
    }

}