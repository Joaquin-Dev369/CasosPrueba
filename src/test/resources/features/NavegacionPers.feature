Feature: Navegación de la sección Personal

  Background:
    Given al navegar hasta la url "https://demo.testfire.net/"
    # Click en el link "Personal"
    When hacemos click en el link "//*[@id='CatLink1']"

  Scenario: 1. Navegar a Deposit Products
    # Click en el sub-menú "Deposit Products"
    When hacemos click en el link "//a[text()='Deposit Products']"
    # Verificación de título
    Then la pagina debe contener el texto "Deposit Products"

  Scenario: 2. Navegar a Checking

    # Click en el sub-menú "Checking"
    When hacemos click en el link "//a[text()='Checking']"
    # Verificación de título
    Then la pagina debe contener el texto "Checking"

  Scenario: 3. Navegar a Loans
    # Click en el sub-menú "Loans"
    When hacemos click en el link "//a[text()='Loans']"
    # Verificación de título
    Then la pagina debe contener el texto "Loans"

  Scenario: 4. Navegar a Card (Credit Cards)
    # Navegamos primero a "Loans"
    When hacemos click en el link "//a[text()='Cards']"
    # Verificamos el título de la página de aplicación
    Then la pagina debe contener el texto "Cards"

  Scenario: 5. Navegar a Investments
    # Click en el sub-menú "Investments"
    When hacemos click en el link "//a[text()='Investments']"
    # Verificación de título
    Then la pagina debe contener el texto "Investments"