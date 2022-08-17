package com.ruled.mockwebserver.utils

import com.ruled.mockwebserver.domain.MockServerResponse
import okhttp3.mockwebserver.MockResponse

/**
 * Builds a [MockResponse] based on the data of a [MockServerResponse]. It works like a mapper
 * to translate this library response data to the response data of the Mock Web Server of the
 * OkHttp library.
 *
 * @return [MockResponse] based on the data of this [MockServerResponse].
 *
 * @see MockResponse
 * @see MockServerResponse
 */
fun MockServerResponse.asMockResponse(): MockResponse =
  MockResponse().apply {
    setResponseCode(code)
    setBody(body)
  }
