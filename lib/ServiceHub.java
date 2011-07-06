import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import java.util.*;

public class ServiceHub {
	private String url;
	public Int maxRedirs = 5;
	public Int timeout = 300;
	
	public static void setUrl( String url ) {
		this.url = url;
	}
	
	private JsonElement call( String method, String serviceName, String[] params, boolean async ) {
		String url = this.url;
		String tempUrl = url.replace("{service}", serviceName);
		
		StringBuilder qs = new StringBuilder();
		qs.append(url);
		for( Int i = 0; i < params.length; i++ ) {
			qs.append("&").append(params[i]);
		}
		
		if( method == "POST" ) {
			return;
		} else {
			return WS.url(qs).get().getJson();
		}
	}
	
	public JsonElement get( String serviceName, String[] params ) {
		return this.call("GET", serviceName, params, false);
	}
	
	public JsonElement post( String serviceName, String[] params ) {
		return this.call("POST", serviceName, params, false);
	}
}