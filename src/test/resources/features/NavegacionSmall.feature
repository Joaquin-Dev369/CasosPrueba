Feature: Navegación de la sección Small Business

  Background:
    Given al navegar hasta la url "https://demo.testfire.net/"
    # Click en el link "Small Business"
    When hacemos click en el link "//*[@id='CatLink2']"

  Scenario: 1. Navegar a Deposit Products
    # Click en el sub-menú "Deposit Products"
    When hacemos click en el link "//a[text()='Deposit Products']"
    # Verificación de título
    Then la pagina debe contener el texto "Deposit Products"


  Scenario: 2. Navegar a Lending
    # Click en el sub-menú "Lending"
    When hacemos click en el link "//a[text()='Lending']"
    # Verificación de título
    Then la pagina debe contener el texto "Lending"

  Scenario: 3. Navegar a Cards
    # Click en el sub-menú "Cards"
    When hacemos click en el link "//a[text()='Cards']"
    # Verificación de título
    Then la pagina debe contener el texto "Cards"

  Scenario: 4. Navegar a Insurance
    # Click en el sub-menú "Insurance"
    When hacemos click en el link "//a[text()='Insurance']"
    # Verificación de título
    Then la pagina debe contener el texto "Insurance"

  Scenario: 5. Navegar a Retirement
    # Click en el sub-menú "Retirement"
    When hacemos click en el link "//a[text()='Retirement']"
    # Verificación de título
    Then la pagina debe contener el texto "Retirement"