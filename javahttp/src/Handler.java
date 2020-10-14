import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;


public class Handler implements HttpHandler {
	@Override
    public void handle(HttpExchange t) {
    	try {
        	Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
            String tipo = params.get("op");
            int codigo = 200;
            if(tipo == null){
                tipo = "na";
            }
            String respuesta = null;
            //se identifica el tipo de operacion y se arma la respuesta
            switch (tipo){
                case "rep":
                            respuesta = params.get("id");
                            String token_a = t.getRequestHeaders().getFirst("Authorization");
                            if(token_a == null || respuesta == null){
                                codigo = 401;
                                respuesta = "No autorizado";
                                break;
                            }
                            String token = token_a.split(" ")[1];
                            if(!verificaToken(token)){
                                respuesta = "No autorizado";
                                codigo = 401;
                            } 
                            break;
                case "aut":
                            String usuario = params.get("usuario");
                            String clave = params.get("clave");
                            if(usuario == null || clave == null){
                                codigo = 404;
                                respuesta = "Faltan parametros";
                            } else if(validaUsuario(usuario,clave)){
                                //validaUsuario(usuario,clave);
                                respuesta = obtenToken(usuario);
                            } else {
                                codigo = 401;
                                respuesta = "Usuario o clave no validos";
                            }
                            //verificar que el usuario y clave sean correctos
                            break;
                default: codigo = 404; respuesta = "Operación inválida";
            }
            byte[] bs = respuesta.getBytes("UTF-8");
        	t.sendResponseHeaders(codigo, bs.length);
        	OutputStream os = t.getResponseBody();
        	os.write(bs);
        	os.close();
    	} catch(IOException e){
    		System.out.println(e);
    	} catch(Exception k){
    		System.out.println(k);
    	}
   	}
   	public Map<String, String> queryToMap(String query) {
        if(query == null){
            return null;
        }
        if(query.length() == 0){
            return null;
        }
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

    private String obtenToken(String usuario){
        Signer signer = HMACSigner.newSHA256Signer("secreto");
        JWT jwt = new JWT().setIssuer("localhost")
                           .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                           .setSubject(usuario)
                           .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(1));
        String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        return encodedJWT;
    }

    private boolean verificaToken(String token){
        try{
            Verifier verifier = HMACVerifier.newVerifier("secreto");
            JWT jwt2 = JWT.getDecoder().decode(token, verifier);
        }catch(JWTExpiredException e){
            return false;
        }
        return true;
        /*ZonedDateTime ahora = ZonedDateTime.now(ZoneOffset.UTC);
        if(ahora.compareTo(jwt2.expiration)> 0){
            return false;
        }
        return true;*/
    }

    private boolean validaUsuario(String usuario, String clave){
        
        String url = "jdbc:postgresql://db/postgres";
        String user = "postgres";
        String password = "1234";
        
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = null;
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIO where usuario = '" + usuario + "' and contrasena = '" + clave + "'");
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (ClassNotFoundException f){
            System.out.println(f.getMessage());
            return false;
        }
        
    }
}