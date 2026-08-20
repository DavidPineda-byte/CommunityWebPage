import com.DavidsCode.CommunityWebPage.dto.Option;
import com.DavidsCode.CommunityWebPage.dto.OptionList;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class optionListTest {

    OptionList optionList;
    @BeforeEach
    public void setup(){
        List<ContentItem> items = new ArrayList<>();
        Poem poem = new Poem();
        poem.setId(1L);
        poem.setTitle("Test Poem");
        items.add(poem);

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");
        genres.add(genre);

        optionList = new OptionList(items, genres);
    }
    @Test
    public void getFullOptionListTest(){
        ArrayList<Option> allContent = optionList.getFullOptionsList();
        for(Option option:allContent){
            System.out.println(option);
        }
        assertFalse(allContent.isEmpty());
    }
}
