package com.example.demo.apiUsage.ytDLP;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.stream.Collectors;
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

    public void executeFromYoutubePlaylist(YtDlpRequest request) throws Exception{
        String command = buildCommand( request.buildFromYoutubeUrl());
        execute(request, command);
    }

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
            System.out.println("im here");
        }catch (IOException e){
            e.printStackTrace();
        }

        InputStream inputStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }


}
