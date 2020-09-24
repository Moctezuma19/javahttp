import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//import java.util.Scanner;

public class Handler implements HttpHandler {
	@Override
    public void handle(HttpExchange t) {
    	try {
        	Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
        	String response = params.get("id");
        	t.sendResponseHeaders(200, response.length());
        	OutputStream os = t.getResponseBody();
        	os.write(response.getBytes());
        	os.close();
    	} catch(IOException e){
    		System.out.println(e.getMessage());
    	} catch(Exception k){
    		System.out.println(k.getMessage());
    	}
   	}
   	public Map<String, String> queryToMap(String query) {
    	Map<String, String> result = new HashMap<>();
    	for (String param : query.split("&")) {
        	String[] entry = param.split("=");
        	if (entry.length > 1) {
            	result.put(entry[0], entry[1]);
        	}else{
            	result.put(entry[0], "");
        	}
    	}
    	return result;
	}
}