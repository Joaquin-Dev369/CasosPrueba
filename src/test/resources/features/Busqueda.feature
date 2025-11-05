Feature: Funcionalidad de Búsqueda

  Scenario: Realizar una búsqueda de término exitosa
    Given al navegar hasta la url "https://demo.testfire.net/"

  # 1. Escribir "funds" en el campo (Target: id=query)
    When coloca en el campo usuario "//*[@id='query']" el texto "funds"

  # 2. Clic en "Go" (Target: xpath=//input[@value='Go'])
    And hacer click sobre el boton Login "//input[@value='Go']"

  # 3. Verificar los resultados
    Then la pagina debe contener el texto "Search Results"
    And la pagina debe contener el texto "funds"