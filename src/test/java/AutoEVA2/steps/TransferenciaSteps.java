package AutoEVA2.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

// Tus utilidades
import Utilidades.Utility;
import Utilidades.ExcelUtils;

public class TransferenciaSteps {

    // Usamos el driver centralizado en Hooks
    WebDriver driver = Hooks.getDriver();

    // --- PASOS GENERALES ---

    @Given("al navegar hasta la url {string}")
    public void al_navegar_hasta_la_url(String url) throws Exception {
        // Aseguramos tener el driver activo
        driver = Hooks.getDriver();
        driver.get(url);

        // Inicializamos el Excel aquí (solo si no se ha hecho antes)
        try {
            // Asegúrate que la ruta sea correcta en tu PC.
            // Si estás en Windows usa doble backslash \\ o slash simple /
            ExcelUtils.setExcelFileSheet("testData/data.xlsx", "DataLogin");
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo cargar el Excel o ya estaba cargado. " + e.getMessage());
        }

        Utility.captureScreenShot(driver, "evidencias/Navegacion_" + Utility.GetTimeStampValue() + ".png");
    }

    @When("hacemos click en el link {string}")
    public void hacemos_click_en_el_link(String xpath) throws Exception {
        driver.findElement(By.xpath(xpath)).click();
    }

    @When("hacer click sobre el boton Login {string}")
    public void hacer_click_sobre_el_boton_login(String xpath) throws Exception {
        driver.findElement(By.xpath(xpath)).click();
        Utility.captureScreenShot(driver, "evidencias/ClickBoton_" + Utility.GetTimeStampValue() + ".png");
    }

    // --- PASOS CONECTADOS AL EXCEL (Data Driven) ---

    @When("coloca en el campo usuario y en el campo password un valor valido {int}")
    public void coloca_usuario_y_password_desde_excel(Integer fila) throws Exception {
        driver.findElement(By.id("uid")).clear();
        driver.findElement(By.id("uid")).sendKeys(ExcelUtils.getCellData(fila, 0));

        driver.findElement(By.id("passw")).clear();
        driver.findElement(By.id("passw")).sendKeys(ExcelUtils.getCellData(fila, 1));

        Utility.captureScreenShot(driver, "evidencias/LoginDatos_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
    }

    @When("Indicar la cuenta de cargo en From Account {int}")
    public void indicar_cuenta_cargo_desde_excel(Integer fila) throws Exception {
        Select fromAccount = new Select(driver.findElement(By.id("fromAccount")));
        String cuentaOrigen = ExcelUtils.getCellData(fila, 2);
        fromAccount.selectByVisibleText(cuentaOrigen);
    }

    @When("Indica la cuenta beneficiaria en To Account {int}")
    public void indicar_cuenta_destino_desde_excel(Integer fila) throws Exception {
        Select toAccount = new Select(driver.findElement(By.id("toAccount")));
        String cuentaDestino = ExcelUtils.getCellData(fila, 3);
        toAccount.selectByVisibleText(cuentaDestino);
    }

    @When("Indicar monto a transferir en Amount TO {int}")
    public void indicar_monto_desde_excel(Integer fila) throws Exception {
        driver.findElement(By.id("transferAmount")).clear();
        String monto = ExcelUtils.getCellData(fila, 4);
        driver.findElement(By.id("transferAmount")).sendKeys(monto);

        Utility.captureScreenShot(driver, "evidencias/FormularioTransfer_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
    }

    // --- VERIFICACIÓN Y ESCRITURA EN EXCEL ---

    @Then("El mensaje de resultados debe contener un mensaje de ingreso {int}")
    public void verificar_resultado_y_escribir_excel(Integer fila) throws Exception {
        String mensajeEsperado = ExcelUtils.getCellData(fila, 5);
        String mensajeActual = "";

        try {
            // Intenta buscar el mensaje de éxito específico de Altoro
            mensajeActual = driver.findElement(By.xpath("//span[contains(@id,'Content_Main_postResp')]")).getText();
        } catch (Exception e) {
            // Fallback: busca en todo el cuerpo si falló el login
            mensajeActual = driver.findElement(By.tagName("body")).getText();
        }

        if (mensajeActual.contains(mensajeEsperado)) {
            System.out.println("Prueba Exitosa en fila " + fila);
            ExcelUtils.setCellData("Prueba OK", fila, 6);
            Utility.captureScreenShot(driver, "evidencias/ResultadoOK_" + fila + "_" + Utility.GetTimeStampValue() + ".png");
        } else {
            System.out.println("Fallo en fila " + fila);
            ExcelUtils.setCellData("Prueba NO OK", fila, 6);
            Utility.captureScreenShot(driver, "evidencias/ResultadoFALLO_" + fila + "_" + Utility.GetTimeStampValue() + ".png");

            // Hacemos fallar el test para que el reporte HTML muestre ROJO
            Assert.fail("Fallo validación Excel. Esperado: " + mensajeEsperado + ", Actual: " + mensajeActual);
        }
    }
}