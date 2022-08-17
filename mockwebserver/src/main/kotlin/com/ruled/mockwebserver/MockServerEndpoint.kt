package com.ruled.mockwebserver

import com.ruled.mockwebserver.domain.MockServerResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Represents an endpoint of a REST API to be handled by the Mock Web Server.
 */
abstract class MockServerEndpoint {
  /**
   * Endpoint path.
   */
  abstract val path: Regex

  /**
   * HTTP methods it handles.
   *
   * Each method contains a list of rules it verifies and that works like logic on the server side
   * controllers to return an output based on some logic.
   *
   * @see MockServerRule
   */
  abstract val methodsList: List<MockServerMethod>

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
    getMethodNamed(request.method!!).getResponseFor(request)

  /**
   * Gets the method with the specified name.
   *
   * @param name Method name.
   *
   * @return Method specified.
   *
   * @exception NoSuchElementException No available method with the name specified.
   *
   * @see MockServerMethod
   */
  private fun getMethodNamed(name: String): MockServerMethod =
    methodsList.first { method -> method.name == name }
}
