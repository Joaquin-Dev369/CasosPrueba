Feature: Navegación del Menú Superior

  Scenario: Probar los links del menú de navegación superior
    Given al navegar hasta la url "https://demo.testfire.net.jsp"

    # El código Java primero hace clic en "Personal" para que aparezca el menú
    When hacemos click en el link "//*[@id='PERSONAL']"
    # Verificamos que estamos en la seccion Personal
    Then la pagina debe contener el texto "Personal"

    # 1. Click en SIGN OFF
    When hacemos click en el link "//*[@id='Deposit Product']"

    Then la pagina debe contener el texto "Deposit Products"

    # 2. Click en HOME
    When hacemos click en el link "//*[@id='MenuHyperLink2']"
    # Verificamos que volvimos al Home
    Then la pagina debe contener el texto "Welcome to Altoro Mutual Online"

    # 3. Click en CONTACT US
    When hacemos click en el link "//*[@id='MenuHyperLink3']"
    # Verificamos la página de Contacto
    Then la pagina debe contener el texto "Contact Us"

    # 4. Click en HELP
    When hacemos click en el link "//*[@id='MenuHyperLink4']"
    # Verificamos la página de Ayuda
    Then la pagina debe contener el texto "Help"

    # 5. Click en ABOUT US
    When hacemos click en el link "//*[@id='MenuHyperLink5']"
    # Verificamos la página de "About"
    Then la pagina debe contener el texto "About Us"

    # 6. Click en SECURITY
    When hacemos click en el link "//*[@id='MenuHyperLink6']"
    # Verificamos la página de "Security"
    Then la pagina debe contener el texto "Security"