package com.example.demo.apiUsage.ytDLP;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@PropertySource("classpath:directory.properties")
public class YtDlpRequest {
    private String directory;
    private String url;
    private Map<String, String> options = new HashMap<>();
    private String filePath;
    private String downloadDirectory;

    @Value("${yt-dlp-request.musicStoragePath}")
    private String musicStorageDirectory;

    public YtDlpRequest(){}

    public YtDlpRequest(String directory){
        this.directory = directory;
    }

    public YtDlpRequest(String url, String directory){
        this.url = url;
        this.directory = directory;
    }
    //Build command with youtube url for yt-dlp
    protected String buildFromYoutubeUrl(){
        System.out.println(musicStorageDirectory);
        StringBuilder builder = new StringBuilder();
        System.out.println(downloadDirectory);
        //Sets the video to be loaded in the format WEBM without video content, only audio
        String par = "-o " + musicStorageDirectory + downloadDirectory + "/%(title)s.webm -f bestaudio";
        builder.append(par).append(" ");
        builder.append(url).append(" ");
        return builder.toString().trim();
    }
    //Build command with youtube url from file for yt-dlp
    protected String buildFromFile(){
        StringBuilder builder = new StringBuilder();
        System.out.println(downloadDirectory);
        String par = "-o " + musicStorageDirectory + downloadDirectory + "/%(title)s.webm ";
        //Sets the video to be loaded in the format WEBM without video content, only audio
        String optionFormatted = String.format("-f bestaudio -a %s", filePath).trim();
        System.out.println(optionFormatted);
        builder.append(par).append(" ");
        builder.append(optionFormatted).append(" ");
        System.out.println(builder);
        return builder.toString().trim();
    }

    protected String buildOptions(){
        StringBuilder builder = new StringBuilder();
        if(url != null){
            builder.append(url + " ");
        }
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry option = (Map.Entry) it.next();
            String name = (String) option.getKey();
            String value = (String) option.getValue();
            if(value == null) {
                value = "";
            }
            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted).append(" ");
            it.remove();
        }
        return builder.toString().trim();
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getOptions() {
        return options;
    }


    public void setOption(String key) {
        options.put(key, null);
    }

    public void setOption(String key, String value) {
        options.put(key, value);
    }

    public void setOption(String key, int value) {
        options.put(key, String.valueOf(value));
    }

    public void setMusicStorageDirectory(String musicStorageDirectory) {
        this.musicStorageDirectory = musicStorageDirectory;
    }
}
