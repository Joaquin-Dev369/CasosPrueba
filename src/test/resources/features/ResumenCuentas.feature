Feature: Funcionalidad de Resumen de Cuentas

  Background: Login de usuario
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

  Scenario: Cambiar la vista a la cuenta "Checking"
    # 1. ACCIÓN: Seleccionar "800003 Checking" del dropdown
    #    (La espera implícita esperará a que el dropdown cargue)
    When seleccionamos en el dropdown "//*[@id='listAccounts']" el texto visible "800003 Checking"

    # 2. ACCIÓN: Hacer clic en el botón "Go"
    And hacer click sobre el boton Login "//*[@id='btnGetAccount']"

    # 3. VERIFICACIÓN: Esperar y verificar que la página de "Checking" cargó
    Then la pagina debe contener el texto "Account History"