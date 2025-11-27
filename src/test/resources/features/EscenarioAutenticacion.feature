Feature: Autenticación de Usuario

  Background:
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"

  Scenario: Inicio de sesión exitoso y verificación de bienvenida
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

    Then la pagina debe contener el texto "Hello John Smith"

  Scenario: Intento de inicio de sesión con contraseña incorrecta
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "contraseñaMal"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"
    Then la pagina debe contener el texto "Login Failed"
    Then la pagina debe contener el texto "username or password was not found in our system"

  Scenario: Intento de inicio de sesión con campos vacíos
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"
    Then La pagina deve mostrar el aviso "You must enter a valid username"


  Scenario: Intento de inicio de sesión con usuario inexistente
    When coloca en el campo usuario "//*[@id='uid']" el texto "usuarioinvalido"
    And coloca en el campo password "//*[@id='passw']" el texto "passinvalido"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

    # Verificamos que aparezca el mismo mensaje de error
    Then la pagina debe contener el texto "Login Failed"
    And la pagina debe contener el texto "username or password was not found in our system"