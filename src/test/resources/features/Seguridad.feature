Feature: Links del Footer

  Scenario: Verificar el link de Declaración de Seguridad
    Given al navegar hasta la url "https://demo.testfire.net/"

    When hacemos click en el link "//*[@id='HyperLink6']"
    Then la pagina debe contener el texto "Security Statement"