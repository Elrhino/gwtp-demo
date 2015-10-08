package com.arcbees.demo.client.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.arcbees.demo.client.api.domain.Contact;
import com.gwtplatform.dispatch.rest.shared.RestAction;

import static com.arcbees.demo.client.api.ApiPaths.TUTORIAL;

@Path(TUTORIAL)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContactResource {
    @GET
    RestAction<List<Contact>> getContacts();
}
