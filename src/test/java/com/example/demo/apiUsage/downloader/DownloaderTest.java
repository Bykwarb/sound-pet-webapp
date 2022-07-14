package com.example.demo.apiUsage.downloader;

import com.example.demo.apiUsage.Downloader;
import com.example.demo.apiUsage.youtube.YoutubeApiParser;
import com.example.demo.apiUsage.ytDLP.YtDlp;
import com.example.demo.apiUsage.ytDLP.YtDlpRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
class DownloaderTest {

    @Autowired
    private YtDlp ytDlp;
    @Autowired
    private YtDlpRequest request;
    @Autowired
    private YoutubeApiParser apiParser;
    @Value("${downloader.absolutePathToMusicStorage.test}")
    private String absolutePathToMusicStorage;
    @Value("${yt-dlp.executablePath.test}")
    private String propertiesPath;
    @Value("${yt-dlp-request.musicStoragePath.test}")
    private String musicStorageDirectory;

    private final List<String> urls = List.of(
            "https://www.youtube.com/watch?v=HdWw9SksiwQ",
            "https://www.youtube.com/watch?v=fWUxkZEuJG4",
            "https://www.youtube.com/watch?v=IiZ7tpjac-g");

    @Test
    void downloadFromYoutubePlaylist() throws Exception {
        int filesQuantity = 11;
        String folderName = "downloadFromYoutubePlaylistTest";
        File file = new File(absolutePathToMusicStorage + folderName);
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + folderName));
        }
        Downloader downloader = new Downloader(Mockito.mock(YoutubeApiParser.class), request, ytDlp);
        ytDlp.setExecutablePath(propertiesPath);
        request.setMusicStorageDirectory(musicStorageDirectory);
        downloader.setAbsolutePathToMusicStorage(absolutePathToMusicStorage);
        downloader.downloadFromYoutubePlaylist(folderName, "https://www.youtube.com/playlist?list=OLAK5uy_n3lOLF85jSMEMjDLqYAoiISkQCiv23H1w");
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName).size());
    }

    @Test
    void downloadFromTxt() throws Exception {
        int filesQuantity = 3;
        String folderName = "downloadFromTxt";
        File file = new File(absolutePathToMusicStorage + folderName);
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + folderName));
        }
        apiParser.setYoutubeUrls(urls);
        ytDlp.setExecutablePath(propertiesPath);
        request.setMusicStorageDirectory(musicStorageDirectory);
        Downloader downloader = new Downloader(apiParser, request, ytDlp);
        downloader.setAbsolutePathToMusicStorage(absolutePathToMusicStorage);
        downloader.downloadFromTxt(folderName);
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName).size());
        Assert.assertEquals("webm", FilenameUtils.getExtension(downloader.extractFiles(absolutePathToMusicStorage + folderName).get(0).getName()));
    }

    @Test
    void updateFromYoutube() throws Exception {
        int filesQuantity = 1;
        String folderName = "downloadFromYoutubePlaylistTest";
        File file = new File(absolutePathToMusicStorage + folderName);
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + folderName));
        }
        ytDlp.setExecutablePath(propertiesPath);
        request.setMusicStorageDirectory(musicStorageDirectory);
        Downloader downloader = new Downloader(Mockito.mock(apiParser.getClass()), request, ytDlp);
        downloader.setAbsolutePathToMusicStorage(absolutePathToMusicStorage);
        downloader.updateFromYoutube(folderName, "https://www.youtube.com/watch?v=EJeEvRv1xjI");
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName + "/update_" + downloader.getUpdateTime()).size());
        Assert.assertEquals("webm", FilenameUtils.getExtension(downloader.extractFiles(absolutePathToMusicStorage + folderName + "/update_" + downloader.getUpdateTime()).get(0).getName()));
    }

    @Test
    void updateFromSpotify() throws Exception {
        int filesQuantity = 3;
        String folderName = "downloadFromTxt";
        File file = new File(absolutePathToMusicStorage + folderName);
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + folderName));
        }
        apiParser.setYoutubeUrls(urls);
        ytDlp.setExecutablePath(propertiesPath);
        request.setMusicStorageDirectory(musicStorageDirectory);
        Downloader downloader = new Downloader(apiParser, request, ytDlp);
        downloader.setAbsolutePathToMusicStorage(absolutePathToMusicStorage);
        downloader.updateFromSpotify(folderName);
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName + "/update_" + downloader.getUpdateTime()).size());
        Assert.assertEquals("webm", FilenameUtils.getExtension(downloader.extractFiles(absolutePathToMusicStorage + folderName + "/update_" + downloader.getUpdateTime()).get(0).getName()));
    }

    @Test
    void extractFileNames() {
        int filesQuantity = 3;
        String folderName = "folderTest";
        Downloader downloader = new Downloader(Mockito.mock(apiParser.getClass()), Mockito.mock(request.getClass()), Mockito.mock(ytDlp.getClass()));
        downloader.extractFileNames(absolutePathToMusicStorage + folderName);
        Assert.assertEquals(filesQuantity, downloader.extractFileNames(absolutePathToMusicStorage + folderName).size());
        List<String> fileNames = List.of(
                (absolutePathToMusicStorage + folderName + "/txt1.txt").replace("/", "\\"),
                (absolutePathToMusicStorage + folderName + "/txt2.txt").replace("/", "\\"),
                (absolutePathToMusicStorage + folderName + "/txt3.txt").replace("/", "\\"));
        Assert.assertEquals(fileNames.toString(), downloader.extractFileNames(absolutePathToMusicStorage + folderName).toString());
    }

    @Test
    void extractFiles() {
        int filesQuantity = 3;
        String folderName = "folderTest";
        Downloader downloader = new Downloader(Mockito.mock(apiParser.getClass()), Mockito.mock(request.getClass()), Mockito.mock(ytDlp.getClass()));
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName).size());
        Assert.assertEquals("txt", FilenameUtils.getExtension(downloader.extractFiles(absolutePathToMusicStorage + folderName).get(0).getName()));
    }

    @Test
    void downloadWithoutUrl() throws Exception {
        int filesQuantity = 0;
        String folderName = "downloadFromYoutubePlaylistTest";
        File file = new File(absolutePathToMusicStorage + folderName);
        if (file.exists()){
            FileUtils.cleanDirectory(new File(absolutePathToMusicStorage + folderName));
        }
        Downloader downloader = new Downloader(Mockito.mock(YoutubeApiParser.class), request, ytDlp);
        ytDlp.setExecutablePath(propertiesPath);
        request.setMusicStorageDirectory(musicStorageDirectory);
        downloader.setAbsolutePathToMusicStorage(absolutePathToMusicStorage);
        downloader.downloadFromYoutubePlaylist(folderName, "");
        Assert.assertEquals(filesQuantity, downloader.extractFiles(absolutePathToMusicStorage + folderName).size());
    }
}