package com.example.demo.apiUsage.youtube.json;

import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Component;

import java.util.List;


public class YoutubeResponseJson {
   @SerializedName("kind")
   public String kind;
   @SerializedName("etag")
   public String etag;
   @SerializedName("nextPageToken")
   public String nextPageToken;
   @SerializedName("regionCode")
   public String regionCode;
   @SerializedName("pageInfo")
   public PageInfo pageInfo;
   @SerializedName("items")
   public List<Items> items;


}
