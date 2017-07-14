package com.jesushghar.uss.pojo;

public class Notice {

    private String title;
    private String pubDate;
    private String description;
    private String link;
    private Integer bookmarked;

    public Notice(String title, String pubDate, String description, String link) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.link = link;
        this.bookmarked = 0;
    }

    public String getPubDate ()
    {
        return pubDate;
    }

    public void setPubDate (String pubDate)
    {
        this.pubDate = pubDate;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pubDate = " + pubDate + ", title = " + title + ", description = "+ description +", link = "+ link + "]";
    }

}
