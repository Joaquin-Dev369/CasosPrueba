Feature: Funcionalidad de Logout (Cierre de Sesión)

  Background: Usuario está logueado
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

    # Verificación de que el login funcionó
    Then la pagina debe contener el texto "Hello John Smith"

  Scenario: El usuario cierra la sesión exitosamente
    # 1. ACCIÓN: Hacemos clic en el link "Sign Off"
    #    (Usando el ID que me diste: id="LoginLink")
    When hacemos click en el link "//*[@id='LoginLink']"

    # 2. VERIFICACIÓN: Esperamos y verificamos que el link "Sign In" reapareció
    #    (El texto "Sign In" vuelve a aparecer en el mismo <a> con id="LoginLink")
    Then la pagina debe contener el texto "Sign In"