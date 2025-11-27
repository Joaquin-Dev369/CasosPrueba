Feature: Transferencias Masivas desde Excel

  Background: Navegacion Inicial
    Given al navegar hasta la url "https://demo.testfire.net/"
    # Entramos al link de login antes de empezar el ciclo de datos
    When hacemos click en el link "//*[@id='LoginLink']/font"

  Scenario Outline: Ejecutar transferencia con datos del Excel
    # 1. Login usando usuario y pass de la fila indicada
    When coloca en el campo usuario y en el campo password un valor valido <fila>
    And hacer click sobre el boton Login "//*[@name='btnSubmit']"

    # 2. Ir a la seccion de transferencias
    And hacemos click en el link "//*[@id='MenuHyperLink3']"

    # 3. Llenar el formulario con datos del Excel (Origen, Destino, Monto)
    And Indicar la cuenta de cargo en From Account <fila>
    And Indica la cuenta beneficiaria en To Account <fila>
    And Indicar monto a transferir en Amount TO <fila>

    # 4. Click en transferir
    And hacer click sobre el boton Login "//*[@id='transfer']"

    # 5. Validar si el mensaje coincide con lo esperado en el Excel
    Then El mensaje de resultados debe contener un mensaje de ingreso <fila>

    # 6. Cerrar sesion para limpiar antes de la siguiente fila
    And hacemos click en el link "//*[@id='LoginLink']/font"

    Examples:
      | fila |
      | 2    |
      | 3    |
      | 4    |