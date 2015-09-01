package com.iisigroup.sonar.httpclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract  class BaseCommonHttpMethod {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseCommonHttpMethod.class);
 
	
	
	 
	public String extractString(final String content,final String regExpress){
    	final	Matcher matcher = Pattern.compile(regExpress).matcher(content);
    	if(matcher.find()){
    		return matcher.group(matcher.groupCount());
    	}
    	return null;
    }
	 public CloseableHttpClient getHttpClient(final String proxyIp,final int  port ) {
	        try {
	        	final SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
	        	if(StringUtils.isBlank(proxyIp)){
	        		
	    			final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf) .build();
	    			return httpclient;
	        	}else{
	        		final HttpHost proxy = new HttpHost(proxyIp, port);
	    			final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
	    			final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setRoutePlanner(routePlanner).build();
	                
	                return httpclient;
	        	} 
	        } catch (Exception e) {
	        	LOGGER.error(e.getMessage(),e);
	            return HttpClients.createDefault();
	        }
	    }

	    public SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

	            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
	            }

	            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
	            }
	        } };
	        SSLContext sslContext = SSLContext.getInstance("SSL");
	        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new TrustingHostnameVerifier());
	        return sslsf;
	    }

	    private static final class TrustingHostnameVerifier implements X509HostnameVerifier {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	        @Override
	        public void verify(String host, SSLSocket ssl) throws IOException {
	        }
	        @Override
	        public void verify(String host, X509Certificate cert) throws SSLException {
	        }
	        @Override
	        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
	        }
	    } 
	    public void getResponseByGet(final  org.apache.http.client.HttpClient httpclient , final String... urls){
	    	for(String url : urls){
	    		 final HttpGet httpGet = new HttpGet(url);
				try {
					final HttpResponse response1 = httpclient.execute(httpGet);
					final HttpEntity entity1 = response1.getEntity();
		            EntityUtils.consume(entity1);
				} catch (ClientProtocolException e) {
					LOGGER.error(e.getMessage(),e);
				} catch (IOException e) {
					LOGGER.error(e.getMessage(),e);
				}	            
	    	}
	    }
}
