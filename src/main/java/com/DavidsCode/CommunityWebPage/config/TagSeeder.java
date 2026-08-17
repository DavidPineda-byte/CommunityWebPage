package com.DavidsCode.CommunityWebPage.config;

import com.DavidsCode.CommunityWebPage.entity.Tag;
import com.DavidsCode.CommunityWebPage.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TagSeeder {

    @Bean
    public CommandLineRunner seedTags(TagRepository tagRepository) {
        return args -> {
            if (tagRepository.count() == 0) {
                List<String> tagNames = Arrays.asList(
                        "Poetry", "Philosophy", "Essays", "Technology", "Art",
                        "Culture", "Society", "History", "Science", "Nature",
                        "Life", "Love", "Loss", "Hope", "Despair",
                        "Music", "Literature", "Film", "Photography", "Design",
                        "Architecture", "Psychology", "Sociology", "Politics", "Economics",
                        "Health", "Wellness", "Mental Health", "Fitness", "Nutrition",
                        "Travel", "Adventure", "Exploration", "Space", "Universe",
                        "Spirituality", "Religion", "Meditation", "Mindfulness", "Yoga",
                        "Education", "Learning", "Teaching", "Parenting", "Childhood",
                        "Career", "Business", "Entrepreneurship", "Finance", "Investing",
                        "Writing", "Reading", "Creativity", "Inspiration", "Motivation"
                );

                for (String name : tagNames) {
                    Tag tag = new Tag();
                    tag.setName(name);
                    tag.setDescription("Articles related to " + name.toLowerCase());
                    tagRepository.save(tag);
                }
            }
        };
    }
}
