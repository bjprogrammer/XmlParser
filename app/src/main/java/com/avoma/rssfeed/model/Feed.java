package com.avoma.rssfeed.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict =  false)
public class Feed
{
    @ElementList(entry = "item",  required = false, inline = true)
    @Path("channel")
    private List<Item> item;

    @Element(name = "lastBuildDate")
    @Path("channel")
    private String lastBuildDate;

    @Element(name = "description")
    @Path("channel")
    private String description;

    @Element(name = "generator")
    @Path("channel")
    private String generator;

    @Element(name = "webMaster", required = false)
    @Path("channel")
    private String webMaster;

    public List<Item> getItem ()
    {
        return item;
    }

    public void setItem (List<Item> item)
    {
        this.item = item;
    }

    public String getLastBuildDate ()
    {
        return lastBuildDate;
    }

    public void setLastBuildDate (String lastBuildDate)
    {
        this.lastBuildDate = lastBuildDate;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getGenerator ()
    {
        return generator;
    }

    public void setGenerator (String generator)
    {
        this.generator = generator;
    }

    public String getWebMaster ()
    {
        return webMaster;
    }

    public void setWebMaster (String webMaster)
    {
        this.webMaster = webMaster;
    }
}