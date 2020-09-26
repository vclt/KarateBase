Feature: Checkout Session Validation

  Background:
    * url QA_HOST
    * configure logPrettyRequest = true
    * configure logPrettyResponse = true
    * configure headers = { Accept: 'application/json', Content-Type: 'application/json' }

  @sprint0
  Scenario Outline: AC1 : Create checkout session endpoint
    * def jsonInput = read('payload/<inputJson>.json')
    * print "JSON Input:", jsonInput
    Given path QA_AUTHENTICATION
    And request jsonInput
    When method post
    Then status 200
    * print "Response : ", response
    Examples:
      | inputJson  |
      | demo |