Feature: Links del Footer

  Scenario: Verificar el link de Declaración de Seguridad
    Given al navegar hasta la url "https://demo.testfire.net/"

    # 1. ACCIÓN: Hacemos clic en el link "Security Statement"
    #    (Selector del HTML: <a id="HyperLink6" ...>Security Statement</a>)
    When hacemos click en el link "//*[@id='HyperLink6']"

    # 2. VERIFICACIÓN: Esperamos y verificamos que la nueva página cargó
    #    Buscamos el título "Security Statement" en la página.
    Then la pagina debe contener el texto "Security Statement"