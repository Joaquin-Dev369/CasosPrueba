Feature: Transferencias

  Background: Login de usuario
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

  Scenario: Realizar una transferencia y verificar en el resumen de cuenta
    # La espera implícita de 10s esperará a que el link sea visible
    When hacemos click en el link "//*[@id='MenuHyperLink3']"
    And seleccionamos en el dropdown "//*[@id='fromAccount']" el texto visible "4539082039396288 Credit Card"
    And coloca en el campo usuario "//*[@id='transferAmount']" el texto "100"
    And hacer click sobre el boton Login "//*[@id='transfer']"
    Then la pagina debe contener el texto "100"
    When hacemos click en el link "//*[@id='MenuHyperLink1']"
    And hacer click sobre el boton Login "//*[@id='btnGetAccount']"
    Then la pagina debe contener el texto "100"

  Scenario: Solicitar una tarjeta de crédito Visa Altoro Gold
    When hacemos click en el link "//a[text()='Here']"
    And coloca en el campo password "//*[@name='passwd']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='Submit']"

    # (Este paso debe fallar, es el bug que encontramos)
    Then la pagina debe contener el texto "Your application for a credit card has been received"