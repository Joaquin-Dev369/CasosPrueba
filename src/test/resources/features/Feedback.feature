Feature: Funcionalidad de Feedback

  Scenario: Enviar un formulario de feedback exitosamente
    Given al navegar hasta la url "https://demo.testfire.net/"

    # 1. Clic en el link "Feedback" (Target: id=HyperLink4)
    When hacemos click en el link "//*[@id='HyperLink4']"

    # 2. ESPERA: Esperar a que el formulario cargue
    Then la pagina debe contener el texto "Subject:"

    # 3. ACCIÓN: Llenar el formulario
    And coloca en el campo usuario "//*[@name='name']" el texto "Usuario de Prueba"
    # ¡ESTA ES LA CORRECCIÓN!
    And coloca en el campo usuario "//*[@name='email_addr']" el texto "feedback@test.com"
    And coloca en el campo usuario "//*[@name='subject']" el texto "Test de Feedback"
    And coloca en el campo usuario "//*[@name='comments']" el texto "Este es un comentario de prueba de feedback."

    # 4. ACCIÓN: Enviar el formulario
    And hacer click sobre el boton Login "//*[@name='submit']"

    # 5. VERIFICACIÓN: Comprobar el resultado
    Then la pagina debe contener el texto "Thank you for your comments"