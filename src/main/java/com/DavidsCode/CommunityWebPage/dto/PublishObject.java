package com.DavidsCode.CommunityWebPage.dto;

public class PublishObject {

    private Long id;
    private String author;
    private String title;
    // "POEM" or "ESSAY"
    private String typeField;
    private String body;
    private Long genreId;

    public PublishObject() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTypeField() { return typeField; }
    public void setTypeField(String typeField) { this.typeField = typeField; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Long getGenreId() { return genreId; }
    public void setGenreId(Long genreId) { this.genreId = genreId; }
}
