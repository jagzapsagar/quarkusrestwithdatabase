package com.example.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mobile")
public class MobileResource {

	@Inject
	PersonRepository repo;
	
	@Inject
	Logger logger;

	List<Mobile> list = new ArrayList<>();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		// return Response.ok(list).build();
		logger.info("getting all list........");
		return Response.ok(repo.listAll()).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getById(@PathParam("id") long id) {

		Mobile m = repo.findById(id);
		if (m != null) {
			return Response.ok(m).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Mobile with ID " + id + " not found.").build();
		}

		/*
		 * Optional<Mobile> first = list.stream().filter(e ->
		 * e.getId()==id).findFirst(); if(first.isPresent()) { return
		 * Response.ok(first.get()).build(); }else { return
		 * Response.ok(" Not Found with id: "+id).build(); }
		 */

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response add(Mobile m) {
		// list.add(m);
		repo.persist(m);
		return Response.ok(m).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	@Transactional
	public Response update(@PathParam("id") long id, Mobile m) {

		Mobile mobile = repo.findById(id);

		if (mobile != null) {
			mobile.setId(m.getId());
			mobile.setName(m.getName());
			mobile.setPrice(m.getPrice());
			// No explicit persist needed, Panache will automatically update it
			return Response.ok(mobile).build();
			// return Response.ok(m).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Mobile with ID " + id + " not found.").build();
		}
		// list = list.stream().map(k -> k.getId() == id ? m : k
		// ).collect(Collectors.toList());
		// return Response.ok(m).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	@Transactional
	public Response delete(@PathParam("id") long id) {

		boolean deleteById = repo.deleteById(id);
		return Response.ok(deleteById == true ? "Mobile Deleted" : "Mobile NOT FOUND with id: " + id).build();

		/*
		 * Optional<Mobile> first = list.stream().filter(e ->
		 * e.getId()==id).findFirst(); if(first.isPresent()) { list.remove(first.get());
		 * return Response.ok("Mobile Deleted from new App").build(); }else { return
		 * Response.ok(" Not Found with id: "+id).build(); }
		 */

	}

}
