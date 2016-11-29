package com.keydak.test;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by admin on 2016/10/17.
 */
//@WebService()
@Path("readerService/{name}/{password}")
public class ReaderService {
    @GET
    //@Produces("application/json;charset=utf8")
   // @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Reader getReader(@PathParam("name") String name,@PathParam("password")
            String password ){
            Reader reader=new Reader(name,password);
//            reader.setName(name);
//            reader.setPassword(password);
            return reader;
    }
    public static void main(String[] args) throws IllegalArgumentException,IOException,URISyntaxException{
        HttpServer server= HttpServerFactory.create("http://localhost:8084/");
        server.start();
    }
}
