package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.apache.http.HttpEntity
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

class DartFormatClient(private val baseUrl: String)
{
    private val httpClient: HttpClient
    private val closeableHttpClient: CloseableHttpClient

    init
    {
        @Suppress("HttpUrlsUsage")
        if (!baseUrl.startsWith("http://"))
            throw DartFormatException.localError("DartFormatClient: expected URL but got: $baseUrl")

        httpClient = HttpClient.newHttpClient()

        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(Constants.HTTP_CLIENT_CONNECT_TIMEOUT_IN_SECONDS * 1000)
            .setConnectionRequestTimeout(Constants.HTTP_CLIENT_CONNECTION_REQUEST_TIMEOUT_IN_SECONDS * 1000)
            .setSocketTimeout(Constants.HTTP_CLIENT_SOCKET_TIMEOUT_IN_SECONDS * 1000).build()
        closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()
    }

    suspend fun get(path: String): HttpResponse<String>
    {
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl$path"))
            .header("User-Agent", "DartFormatPlugin")
            .header("Content-Type", "text/plain; charset=utf-8")
            .GET()
            .build()

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).await()
    }

    suspend fun post(path: String, entity: HttpEntity): CloseableHttpResponse
    {
        val httpRequest = HttpPost("$baseUrl$path")
        httpRequest.entity = entity

        val startTime = Date()
        val result: Any = withContext(Dispatchers.IO) { closeableHttpClient.execute(httpRequest, null) }
        val endTime = Date()
        val diffTime = endTime.time - startTime.time
        val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"
        if (Constants.LOG_VERBOSE) Logger.logVerbose("closeableHttpClient.execute took $diffTimeText")

        @Suppress("KotlinConstantConditions")
        if (result !is CloseableHttpResponse)
            throw DartFormatException.localError("DartFormatClient.post: expected CloseableHttpResponse but got: ${result::class.java.typeName} $result")

        return result
    }
}
