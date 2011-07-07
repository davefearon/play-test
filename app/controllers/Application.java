package controllers;

//generic stuff
import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import java.util.*;
import javax.*;
import models.*;

//for file stuff
import java.io.*;
import play.data.*;
import play.db.jpa.Blob;

//for servicehub stuff
import play.libs.WS;
import play.libs.WS.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Application extends Controller {
	@Before
	static void addDefaults() {
		renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
		renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
	}

    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
		List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
		render(frontPost, olderPosts);
    }
	
	// START blog tutorial
	public static void show(Long id) {
		Post post = Post.findById(id);
		render(post);
	}
	
	public static void postComment(Long postId, @Required String author, @Required String content) {
		Post post = Post.findById(postId);
		if( validation.hasErrors() ) {
			render("Application/show.html", post);
		}
		post.addComment(author, content);
		flash.success("Thanks for posting %s", author);
		show(postId);
	}
	// END blog tutorial
	
	// START servicehub tests
	public static void testing() {
		String serviceName = "datadirector_eventschedule";
		String url = "https://204.232.202.221:59443/sec/servicehub-preprod/services/|service|";
		String tempUrl = url.replace("|service|", serviceName);
		
		String params[] = new String[1];
		params[0] = "appId=GMUSMOBILE2011";
		
		StringBuilder qs = new StringBuilder();
		qs.append(tempUrl);
		for( Integer i = 0; i < params.length; i++ ) {
			qs.append("&").append(params[i]);
		}
		String tmp = qs.toString();
		String username = "automation";
		String password = "rufrsi235";
		HttpResponse tbody = WS.url(tmp).authenticate(username, password).get();
		
		JsonElement body = tbody.getJson();
		render(body);
	}
	
	public static void servicehub() {
		String url = "https://204.232.202.221:59443/sec/servicehub-preprod/services/|service|";
		
		/*
		// GET
		String serviceName = "datadirector_eventschedule";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", "GMUSMOBILE2011");		
		ServiceHub sh = new ServiceHub(url);
		
		String result = sh.get(serviceName, params);
		render(result);
		/**/
		
		///*
		// POST
		String serviceName = "custom_GMUS_gmussubmitservice";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("ITS_EventName", "Military+Dev");
		params.put("ITS_EventId", "GMUSMOBILE20114e089249dfe9f");
		params.put("ITS_EventCode", "militarydev");
		params.put("data%5BITS_Scenario%5D", "play");
		params.put("data%5BITS_RemoteUser%5D", "123.456.789.0");
		params.put("data%5BITS_ClientId%5D", "GMUS");
		params.put("data%5BITS_SurveyId%5D", "GMUSMOBILE2011");
		params.put("data%5BITS_DeviceType%5D", "MOBILE-WEB");
		params.put("data%5Bcontactinfo_givenname%5D", "dave");
		params.put("data%5Bcontactinfo_surname%5D", "fearon");
		
		ServiceHub sh = new ServiceHub(url);
		String result = sh.post(serviceName, params);
		render(result);
		/**/
	}
	// END servicehub tests
	
	
	// START image tests
	public static void images() {
		render();
	}
	
	public static void addPhoto(Picture picture) {
		picture.save();
		images();
	}
	
	public static void getPhoto(long id) {
		final Picture picture = Picture.findById(id);
		notFoundIfNull(picture);
		response.setContentTypeIfNotSet(picture.image.type());
		renderBinary(picture.image.get());
	}
	
	public static void addPhotoWithFileName(File photo) throws FileNotFoundException {
		final Picture picture = new Picture();
		picture.fileName = photo.getName();
		picture.image = new DateBlob();
		picture.image.set(new FileInputStream(photo), MimeTypes.getContentType(photo.getName()));
		picture.save();
		images();
	}
	
	public static void downloadPhoto(long id) {
		final Picture picture = Picture.findById(id);
		response.setContentTypeIfNotSet(picture.image.type());
		renderBinary(picture.image.get(), picture.fileName);
	}
	// END images tests
}