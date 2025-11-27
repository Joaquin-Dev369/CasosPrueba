package AutoEVA2.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// --- TUS IMPORTACIONES DE UTILIDADES ---
import Utilidades.Utility;     // Para las fotos
import Utilidades.ExcelUtils;  // Para leer/escribir Excel
// ---------------------------------------

public class TransferenciaSteps {

    static WebDriver driver;

    @Before
    public void setup() throws Exception {
        // 1. Configuración del Driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 2. ¡IMPORTANTE! Inicializar el archivo Excel antes de los tests
        // Ruta: carpeta testData en la raíz del proyecto. Hoja: DataLogin
        ExcelUtils.setExcelFileSheet("testData\\data.xlsx", "DataLogin");
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- PASOS GENERALES (Navegación y Clicks) ---

    @Given("al navegar hasta la url {string}")
    public void al_navegar_hasta_la_url(String url) throws Exception {
        driver.get(url);
        Utility.captureScreenShot(driver, "evidencias\\Navegacion_" + Utility.GetTimeStampValue() + ".png");
    }

    @When("hacemos click en el link {string}")
    public void hacemos_click_en_el_link(String xpath) throws Exception {
        // Método genérico para hacer clic en cualquier link definido por XPath
        driver.findElement(By.xpath(xpath)).click();
    }

    @When("hacer click sobre el boton Login {string}")
    public void hacer_click_sobre_el_boton_login(String xpath) throws Exception {
        driver.findElement(By.xpath(xpath)).click();
        Utility.captureScreenShot(driver, "evidencias\\ClickBoton_" + Utility.GetTimeStampValue() + ".png");
    }

    // --- PASOS CONECTADOS AL EXCEL (Data Driven) ---
    // Estos pasos usan el número de fila {int} para buscar los datos

    @When("coloca en el campo usuario y en el campo password un valor valido {int}")
    public void coloca_usuario_y_password_desde_excel(Integer fila) throws Exception {
        // Limpiar y llenar Usuario (Columna 0 del Excel)
        driver.findElement(By.id("uid")).clear();
        driver.findElement(By.id("uid")).sendKeys(ExcelUtils.getCellData(fila, 0));

        // Limpiar y llenar Password (Columna 1 del Excel)
        driver.findElement(By.id("passw")).clear();
        driver.findElement(By.id("passw")).sendKeys(ExcelUtils.getCellData(fila, 1));

        Utility.captureScreenShot(driver, "evidencias\\LoginDatos_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
    }

    @When("Indicar la cuenta de cargo en From Account {int}")
    public void indicar_cuenta_cargo_desde_excel(Integer fila) throws Exception {
        // Seleccionar cuenta origen (Columna 2 del Excel)
        Select fromAccount = new Select(driver.findElement(By.id("fromAccount")));
        String cuentaOrigen = ExcelUtils.getCellData(fila, 2);
        fromAccount.selectByVisibleText(cuentaOrigen);
    }

    @When("Indica la cuenta beneficiaria en To Account {int}")
    public void indicar_cuenta_destino_desde_excel(Integer fila) throws Exception {
        // Seleccionar cuenta destino (Columna 3 del Excel)
        Select toAccount = new Select(driver.findElement(By.id("toAccount")));
        String cuentaDestino = ExcelUtils.getCellData(fila, 3);
        toAccount.selectByVisibleText(cuentaDestino);
    }

    @When("Indicar monto a transferir en Amount TO {int}")
    public void indicar_monto_desde_excel(Integer fila) throws Exception {
        // Llenar monto (Columna 4 del Excel)
        driver.findElement(By.id("transferAmount")).clear();
        String monto = ExcelUtils.getCellData(fila, 4);
        driver.findElement(By.id("transferAmount")).sendKeys(monto);

        Utility.captureScreenShot(driver, "evidencias\\FormularioTransfer_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
    }

    // --- VERIFICACIÓN Y ESCRITURA EN EXCEL ---

    @Then("El mensaje de resultados debe contener un mensaje de ingreso {int}")
    public void verificar_resultado_y_escribir_excel(Integer fila) throws Exception {
        // 1. Obtener el texto esperado desde el Excel (Columna 5)
        String mensajeEsperado = ExcelUtils.getCellData(fila, 5);

        // 2. Obtener el texto actual de la página (ajusta el ID/XPath según tu aplicación real)
        // Nota: En AltoroMutual, el resultado suele estar en un span con ID '_ctl0__ctl0_Content_Main_postResp'
        // Si no funciona, usa un XPath genérico que contenga el texto.
        String mensajeActual = "";
        try {
            mensajeActual = driver.findElement(By.xpath("//span[contains(@id,'Content_Main_postResp')]")).getText();
        } catch (Exception e) {
            // Si no encuentra el span específico, intentamos buscar el texto en el body
            mensajeActual = driver.findElement(By.tagName("body")).getText();
        }

        // 3. Comparar y Escribir en Excel (Columna 6)
        if (mensajeActual.contains(mensajeEsperado)) {
            System.out.println("Prueba Exitosa en fila " + fila);
            ExcelUtils.setCellData("Prueba OK", fila, 6);
            Utility.captureScreenShot(driver, "evidencias\\ResultadoOK_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
        } else {
            System.out.println("Fallo en fila " + fila + ". Esperado: " + mensajeEsperado + " | Actual: " + mensajeActual);
            ExcelUtils.setCellData("Prueba NO OK", fila, 6);
            Utility.captureScreenShot(driver, "evidencias\\ResultadoFALLO_" + fila + "_" + Utility.GetTimeStampValue() + ".png");

            // Hacer fallar el test de Cucumber para que se marque en rojo
            Assert.fail("El texto esperado no coincide. Esperado: " + mensajeEsperado + ", Actual: " + mensajeActual);
        }
    }
}