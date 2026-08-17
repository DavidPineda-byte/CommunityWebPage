import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static spark.Spark.post;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StripeAPITest {
  @BeforeEach
  public void setup() {

  }

  @Test
  public void createCheckoutSession() throws IOException, InterruptedException {

      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/payment/create-checkout-session"))
              .POST(HttpRequest.BodyPublishers.noBody())
              .build();

      HttpResponse<String> response =
              client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println(response.body());

  }
}
