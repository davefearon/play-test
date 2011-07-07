package models;

import play.*;
import play.db.jpa.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ServiceHub extends Model {
	private String url;
	public Integer maxRedirs = 5;
	public Integer timeout = 300;
	private String username = "automation";
	private String password = "rufrsi235";
	
	public ServiceHub( String url ) {
		this.url = url;
	}
	
	private String call( String method, String serviceName, Map<String, String> params ) {
		String url = this.url;
		String tempUrl = url.replace("|service|", serviceName);
		
		HttpResponse tbody;
		if( method == "POST" ) {
			WSRequest ws;
			ws = WS.url(tempUrl).authenticate(this.username, this.password);
			for( Entry<String, String> p : params.entrySet() ) {
				System.out.println(p.getKey() + "=" + p.getValue());
				ws.setParameter(p.getKey(), p.getValue());
			}
			tbody = ws.post();
		} else {
			StringBuilder qs = new StringBuilder();
			qs.append(tempUrl).append("&");
			for( Entry<String, String> p : params.entrySet() ) {
				qs.append(p.getKey() + "=" + p.getValue()).append("&");
			}
			String finalUrl = qs.toString();
			tbody = WS.url(finalUrl).authenticate(this.username, this.password).get();
		}
		
		//JsonElement body = tbody.getJson();
		String body = tbody.getString();
		
		return body;
	}
	
	public String get( String serviceName, Map<String, String> params ) {
		return this.call("GET", serviceName, params);
	}
	
	public String post( String serviceName, Map<String, String> params ) {
		return this.call("POST", serviceName, params);
	}
}