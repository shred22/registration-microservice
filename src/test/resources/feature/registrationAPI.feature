@cucumber
@completing
Feature: Scenario to demonstrate actual spring registration service call from bdd test

  Scenario: Verify status after registration service call
    Given registration API request
    When the client calls service
    Then verify expected result


  Scenario: Get registration Details
    Given get registration API details
    When the client calls service to get details
    Then verify expected result with registration details
