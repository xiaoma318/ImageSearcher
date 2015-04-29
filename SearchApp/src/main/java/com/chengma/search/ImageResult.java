package com.chengma.search;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chengma on 2/5/15.
 */
public class ImageResult implements Serializable {

	private static final long serialVersionUID = -2413015562395125127L;
	private String fullUrl;
	private String tbUrl;

	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.tbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getTbUrl() {
		return tbUrl;
	}

	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}

	@Override
	public String toString() {
		return "ImageResult [fullUrl=" + fullUrl + ", tbUrl=" + tbUrl + "]";
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray imageResult) {
		ArrayList<ImageResult> list = new ArrayList<ImageResult>();
		for (int i = 1; i < imageResult.length(); i++) {
			try {
				list.add(new ImageResult(imageResult.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
