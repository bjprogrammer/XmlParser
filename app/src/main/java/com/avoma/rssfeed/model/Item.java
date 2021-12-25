package com.avoma.rssfeed.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Entity(tableName = "feed")
@Root(name = "item", strict =  false)
public class Item implements Comparable<Item>
{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Boolean getBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    private Boolean isBookmarked= false;

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Element(name = "encoded", required = false, data =  true)
    @Namespace(reference="http://purl.org/rss/1.0/modules/content/", prefix="content")
    private String encoded;

    @Element(name = "creator", required = false, data = true)
    @Namespace(reference="http://purl.org/dc/elements/1.1/", prefix="dc")
    private String creator;

    @Element(name = "guid", required = false)
    private String guid;

    @Path("title")
    @Text(required = false)
    private String title;

    @ElementList(entry= "category", inline = true, required = false, data =  true)
    public List<String> category;

    @Element(name = "pubDate", required = false)
    private String pubDate;

    public String getGuid ()
    {
        return guid;
    }

    public void setGuid (String guid)
    {
        this.guid = guid;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public List<String> getCategory ()
    {
        return category;
    }

    public void setCategory (List<String> category)
    {
        this.category = category;
    }

    public String getPubDate ()
    {
        return pubDate;
    }

    public void setPubDate (String pubDate)
    {
        this.pubDate = pubDate;
    }

    @Override
    public int compareTo(Item o) {
        return getPubDate().compareTo(o.getPubDate());
    }
}