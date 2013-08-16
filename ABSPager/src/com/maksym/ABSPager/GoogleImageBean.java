package com.maksym.ABSPager;

public class GoogleImageBean
{
	String thumbUrl;
	String title;
    Boolean isFavorite=false;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GoogleImageBean(String thumbUrl, String title) {
        this.thumbUrl = thumbUrl;
        this.title = title;
        this.isFavorite=true;
    }

    public GoogleImageBean() {
    }

    public String getThumbUrl()
	{
		return thumbUrl;
	}

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setThumbUrl(String url)

	{
		this.thumbUrl = url;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
}