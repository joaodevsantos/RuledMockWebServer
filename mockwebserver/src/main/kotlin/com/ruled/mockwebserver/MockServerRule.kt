package com.ruled.mockwebserver

import com.ruled.mockwebserver.domain.MockServerResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Represents a rule for the Mock Web Server.
 *
 * Allows mocking a response for some HTTP request that it will only be returned if some logic
 * is verified.
 */
interface MockServerRule {
  /**
   * Response to be returned by this rule.
   *
   * @see MockServerResponse
   */
  val response: MockServerResponse

  /**
   * Checks if this rule should be applied.
   *
   * @param request Request that is being tested against this rule.
   *
   * @return True if the rule should be applied, false otherwise.
   *
   * @see RecordedRequest
   */
  fun isToApply(request: RecordedRequest): Boolean
}
