package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.dto.PoemDraft;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe, in-memory "Filing Cabinet" for poem drafts.
 * This survives the trip to Stripe and back without needing a database.
 */
@Service
public class TempPoemStorage {
    
    // ConcurrentHashMap handles multiple users safely
    private final Map<String, PoemDraft> storage = new ConcurrentHashMap<>();

    public String save(PoemDraft draft) {
        String id = UUID.randomUUID().toString();
        storage.put(id, draft);
        return id;
    }

    public PoemDraft get(String id) {
        return storage.get(id);
    }

    public void remove(String id) {
        storage.remove(id);
    }
}
