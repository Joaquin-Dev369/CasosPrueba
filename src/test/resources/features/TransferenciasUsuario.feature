Feature: Transferencias
  Background: Login de usuario
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

  Scenario: Realizar una transferencia y verificar en el resumen de cuenta
    # El Background (Login) ya nos dejó en la página "main.jsp"
    # 1. Navegar a la página de transferencia
    When hacemos click en el link "//*[@id='MenuHyperLink3']"
    # 2. Seleccionar la cuenta origen
    And seleccionamos en el dropdown "//*[@id='fromAccount']" el texto visible "4539082039396288 Credit Card"
    # 3. Colocar el monto
    And coloca en el campo usuario "//*[@id='transferAmount']" el texto "1001231230"
    # 4. Click en el botón de transferir
    And hacer click sobre el boton Login "//*[@id='transfer']"
    # 5. Verificar la confirmación de la transferencia
    # (El step revisa que el monto aparezca en la página de confirmación)
    Then la pagina debe contener el texto "1001231230"
    # 6. Regresar al Resumen de Cuentas (View Account Summary)
    When hacemos click en el link "//*[@id='MenuHyperLink1']"
    # 7. Ver los detalles de la cuenta (usando la selección por defecto)
    And hacer click sobre el boton Login "//*[@id='btnGetAccount']"
    # 8. Verificar que el monto transferido aparece en la tabla de transacciones recientes
    # (Reutilizamos el step para verificar el monto en la nueva página)
    Then la pagina debe contener el texto "1001231230"


  Scenario: Solicitar una tarjeta de crédito Visa Altoro Gold
    # Asumimos que el Background (Login) ya nos dejó en la página "main.jsp"

    # 1. Click en el link "Here" (By.linkText("Here"))
    # (Convertimos By.linkText a un XPath)
    When hacemos click en el link "//a[text()='Here']"

    # 2. Colocar la contraseña (By.name("passwd"))
    # (Usamos el step de password y convertimos By.name a XPath)
    And coloca en el campo password "//*[@name='passwd']" el texto "Demo1234"

    # 3. Click en el botón Submit (By.name("Submit"))
    # (Convertimos By.name a XPath)
    And hacer click sobre el boton Login "//*[@name='Submit']"

    # 4. Verificación
    # (En lugar de hacer clic en el mensaje como el test original,
    # verificamos que la página contenga el texto de éxito)
    Then la pagina debe contener el texto "Your application for a credit card has been received"