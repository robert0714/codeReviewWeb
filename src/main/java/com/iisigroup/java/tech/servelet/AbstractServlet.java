package com.iisigroup.java.tech.servelet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;

public abstract class AbstractServlet implements org.springframework.web.HttpRequestHandler{
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
		
		switch (requestMethod) {
		case POST:
			doPost(request, response);
			break;
		case GET:
			doGet(request, response);
			break;
		case DELETE:
			break;
		case HEAD:
			break;
		case OPTIONS:
			break;
		case PATCH:
			break;
		case PUT:
			break;
		case TRACE:
			break;
		default:
			break;
		}		
	}

	abstract void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
	abstract void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
