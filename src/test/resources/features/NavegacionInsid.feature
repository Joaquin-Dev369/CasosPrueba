Feature: Navegación de la sección Inside Altoro Mutual

  Background:
    Given al navegar hasta la url "https://demo.testfire.net/"

  Scenario: Probar los links de la sección Inside1
    # Click en el link principal "Inside Altoro Mutual"
    When hacemos click en el link "//*[@id='CatLink3']"
    #Verificacion de paguina altoro
    Then la pagina debe contener el texto "Inside Altoro"

    # Click en "community"
    And hacemos click en el link "//a[text()='community programs']"
    # Verificación de la página "community"
    Then la pagina debe contener el texto "Community Affairs"


  Scenario: Probar los links de la sección Inside
    # Click en el link principal "Inside Altoro Mutual"
    When hacemos click en el link "//*[@id='CatLink3']"

    And hacemos click en el link "//a[text()='2006 community annual report (PDF, 63KB)']"

