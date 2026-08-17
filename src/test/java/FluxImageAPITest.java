import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.DavidsCode.CommunityWebPage.service.FluxImageAPI;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FluxImageAPITest {

    FluxImageAPI imageAPI =  new FluxImageAPI();
    String prompt;
    @BeforeEach
    public void setup() {

        prompt = "make an image of a cat eating a giant banana";
    }

    @Test
    public void generateImageURLTest(){
        String imageUrl =
                imageAPI.generateImageURL(prompt)
                        .join();
        System.out.println(imageUrl);
        assertTrue(imageUrl.length() > 0);
    }

}
