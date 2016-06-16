package com.thingswise.appframework.example.spring;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.osgi.service.component.annotations.Component;

@Path("/test")
@Component(service=TestResource.class)
public class TestResource {

	@GET
	@Produces("text/plain")
	public String get() {
		return "Hello, world!";
	}

}
