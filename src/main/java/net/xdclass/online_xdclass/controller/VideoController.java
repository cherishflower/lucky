package net.xdclass.online_xdclass.controller;

import net.xdclass.online_xdclass.model.entity.Video;
import net.xdclass.online_xdclass.model.entity.VideoBanner;
import net.xdclass.online_xdclass.service.VideoService;
import net.xdclass.online_xdclass.utils.JsonData;
import net.xdclass.online_xdclass.utils.NonStaticResourceHttpRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/v1/pub/video")
public class VideoController {
    @Autowired
    private  NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Autowired
    private VideoService videoService;


    /**
     * 轮播图列表
     * @return
     */
    @GetMapping("list_banner")
    public JsonData indexBanner(){


        List<VideoBanner> bannerList =  videoService.listBanner();



        return JsonData.buildSuccess(bannerList);

    }


    /**
     * 视频列表
     * @return
     */
    @RequestMapping("list")
    public JsonData listVideo(){

        List<Video> videoList = videoService.listVideo();
        return JsonData.buildSuccess(videoList);
    }


    /**
     * 查询视频详情，包含章，集信息
     * @param videoId
     * @return
     */
    @GetMapping("find_detail_by_id")
    public JsonData findDetailById(@RequestParam(value = "video_id",required = true)int videoId){




        Video video = videoService.findDetailById(videoId);

        return JsonData.buildSuccess(video);

    }


    /**
     * 获取单集视频
     * @param videoId
     * @return
     */
//    @GetMapping("find_episode_detail_by_id")
//    public JsonData findEpisodeDetailById(@RequestParam(value = "video_id",required = true)int videoId,@RequestParam(value = "chapter_id",required = true)int chapterId,@RequestParam(value = "episode_id",required = true)int episodeId){
//
//        Video video = videoService.findEpisodeDetailById(videoId,chapterId,episodeId);
//
//        return JsonData.buildSuccess(video);
//
//    }
    /**
     * 获取单集视频
     * @param request
     * @return
     */


    @GetMapping("find_episode_detail_by_id/{video_id}/{chapter_id}/{episode_id}")
    public void videoPreview(@PathVariable String video_id,@PathVariable String chapter_id,@PathVariable String episode_id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //假如我把视频1.mp4放在了static下的video文件夹里面
        //sourcePath 是获取resources文件夹的绝对地址
        //realPath 即是视频所在的磁盘地址
        String sourcePath = "D:/cherish_学习资料/14.nodejs（7天）/视频/videos/";
        String realPath = sourcePath +"00-为什么要学习Node.mp4";


        Path filePath = Paths.get(realPath );
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }




}
