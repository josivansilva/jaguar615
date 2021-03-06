/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

@SPlcUtil
@QPlcDefault
public class PlcHttpClientUtil {
	
	private static final String HTTP_PREFFIX = "http://";

	private HttpClient client;
	
	private String urlApp;
	
	private HttpMethod lastMethod;

	private boolean autenticacaoAutomatica;
	
	public PlcHttpClientUtil(){
		
	}

	public PlcHttpClientUtil(String urlApp) {
		
		if (!urlApp.startsWith(HTTP_PREFFIX)) {
			urlApp = HTTP_PREFFIX.concat(urlApp);
		}
		
		this.urlApp = urlApp;
		
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter(HttpClientParams.USER_AGENT, "Mozilla/5.0");
		clientParams.setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, Boolean.TRUE);
		
		this.client = new HttpClient(clientParams);
		this.client.setState(new HttpState());
	}

	public PlcHttpClientUtil(String host, int port, String app, String usuario, String senha) {
		this("http://" + host + ":" + port + "/" + app);
		this.client.getState().setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(usuario, senha));
		this.autenticacaoAutomatica = true;
	}

	public boolean doLogin(String login, String senha) throws IOException {

		String url = adjustUrl("/j_security_check");
		// Tenta Logar 5 vezes.
		for (int i = 0; i < 3; i++) {
		
			// faz um get para obter e configurar cookies. Tenta jsf, se nulo, tenta struts.
			String retorno = doGet( adjustUrl("/f/t/inicial") );
			if (retorno == null) {
				retorno = doGet( adjustUrl("/") );
			}
			
			PostMethod post = new PostMethod(url);
			
			post.setRequestBody(new NameValuePair[]{
				new NameValuePair("j_username", login),
				new NameValuePair("j_password", senha)
			});
			
			int statusCode = execute(post);
			
			if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				// Executa os Redirects para setar os Cookies!
				if (isLoginPage(getRedirect(post))) {
					continue;
				} else {
					return true;
				}
			}
			
			if (isLoginPage(post.getResponseBodyAsString())) {
				break;
			}
			
//			return true;
		}
		return false;
	}
	
	public String doGet(String url) throws HttpException, IOException {
		
		GetMethod get = new GetMethod(adjustUrl(url));
		// Vaz a verificação automatica de Login e Senha, utilizando o Credentials utilizado.
		get.setDoAuthentication(this.autenticacaoAutomatica);
		
		int statusCode = execute(get);
		
		if (statusCode == HttpStatus.SC_OK) {
			return get.getResponseBodyAsString();
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
			return getRedirect(get);
		} else {
			return null;
		}
	}
	
	public String doPut(String url, String body) throws HttpException, IOException {
		
		PutMethod put = new PutMethod(adjustUrl(url));
		
		// Vaz a verificação automatica de Login e Senha, utilizando o Credentials utilizado.
		put.setDoAuthentication(this.autenticacaoAutomatica);
		
		if(body != null && StringUtils.isNotEmpty(body)) {
			put.setRequestBody(body);
		}
		int statusCode = execute(put);
		
		if (statusCode == HttpStatus.SC_OK) {
			return put.getResponseBodyAsString();
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
			return getRedirect(put);
		} else {
			return null;
		}
	}
	
	public String doPost(String url, List<String[]> params) throws HttpException, IOException {
		return doPost(url, params, null);
	}
	
	public String doPost(String url, List<String[]> params, String body) throws HttpException, IOException {
		
		PostMethod post = new PostMethod(adjustUrl(url));
		
		if (autenticacaoAutomatica) {
			// Vaz a verificação automatica de Login e Senha, utilizando o Credentials utilizado.
			post.setDoAuthentication(autenticacaoAutomatica);
		}
		
		if (params != null && !params.isEmpty()) {
			for (String[] param : params) {
				if (param.length == 2) {
					post.addParameter(param[0], param[1]);
				} else if (param.length > 2) {
					for (int i = 1; i < param.length; i++) {
						post.addParameter(param[0], param[i]);
					}
				}
			}
		}
		
		if(body != null) {
			post.setRequestBody(body);
		}
		
		int statusCode = execute(post);
		
		if (statusCode == HttpStatus.SC_OK) {
			return post.getResponseBodyAsString();
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
			return getRedirect(post);
		} else {
			return null;
		}
	}
	
	public boolean isLoginPage(String content) {
		return content != null && content.contains("action=\"j_security_check\"");
	}
	
	public HttpMethod getLastMethod() {
		return lastMethod;
	}
	
	public String getCookie(String name) {
		HttpState state = client.getState();
		Cookie[] cookies = state.getCookies();
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	protected String adjustUrl(String url) {
		if (!url.startsWith(HTTP_PREFFIX) && !url.startsWith(urlApp)) {
			return urlApp + url;
		}
		return url;
	}
	
	public String getBodyString() throws IOException {
		if (lastMethod != null) {
			return lastMethod.getResponseBodyAsString();
		}
		return null;
	}
	
	protected int execute(HttpMethod method) throws IOException {
		this.lastMethod = method;
		return client.executeMethod(method);
	}
	
	protected String getRedirect(HttpMethod method) throws IOException {
		
		Header location = method.getResponseHeader("location");
		
		for (int i = 0; i < 10; i++) {
			
			if (location != null) {
				
				GetMethod get = new GetMethod(adjustUrl(location.getValue()));
				
				int statusCode = execute(get);
				
				if (statusCode == HttpStatus.SC_OK) {
					return get.getResponseBodyAsString();
				} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
					location = get.getResponseHeader("location");
				}
			} else {
				break;
			}
		}
		
		throw new IllegalStateException("Redirect Loop");
	}
	
	public String getAppInfo() throws HttpException, IOException {
		// Tenta obter via url jsf (tambem vale pra facelets)
		String xml = this.doGet("/f/plc.servico.appinfo");
		if (xml == null) {
			// se não conseguiu, tenta obter via url struts...
			xml = this.doGet("/plc.servico.appinfo.do");
		}
		return xml;	

	}
	
	
	public static void main(String[] args) {

		PlcHttpClientUtil httpClient = new PlcHttpClientUtil("localhost", 8080, "activiti-rest", "kermit", "kermit");

		try {
			
			String resp = httpClient.doPost("/service/process-instance ", null, "{'processDefinitionId':'financialReport:1' }");
			
			System.out.println("getDoAuthentication: " + httpClient.getLastMethod().getStatusCode());
			
			System.out.println("/service/process-definitions\n" + resp);
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

