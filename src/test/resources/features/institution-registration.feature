Feature: Institution registration

  As an authenticated administrator
  I want to register educational institutions
  So that the platform can manage institutions and their users


  Background:
    Given an authenticated administrator


  Rule: Institution names must satisfy validation constraints

    Scenario Outline: Reject invalid institution names

      When the administrator registers an institution with name "<name>"

      Then the system rejects the registration
      And the validation message is "<message>"

      Examples:
        | name | message |
        |      | Institution name is required |
        | AB   | Institution name must contain at least 3 characters |


  Rule: Institution name must be unique

    Scenario: Reject duplicate institution name

      Given an institution exists with name "Institución Educativa Sagrada Familia"

      When the administrator registers an institution with name "Institución Educativa Sagrada Familia"

      Then the system rejects the registration
      And the validation message is "Institution name already exists"


  Rule: Valid institutions can be registered

    Scenario: Register a valid institution

      Given no institution exists with name "Institución Educativa Sagrada Familia"

      When the administrator registers an institution with name "Institución Educativa Sagrada Familia"

      Then the institution is stored in the system
      And the institution has a generated identifier
      And the institution name is "Institución Educativa Sagrada Familia"


  Rule: Institution names are normalized

    Scenario: Trim leading and trailing spaces

      Given no institution exists with name "Institución Educativa Sagrada Familia"

      When the administrator registers an institution with name "   Institución Educativa Sagrada Familia   "

      Then the institution is stored in the system
      And the stored institution name is "Institución Educativa Sagrada Familia"
