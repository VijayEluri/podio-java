package com.podio;

import java.util.Map;

import javax.ws.rs.core.Response.Status.Family;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.filter.ClientFilter;

public class ExceptionFilter extends ClientFilter {

	@Override
	public ClientResponse handle(ClientRequest cr)
			throws ClientHandlerException {
		ClientResponse response = getNext().handle(cr);
		if (response.getClientResponseStatus().getFamily() != Family.SUCCESSFUL) {
			Map<String, Object> errorData = response
					.getEntity(new GenericType<Map<String, Object>>() {
					});

			throw new APIException(
					response.getClientResponseStatus(),
					(String) errorData.get("error"),
					(String) errorData.get("error_description"),
					(Map<String, Object>) errorData.get("parameters"));
		} else {
			return response;
		}
	}
}