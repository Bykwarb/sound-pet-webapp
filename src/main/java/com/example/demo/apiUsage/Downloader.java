package com.example.demo.apiUsage;

import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.apiUsage.ytDLP.YtDlp;
import com.example.demo.apiUsage.ytDLP.YtDlpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Service
@PropertySource("classpath:directory.properties")
public class Downloader {
    final private YoutubeApiParser apiParser;
    final private YtDlpRequest request;
    final private YtDlp dlp;
    @Value("${downloader.absolutePathToMusicStorage}")
    private String absolutePathToMusicStorage;

    private String updateTime;


    public Downloader(YoutubeApiParser apiParser, YtDlpRequest request, YtDlp dlp) {
        this.apiParser = apiParser;
        this.request = request;
        this.dlp = dlp;
    }
    //Execute yt-dlp utility for download from youtube url
    public void downloadFromYoutubePlaylist(String token, String playlistUrl) throws Exception {
        createFolders(token);
        String directory = System.getProperty("user.home");
        request.setUrl(playlistUrl);
        request.setDirectory(directory);
        request.setDownloadDirectory(token);
        dlp.executeFromYoutubePlaylist(request);
    }
    //Execute yt-dlp utility for download from spotify songlist
    public void downloadFromTxt(String token) throws Exception {
        String txtPath = createFoldersAndTxt(token);
        String directory = System.getProperty("user.home");
        request.setDirectory(directory);
        request.setFilePath(txtPath);
        request.setDownloadDirectory(token);
        dlp.executeFromFile(request);
        Files.delete(Path.of(txtPath));
    }
    //Execute yt-dlp utility for download from youtube url
    public void updateFromYoutube(String token, String playlistUrl) throws Exception {
        createUpdateFoldersYouTube(token);
        String directory = System.getProperty("user.home");
        request.setUrl(playlistUrl);
        request.setDirectory(directory);
        request.setDownloadDirectory(token + "/" + "update_" + updateTime);
        dlp.executeFromYoutubePlaylist(request);
    }

    //Execute yt-dlp utility for download from spotify songlist
    public void updateFromSpotify(String token) throws Exception{
        String directory = System.getProperty("user.home");
        String txtPath = createUpdateFoldersSpotify(token);
        request.setDirectory(directory);
        request.setFilePath(txtPath);
        request.setDownloadDirectory(token + "/" + "update_" + updateTime + "/");
        dlp.executeFromFile(request);
        Files.delete(Path.of(txtPath));
    }
    //Create an update directory in directory which corresponds to the room number,
    private void createUpdateFoldersYouTube(String token) throws IOException{
        updateTime = String.valueOf(System.currentTimeMillis());
        String dirPath = absolutePathToMusicStorage + token + "/update_" + updateTime;
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
    }//Create an update directory in directory which corresponds to the room number,
    // and create a txt file with youtube urls from youtubeApiParser. Return absolute path to txt file

    private String createUpdateFoldersSpotify(String token) throws IOException{
        updateTime = String.valueOf(System.currentTimeMillis());
        String dirPath = absolutePathToMusicStorage + token + "/update_" + updateTime;
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File url = new File(dir.getAbsolutePath() + "/" + token + ".txt");
        Path output = Paths.get(url.getAbsolutePath());
        Files.write(output, apiParser.getYoutubeUrls());
        return url.getAbsolutePath();
    }

    //Create a directory which corresponds to the room number, and create a txt file with youtube urls from youtubeApiParser. Return absolute path to txt file
    private String createFoldersAndTxt(String token) throws IOException {
        String dirPath = absolutePathToMusicStorage + token;
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        System.out.println(dir.getAbsoluteFile() + "/" + token + ".txt");
        File url  = new File(dir.getAbsoluteFile() + "/" + token + ".txt");
        System.out.println(apiParser.getYoutubeUrls());
        Path output = Paths.get(url.getAbsolutePath());
        Files.write(output, apiParser.getYoutubeUrls());
        return url.getAbsolutePath();
    }
    //Create a directory which corresponds to the room number
    private void createFolders(String token){
        String dirPath = absolutePathToMusicStorage + token;
        File dir = new File(dirPath);
        if (!dir.exists()){
            System.out.println("Folder created");
            dir.mkdirs();
        }
    }

    //Gets file names from the specified folder, sorts them by creation date and adds them to the list
    public List<String> extractFileNames(String pathToDirectory){
        File[] folder = new File(pathToDirectory).listFiles();
        List<String> fileList = new ArrayList<>();
        Arrays.sort(folder, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long l1 = getFileCreationEpoch(o1);
                long l2 = getFileCreationEpoch(o2);
                return Long.valueOf(l1).compareTo(l2);
            }
        });
        Arrays.asList(folder).forEach(e->{
            fileList.add(e.getAbsolutePath());
        });
        return fileList;
    }
    //Get files from the specified folder, sorts them by creation date and adds them to the list
    public List<File> extractFiles(String pathToDirectory){
        File[] folder = new File(pathToDirectory).listFiles();
        Arrays.sort(folder, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long l1 = getFileCreationEpoch(o1);
                long l2 = getFileCreationEpoch(o2);
                return Long.valueOf(l1).compareTo(l2);
            }
        });
        return Arrays.asList(folder);
    }
    //Gets a file creation date
    private static long getFileCreationEpoch (File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return attr.creationTime()
                    .toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
