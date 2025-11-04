Feature: Formulario de Contacto

  Background: Login de usuario
    Given al navegar hasta la url "https://demo.testfire.net/"
    When hacemos click en el link "//*[@id='LoginLink']/font"
    And coloca en el campo usuario "//*[@id='uid']" el texto "jsmith"
    And coloca en el campo password "//*[@id='passw']" el texto "Demo1234"
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

  Scenario: Enviar un comentario de feedback exitosamente
    # 1. Hacemos clic en el link del menú
    When hacemos click en el link "//*[@id='MenuHyperLink4']"

    # 2. [ESPERA 1]
    #    Forzamos al test a esperar a que la página de CONTACTO cargue.
    #    Sabemos que cargó si contiene el texto "Email:".
    Then la pagina debe contener el texto "Email:"

    # 3. [ACCIÓN]
    #    Ahora que la página cargó, hacemos clic en el link "online form"
    And hacemos click en el link "//a[normalize-space(text())='online form']"

    # 4. [ESPERA 2]
    #    El clic anterior carga el FORMULARIO. Esperamos a que
    #    cargue buscando el texto "Subject:".
    Then la pagina debe contener el texto "Subject:"

    # 5. [ACCIÓN] Llenamos el formulario
    And coloca en el campo usuario "//*[@name='name']" el texto "Usuario de Prueba"
    And coloca en el campo usuario "//*[@name='email_addr']" el texto "test@test.com"
    And coloca en el campo usuario "//*[@name='subject']" el texto "Asunto de Prueba"
    And coloca en el campo usuario "//*[@name='comments']" el texto "Comentario de prueba."
    And hacer click sobre el boton Login "//*[@name='submit']"

    # 6. [VERIFICACIÓN FINAL]
    #    Esperamos y verificamos el mensaje de éxito
    Then la pagina debe contener el texto "Thank you for your comments."