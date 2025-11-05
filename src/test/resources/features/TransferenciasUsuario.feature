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

  Scenario: Realizar una transferencia a la misma cuenta
    # La espera implícita de 10s esperará a que el link sea visible
    When hacemos click en el link "//*[@id='MenuHyperLink3']"
    And coloca en el campo usuario "//*[@id='transferAmount']" el texto "100"
    And hacer click sobre el boton Login "//*[@id='transfer']"
    Then La pagina deve mostrar el aviso "From Account and To Account fields cannot be the same."

  Scenario: Realizar una transferencia con valor no valido
    # La espera implícita de 10s esperará a que el link sea visible
    When hacemos click en el link "//*[@id='MenuHyperLink3']"
    And seleccionamos en el dropdown "//*[@id='fromAccount']" el texto visible "4539082039396288 Credit Card"
    And hacer click sobre el boton Login "//*[@id='transfer']"
    Then La pagina deve mostrar el aviso "Transfer Amount must be a number greater than 0."










