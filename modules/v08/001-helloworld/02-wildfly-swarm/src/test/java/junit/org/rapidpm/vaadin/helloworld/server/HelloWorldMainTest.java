package junit.org.rapidpm.vaadin.helloworld.server;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import org.wildfly.swarm.Swarm;

/**
 *
 */
@Disabled
public class HelloWorldMainTest {

  private Swarm swarm;

  @BeforeEach
  public void setUp() throws Exception {
    swarm = new Swarm();
    swarm.start().deploy();
  }

  @AfterEach
  public void tearDown() throws Exception {
    swarm.stop();
  }

  @Test
  public void test001() throws Exception {
    Request request = new Request.Builder()
        .url("http://127.0.0.1:8080/helloworld")
        .build();

    OkHttpClient client = new OkHttpClient();
    Response response = client.newCall(request).execute();

    Assertions.assertNotNull(response);
    ResponseBody body = response.body();
    Assertions.assertNotNull(body);
    System.out.println("body = " + body);
    Assertions.assertEquals("Hello World", body.string());
  }
}
