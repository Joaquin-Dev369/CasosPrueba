package AutoEVA2.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilidades.Utility;
import Utilidades.ExcelUtils;
import java.io.File;
import java.time.Duration;

public class TransferenciaSteps {

    WebDriver driver = Hooks.getDriver();

    // --- MÉTODOS AUXILIARES ---

    private void manejarAlertaSiExiste() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
            if(wait.until(ExpectedConditions.alertIsPresent()) != null){
                driver.switchTo().alert().accept();
            }
        } catch (Exception e) {
            // Sin alerta, continuamos
        }
    }

    // ==========================================
    // SECCIÓN 1: PASOS GENÉRICOS
    // ==========================================

    @Given("al navegar hasta la url {string}")
    public void al_navegar_hasta_la_url(String url) throws Exception {
        driver = Hooks.getDriver();
        driver.get(url);

        try {
            String projectPath = System.getProperty("user.dir");
            String excelPath = projectPath + File.separator + "testData" + File.separator + "data.xlsx";
            ExcelUtils.setExcelFileSheet(excelPath, "DataLogin");
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo precargar el Excel.");
        }
        Utility.captureScreenShot(driver, "evidencias/Navegacion.png");
    }

    @When("hacemos click en el link {string}")
    public void hacemos_click_en_el_link(String xpath) throws Exception {
        try {
            driver.findElement(By.xpath(xpath)).click();
        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept();
            driver.findElement(By.xpath(xpath)).click();
        }
    }

    @When("coloca en el campo usuario {string} el texto {string}")
    public void coloca_en_el_campo_usuario_el_texto(String xpath, String texto) throws Exception {
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(texto);
    }

    @When("coloca en el campo password {string} el texto {string}")
    public void coloca_en_el_campo_password_el_texto(String xpath, String texto) throws Exception {
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(texto);
    }

    @When("hacer click sobre el boton Login {string}")
    public void hacer_click_sobre_el_boton_login(String xpath) throws Exception {
        driver.findElement(By.xpath(xpath)).click();

        try {
            WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofMillis(800));
            waitShort.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alerta detectada tras el click (Correcto para pruebas negativas).");
        } catch (TimeoutException e) {
            // Solo tomamos foto si NO hay alerta, para no bloquear el driver
            Utility.captureScreenShot(driver, "evidencias/Click_Boton_" + System.currentTimeMillis() + ".png");
        }
    }

    @When("seleccionamos en el dropdown {string} el texto visible {string}")
    public void seleccionamos_en_el_dropdown_el_texto_visible(String xpath, String textoVisible) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        Select dropdown = new Select(driver.findElement(By.xpath(xpath)));
        dropdown.selectByVisibleText(textoVisible);
    }

    @Then("la pagina debe contener el texto {string}")
    public void la_pagina_debe_contener_el_texto(String textoEsperado) {
        manejarAlertaSiExiste();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), textoEsperado));
        } catch (Exception e) {}

        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue("Texto no encontrado: " + textoEsperado, bodyText.contains(textoEsperado));
    }

    @Then("La pagina deve mostrar el aviso {string}")
    public void la_pagina_deve_mostrar_el_aviso(String textoEsperado) {
        Alert alert = null;
        try {
            // Intentamos esperar la alerta
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            alert = driver.switchTo().alert();
        } catch (UnhandledAlertException e) {
            // ¡AJÁ! Si Selenium se queja de que hay una alerta sin manejar, es porque LA HAY.
            // La capturamos aquí.
            alert = driver.switchTo().alert();
        } catch (Exception e) {
            Assert.fail("No apareció la alerta esperada: " + textoEsperado);
        }

        if (alert != null) {
            String alertText = alert.getText();
            alert.accept();
            Assert.assertEquals(textoEsperado, alertText);
        }
    }

    // ==========================================
    // SECCIÓN 2: PASOS EXCEL (DATA DRIVEN)
    // ==========================================

    @When("coloca en el campo usuario y en el campo password un valor valido {int}")
    public void coloca_usuario_y_password_desde_excel(Integer fila) throws Exception {
        String usuario = ExcelUtils.getCellData(fila, 0);
        String pass = ExcelUtils.getCellData(fila, 1);

        System.out.println("LOG DEBUG - Fila: " + fila + " | Usuario leido: " + usuario);

        // Protección contra filas vacías
        if(usuario == null || usuario.trim().isEmpty()) {
            System.out.println("Fila vacía o inválida, saltando...");
            return;
        }

        driver.findElement(By.id("uid")).clear();
        driver.findElement(By.id("uid")).sendKeys(usuario);
        driver.findElement(By.id("passw")).clear();
        driver.findElement(By.id("passw")).sendKeys(pass);
    }

    @When("Indicar la cuenta de cargo en From Account {int}")
    public void indicar_cuenta_cargo_desde_excel(Integer fila) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fromAccount")));
            Select fromAccount = new Select(driver.findElement(By.id("fromAccount")));
            String cuentaOrigen = ExcelUtils.getCellData(fila, 2);
            fromAccount.selectByVisibleText(cuentaOrigen);
        } catch (Exception e) {
            System.err.println("No se pudo seleccionar cuenta origen (Posible fallo login previo).");
        }
    }

    @When("Indica la cuenta beneficiaria en To Account {int}")
    public void indicar_cuenta_destino_desde_excel(Integer fila) throws Exception {
        try {
            Select toAccount = new Select(driver.findElement(By.id("toAccount")));
            String cuentaDestino = ExcelUtils.getCellData(fila, 3);
            toAccount.selectByVisibleText(cuentaDestino);
        } catch (Exception e) {}
    }

    @When("Indicar monto a transferir en Amount TO {int}")
    public void indicar_monto_desde_excel(Integer fila) throws Exception {
        try {
            driver.findElement(By.id("transferAmount")).clear();
            String monto = ExcelUtils.getCellData(fila, 4);
            driver.findElement(By.id("transferAmount")).sendKeys(monto);
        } catch (Exception e) {}
    }

    @Then("El mensaje de resultados debe contener un mensaje de ingreso {int}")
    public void verificar_resultado_y_escribir_excel(Integer fila) throws Exception {
        String mensajeEsperado = ExcelUtils.getCellData(fila, 5);
        if(mensajeEsperado == null || mensajeEsperado.isEmpty()) return;

        String mensajeActual = "";
        try {
            mensajeActual = driver.findElement(By.tagName("body")).getText();
        } catch (Exception e) {
            mensajeActual = "Error leyendo página";
        }

        if (mensajeActual.contains(mensajeEsperado)) {
            System.out.println("Excel Fila " + fila + ": OK");
            ExcelUtils.setCellData("Prueba OK", fila, 6);
        } else {
            System.out.println("Excel Fila " + fila + ": FALLO. Buscaba '" + mensajeEsperado + "'");
            ExcelUtils.setCellData("Prueba NO OK", fila, 6);
            Assert.fail("Fallo validación Excel fila " + fila);
        }
    }
}