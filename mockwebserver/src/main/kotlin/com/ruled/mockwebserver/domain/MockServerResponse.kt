package com.ruled.mockwebserver.domain

/**
 * Holds the data to be retrieved as response to an HTTP request to the Mock Web Server.
 *
 * @param code HTTP response code.
 * @param body HTTP response body.
 */
data class MockServerResponse(
  val code: Int,
  val body: String = ""
)
