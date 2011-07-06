import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
	public void createAndRetrieveUser() {
		//create a new user and save it
		new User("bob@gmail.com","secret","Bob").save();
		
		User bob = User.find("byEmail", "bob@gmail.com").first();
		
		assertNotNull(bob);
		assertEquals("Bob", bob.fullname);
	}
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();

	    // Test 
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNull(User.connect("bob@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	}

}
