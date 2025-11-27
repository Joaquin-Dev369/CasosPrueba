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

    // Método para limpiar alertas residuales si las hubiera
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

        // Cargar Excel por defecto para evitar errores si se olvida
        try {
            String projectPath = System.getProperty("user.dir");
            String excelPath = projectPath + File.separator + "testData" + File.separator + "data.xlsx";
            ExcelUtils.setExcelFileSheet(excelPath, "DataLogin");
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo precargar el Excel (normal si no existe el archivo aun).");
        }
        Utility.captureScreenShot(driver, "evidencias/Navegacion.png");
    }

    @When("hacemos click en el link {string}")
    public void hacemos_click_en_el_link(String xpath) throws Exception {
        try {
            driver.findElement(By.xpath(xpath)).click();
        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept(); // Aceptar alerta bloqueante anterior
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

        // CORRECCIÓN CLAVE: Verificamos si salió una alerta ANTES de intentar la foto.
        // Si hay alerta, NO tomamos foto para no romper el test.
        try {
            WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofMillis(800));
            waitShort.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alerta detectada tras el click. Se omite foto para permitir validación en el siguiente paso.");
        } catch (TimeoutException e) {
            // No hay alerta, es seguro tomar la foto
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
        manejarAlertaSiExiste(); // Limpiamos alertas previas que estorben

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), textoEsperado));
        } catch (Exception e) {}

        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue("Texto no encontrado: " + textoEsperado, bodyText.contains(textoEsperado));
    }

    @Then("La pagina deve mostrar el aviso {string}")
    public void la_pagina_deve_mostrar_el_aviso(String textoEsperado) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            Assert.assertEquals(textoEsperado, alertText);
        } catch (Exception e) {
            Assert.fail("No apareció la alerta esperada: " + textoEsperado);
        }
    }

    // ==========================================
    // SECCIÓN 2: PASOS EXCEL (DATA DRIVEN)
    // ==========================================

    @When("coloca en el campo usuario y en el campo password un valor valido {int}")
    public void coloca_usuario_y_password_desde_excel(Integer fila) throws Exception {
        String usuario = ExcelUtils.getCellData(fila, 0);
        String pass = ExcelUtils.getCellData(fila, 1);

        System.out.println("LOG DEBUG - Fila: " + fila + " | Usuario leido: " + usuario); // Para depurar

        if(usuario == null || usuario.isEmpty()) return;

        driver.findElement(By.id("uid")).clear();
        driver.findElement(By.id("uid")).sendKeys(usuario);
        driver.findElement(By.id("passw")).clear();
        driver.findElement(By.id("passw")).sendKeys(pass);

        try { Utility.captureScreenShot(driver, "evidencias/Excel_Login_" + fila + ".png"); } catch (Exception e) {}
    }

    @When("Indicar la cuenta de cargo en From Account {int}")
    public void indicar_cuenta_cargo_desde_excel(Integer fila) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By dropdownLocator = By.id("fromAccount");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownLocator));
            Select fromAccount = new Select(driver.findElement(dropdownLocator));
            String cuentaOrigen = ExcelUtils.getCellData(fila, 2);
            fromAccount.selectByVisibleText(cuentaOrigen);
        } catch (TimeoutException e) {
            System.err.println("ERROR: No se encontró el menú de cuentas. Probablemente el LOGIN falló previamente.");
            // No lanzamos error fatal para dejar que el reporte registre el fallo más adelante
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
        if(mensajeEsperado.isEmpty()) return;

        String mensajeActual = "";
        try {
            // Verificamos si hay alerta primero (común en casos de error)
            try {
                Alert alert = driver.switchTo().alert();
                mensajeActual = alert.getText();
                alert.accept();
            } catch (NoAlertPresentException e) {
                // Si no hay alerta, buscamos en el cuerpo
                mensajeActual = driver.findElement(By.tagName("body")).getText();
            }
        } catch (Exception e) {
            mensajeActual = "Error leyendo página";
        }

        if (mensajeActual.contains(mensajeEsperado)) {
            System.out.println("Excel Fila " + fila + ": OK");
            ExcelUtils.setCellData("Prueba OK", fila, 6);
        } else {
            System.out.println("Excel Fila " + fila + ": FALLO. Esperado: '" + mensajeEsperado + "' vs Actual: '" + mensajeActual + "'");
            ExcelUtils.setCellData("Prueba NO OK", fila, 6);
            // Fallamos el test para que salga rojo en el reporte
            Assert.fail("Fallo validación Excel fila " + fila);
        }
    }
}