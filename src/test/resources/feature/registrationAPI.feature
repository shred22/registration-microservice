Feature: Scenario to demonstrate actual spring registration service call from bdd test

  Scenario: Verify status after registration service call
    Given registration API request
    When the client calls service
    Then verify expected result
