package com.DavidsCode.CommunityWebPage.entity;

/**
 * Represents the approval status of user-submitted content.
 * All new content starts as PENDING and must be approved by an admin
 * before it becomes visible on the public site.
 */
public enum ContentStatus {
    PENDING,   // Awaiting admin review — not visible to the public
    APPROVED,  // Admin approved — visible on the public site
    REJECTED   // Admin rejected — hidden from the public
}
