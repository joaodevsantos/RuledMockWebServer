package com.ruled.mockwebserver

import com.ruled.mockwebserver.domain.MockServerResponse
import com.ruled.mockwebserver.utils.asMockResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

/**
 * Configuration of a Mock Web Server.
 *
 * It defines the settings that a Mock Web Server should implement in order to be properly
 * configured based on the real environment that it pretends to mock.
 */
abstract class MockServerConfig {
  /**
   * Local port where it will be listening.
   */
  open val port: Int = DEFAULT_PORT_NUMBER

  /**
   * Default response to be given if there is any problem dispatching a request.
   *
   * @see MockServerResponse
   */
  open val defaultResponse: MockServerResponse =
    MockServerResponse(code = HttpURLConnection.HTTP_BAD_GATEWAY)

  /**
   * List of available endpoints.
   *
   * As a Mock Web Server, this endpoints should be properly configured as the real endpoints. This
   * way, the Mock Web Server will be able to handle the server requests.
   *
   * @see MockServerEndpoint
   */
  abstract val endpoints: List<MockServerEndpoint>

  /**
   * Mock Web Server Dispatcher.
   *
   * Handles the network requests made to the Mock Web Server and return the desired response.
   *
   * @see Dispatcher
   */
  open val dispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse = getResponseFor(request)
  }

  /**
   * Gets the response for a specific HTTP request.
   *
   * @param request HTTP request to handle.
   *
   * @return Response to the HTTP request made. It returns the [defaultResponse] if any error
   * occurs.
   *
   * @see RecordedRequest
   * @see MockResponse
   */
  private fun getResponseFor(request: RecordedRequest): MockResponse =
    try {
      getEndpointResponseFor(request)
    } catch (e: Exception) {
      defaultResponse
    }.asMockResponse()

  /**
   * Gets the an endpoint response to an HTTP request.
   *
   * @param request HTTP request to handle.
   *
   * @return Response for the HTTP request made.
   *
   * @see MockServerResponse
   */
  private fun getEndpointResponseFor(request: RecordedRequest): MockServerResponse =
    getEndpointWithPath(request.path!!).getResponseFor(request)

  /**
   * Gets the endpoint with the specified path.
   *
   * @param path Endpoint path.
   *
   * @return Endpoint with the specified path.
   *
   * @exception NoSuchElementException No available endpoint matches the requested path.
   *
   * @see MockServerEndpoint
   */
  private fun getEndpointWithPath(path: String): MockServerEndpoint =
    endpoints.first { endpoint ->
      val pathWithoutQueryParameters = path.substringBefore("?")

      endpoint.path.matches(pathWithoutQueryParameters)
    }

  companion object {
    /**
     * Default port number for the Mock Web Server.
     */
    const val DEFAULT_PORT_NUMBER: Int = 8080
  }
}
