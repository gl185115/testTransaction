/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * AppServerResource
 *
 * Resource which provides a list of Application Server for checking WebApp connection.
 *
 */
package ncr.res.mobilepos.callExternalApi.resource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.callExternalApi.model.ExternalApi;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * CallExternalApiResource Web Resource Class.
 * This gets external APIs.
 */
@Path("/callExternal")
@Api(value = "/callExternal", description = "外部API呼び出し")
public class CallExternalApiResource {
	/**
	 * The program name.
	 */
	private static final String PROG_NAME = "CalExApi";
	/**
	 * The external URL head.
	 */
	private static final String URL_HEAD = "https://";
	/**
	 * The external URL foot.
	 */
	private static final String URL_FOOT = ":8443/resTransaction/rest/selfmode/getStatus";
	/**
	 * The request method name get.
	 */
	private static final String HTTP_REQUEST_METHOD_GET = "get";
	/**
	 * The request method name post.
	 */
	private static final String HTTP_REQUEST_METHOD_POST = "post";
	/**
	 * the instance of the logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * Default Constructor for CallExternalApiResource.
	 */
	public CallExternalApiResource() {
	}

	/**
	 * Request To ~.
	 * @param Resource
	 * @param HttpMethod
	 * @return
	 */
	@Path("/get")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "getExternalApiResult", response = ExternalApi.class)
	@Consumes("application/x-www-form-urlencoded")
	public final ExternalApi getExternalApiResult(
		@ApiParam(name = "Resource", value = "ホスト") @QueryParam("Resource") final String Resource,
		@ApiParam(name = "HttpMethod", value = "HTTPメソッド") @QueryParam("HttpMethod") final String HttpMethod
	) {
		String functionName = DebugLogger.getCurrentMethodName();
		String resp = null;
		String url = URL_HEAD + Resource + URL_FOOT;
		Trace.Printer tp = DebugLogger.getDbgPrinter(
				Thread.currentThread().getId(),
				getClass()
		);
		if (tp != null) {
			tp.methodEnter(functionName);
		}

		ExternalApi api = new ExternalApi();
		Client client = this.CreateCertIgnoreClient();
		client.addFilter(new HTTPBasicAuthFilter(GlobalConstant.getSelfRapAuthenticationUid(), GlobalConstant.getSelfRapAuthenticationPassword()));

		try {
		    switch (HttpMethod.toLowerCase()) {
		    case HTTP_REQUEST_METHOD_POST:
		    	resp = client.resource(url).post(String.class);
			    break;
		    case HTTP_REQUEST_METHOD_GET:
		    default:
		    	resp = client.resource(url).get(String.class);
		    	break;
		    }
		    api.setResultData(resp);
		} catch (Exception ex) {
			LOGGER.logAlert(
					PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					" Failed to request external api:" + ex.getMessage()
			);
			api.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			api.setResultData("Exception occured, resp:" + resp + ", ex:" + ex.getMessage());
		} finally {
			tp.methodExit(api);
		}
		return api;
	}

	/**
	 * To ignore SSL certification.
	 * @return
	 */
	private Client CreateCertIgnoreClient() {
		String functionName = "CreateSslCertIgnore";
		ClientConfig conf = new DefaultClientConfig();
		try {
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
					@Override
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
					@Override
					public X509Certificate[] getAcceptedIssuers() { return null; }
				}
			};

			SSLContext sslcontext = SSLContext.getInstance( "TLS" );
	        sslcontext.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			conf.getProperties().put(
				HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(
				     new HostnameVerifier() {
						@Override
						public boolean verify(String arg0, SSLSession arg1) { return true; }
				     },
				     sslcontext
				)
			);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			LOGGER.logAlert(
					PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to CreateSslCertIgnore:" + e.getMessage()
			);
		}
		Client client = Client.create(conf);
		return client;
	}
}
