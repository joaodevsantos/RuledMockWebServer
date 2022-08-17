package com.ruled.mockwebserver

import com.ruled.mockwebserver.domain.MockServerResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Represents a method of a REST API endpoint.
 *
 * It is a ruled method since it handles a request and based on its rules it gives a response. This
 * is what makes this Mock Web Server a ruled one. This business logic allows the Mock Web Server
 * to return different responses based on rules that are validated against the request data.
 */
abstract class MockServerMethod {
  /**
   * Method name.
   */
  abstract val name: String

  /**
   * List of rules this method verifies.
   */
  abstract val rulesList: List<MockServerRule>

  /**
   * Gets the response for a specific HTTP request.
   *
   * @param request HTTP request to handle.
   *
   * @return Response to the HTTP request made.
   *
   * @see RecordedRequest
   * @see MockResponse
   */
  fun getResponseFor(request: RecordedRequest): MockServerResponse =
    getRuleToApply(request).response

  /**
   * Gets the rule to be applied for the request specified.
   *
   * @param request Request being handled by the Mock Web Server.
   *
   * @return Rule to be applied for the request made.
   *
   * @exception NoSuchElementException No available rule applies to this request.
   *
   * @see MockServerRule
   */
  private fun getRuleToApply(request: RecordedRequest): MockServerRule =
    rulesList.first { rule -> rule.isToApply(request) }
}
