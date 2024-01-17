package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import kotlinx.coroutines.future.await
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

class DartFormatClient(private val baseUrl: String)
{
    private val httpClient: HttpClient
    private val closeableHttpClient: CloseableHttpClient

    init
    {
        @Suppress("HttpUrlsUsage")
        if (!baseUrl.startsWith("http://"))
            throw Exception("DartFormatClient: expected URL but got: $baseUrl")

        httpClient = HttpClient.newHttpClient()

        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000)
            .setConnectionRequestTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000)
            .setSocketTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000).build()
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

    fun post(path: String, entity: HttpEntity?): CloseableHttpResponse
    {
        val httpRequest = HttpPost("$baseUrl$path")
        httpRequest.entity = entity
        return closeableHttpClient.execute(httpRequest, null)
    }
}
