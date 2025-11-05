Feature: Formulario de Contacto

  Background: Login de usuario
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

  Scenario: Enviar un comentario de feedback exitosamente
    # 1. Clic en "Contact Us" (el link del HEADER que me diste)
    When hacemos click en el link "//*[@id='HyperLink3']"

    # 2. Clic en "online form" (el link que me diste)
    #    La espera implícita de 10s esperará a que este link aparezca.
    And hacemos click en el link "//a[@href='feedback.jsp']"

    # 3. ESPERA: Esperar a que el formulario cargue
    Then la pagina debe contener el texto "Subject:"

    # 4. ACCIÓN: Llenar el formulario
    And coloca en el campo usuario "//*[@name='name']" el texto "Usuario de Prueba"
    And coloca en el campo usuario "//*[@name='email_addr']" el texto "test@test.com"
    And coloca en el campo usuario "//*[@name='subject']" el texto "Asunto de Prueba"
    And coloca en el campo usuario "//*[@name='comments']" el texto "Comentario de prueba."
    And hacer click sobre el boton Login "//*[@name='submit']"

    # 5. VERIFICACIÓN: Comprobar el resultado
    Then la pagina debe contener el texto "Thank you for your comments"