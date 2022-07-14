package com.example.demo.apiUsage.ytDLP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.stream.Collectors;
//That class execute cmd utility for download video from youtube
@Component
@PropertySource("classpath:directory.properties")
public class YtDlp  {
    @Value("${yt-dlp.executablePath}")
    private String propertiesPath;

    @Value("${yt-dlp.executablePath}")
    public void setExecutablePath(String propertiesPath){
        YtDlp.EXECUTABLE_PATH = propertiesPath;
    }

    private static String EXECUTABLE_PATH;

    protected  String buildCommand(String command){
        return String.format("%s %s", EXECUTABLE_PATH, command);
    }
    //Use yt-dlp utility for download youtube videos from youtube url
    public void executeFromYoutubePlaylist(YtDlpRequest request) throws Exception{
        String command = buildCommand( request.buildFromYoutubeUrl());
        execute(request, command);
    }

    //Use yt-dlp utility for download youtube videos from youtube url in the txt file
    public  void executeFromFile(YtDlpRequest request) throws Exception {
        String command = buildCommand(request.buildFromFile());
        execute(request, command);
    }

    private void execute(YtDlpRequest request, String command) {
        String directory = request.getDirectory();
        Process process = null;
        String[] split = command.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(split);
        if(directory != null){
            processBuilder.directory(new File(directory));
        }
        try {
            process = processBuilder.start();
        }catch (IOException e){
            e.printStackTrace();
        }

        InputStream inputStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }


}
