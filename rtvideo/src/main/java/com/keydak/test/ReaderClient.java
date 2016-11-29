package com.keydak.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
/**
 * Created by admin on 2016/10/17.
 */
public class ReaderClient {
    public static void main(String[] args){
        Client client= Client.create();
        WebResource resource=client.resource("http://localhost:8084/readerService/kay/1231239879987");
       // System.out.println( resource.toString());
        Reader reader=resource.get(Reader.class);
        System.out.println(reader);
    }
}
