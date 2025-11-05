Feature: Links del Footer

  Scenario: Verificar el link de Política de Privacidad
    Given al navegar hasta la url "https://demo.testfire.net/"

    # 1. ACCIÓN: Hacemos clic en el link "Privacy Policy"
    #    (Selector del HTML: <a id="HyperLink5" ...>Privacy Policy</a>)
    When hacemos click en el link "//*[@id='HyperLink5']"

    # 2. VERIFICACIÓN: Esperamos y verificamos que la nueva página cargó
    #    Buscamos el título "Privacy Policy" en la página.
    Then la pagina debe contener el texto "Privacy Policy"