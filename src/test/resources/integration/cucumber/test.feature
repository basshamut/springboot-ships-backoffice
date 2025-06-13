Feature: Consulta de naves espaciales
  Como usuario
  Quiero poder consultar los detalles de una nave espacial
  Para obtener información sobre las naves espaciales registradas

  Scenario: Consultar una nave espacial
    Given existe una nave espacial con el ID 1
    When el usuario consulta los detalles de la na espacial con ID 1
    Then se muestran los detalles de la nave espacial
