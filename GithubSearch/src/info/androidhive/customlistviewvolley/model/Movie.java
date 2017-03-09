package info.androidhive.customlistviewvolley.model;

import java.util.ArrayList;

public class Movie {
	private String image;
	private String username;
	private String profileurl;

	public Movie() {
	}

	public Movie(String image, String username, String profileurl) {
		this.image = image;
		this.username = username;
		this.profileurl = profileurl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileurl() {
		return profileurl;
	}

	public void setProfileurl(String profileurl) {
		this.profileurl = profileurl;
	}

}