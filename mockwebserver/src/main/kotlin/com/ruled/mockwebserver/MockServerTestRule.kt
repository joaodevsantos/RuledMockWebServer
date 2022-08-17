package com.ruled.mockwebserver

import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.rules.ExternalResource
import org.junit.rules.TestRule
import java.io.IOException
import javax.net.ssl.SSLSocketFactory
import kotlin.reflect.typeOf

/**
 * JUnit Rule that manages the Mock Web Server on a test suit.
 *
 * It needs to be added as a variable along with the @Rule annotation. This way, it is initialized
 * before each test, where it starts the Mock Web Server, and also cleaned after each, where it
 * shutdown the Mock Web Server.
 *
 * @param mockServerConfig Configuration to be used to set up the Mock Web Server.
 *
 * @see TestRule
 * @see MockServerConfig
 */
open class MockServerTestRule(
  private val mockServerConfig: MockServerConfig
) : ExternalResource() {
  /**
   * Path to the SSL certificate resource.
   */
  open val sslCertificateResourcePath: String = "res/raw/mock_server.pem"

  /**
   * Raw SSL certificate to set up.
   */
  open val rawSslCertificate: String
    get() = typeOf<Class<*>>().javaClass
      .classLoader!!
      .getResource(sslCertificateResourcePath)
      .readText()

  /**
   * Held certificate to setup SSL.
   *
   * @see HeldCertificate
   */
  open val heldSslCertificate: HeldCertificate? = null

  /**
   * Mock Web Server instance.
   *
   * @see MockWebServer
   */
  private lateinit var _mockWebServer: MockWebServer

  /**
   * Starts the Mock Web Server.
   *
   * To start the Mock Web Server a new instance is created and then configured based on the
   * specified configuration.
   *
   * @throws IllegalStateException If there is some [IOException] on the setup server process.
   *
   * @see MockWebServer
   * @see MockServerConfig
   */
  @Throws(IllegalArgumentException::class)
  override fun before() =
    try {
      _mockWebServer = MockWebServer().apply {
        setupSsl()

        start(mockServerConfig.port)
        dispatcher = mockServerConfig.dispatcher
      }
    } catch (e: IOException) {
      throw IllegalStateException("Mock web server start issue.")
    }

  /**
   * Stops the Mock Web Server.
   *
   * @throws IllegalStateException If there is some [IOException] on the shutdown server process.
   */
  @Throws(IllegalArgumentException::class)
  override fun after() =
    try {
      _mockWebServer.shutdown()
    } catch (e: IOException) {
      throw IllegalStateException("Mock web server shutdown issue.")
    }

  /**
   * Sets up SSL on the Mock Web Server.
   */
  private fun MockWebServer.setupSsl() {
    useHttps(
      sslSocketFactory = getSslSocketFactory(),
      tunnelProxy = false
    )
  }

  /**
   * Gets the SSL socket factory of the Mock Web Server.
   *
   * @see SSLSocketFactory
   */
  private fun getSslSocketFactory(): SSLSocketFactory =
    getHandshakeCertificates().sslSocketFactory()

  /**
   * Gets the handshake certificates to setup SSL on the Mock Web Server.
   *
   * @see HandshakeCertificates
   */
  private fun getHandshakeCertificates(): HandshakeCertificates =
    HandshakeCertificates.Builder()
      .heldCertificate(
        heldCertificate = heldSslCertificate ?: getHeldCertificate()
      )
      .build()

  /**
   * Gets an [HeldCertificate] of the Mock Web Server's raw SSL certificate.
   *
   * @see HeldCertificate
   */
  private fun getHeldCertificate(): HeldCertificate =
    HeldCertificate.decode(rawSslCertificate)
}
