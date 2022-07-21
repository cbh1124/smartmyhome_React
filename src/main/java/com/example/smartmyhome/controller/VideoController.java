package com.example.smartmyhome.controller;

import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.service.VideoService;
import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.PipeOutput;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/video")
@Log4j2
@Slf4j
public class VideoController {

    @Autowired
    VideoService videoService;

    //    @PathVariable("id") Long tipperId
    // CCTV 0번 설정을 통해서
    @GetMapping(value = "/live.mp4/{rsname}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> livestream(@PathVariable(required = false) String rsname) throws Exception {
        // 특정 번호를 입력 받으면 DB에서 CCTV를 서칭한다.
        Optional<RoomEntity> entity = videoService.findsensorname(rsname);
        String rtspUrl = entity.get().getRsaddress();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(os -> {
                    FFmpeg.atPath()
                            .addArgument("-re")
                            .addArguments("-acodec", "pcm_s16le")
                            .addArguments("-rtsp_transport", "tcp")
                            .addArguments("-i", rtspUrl)
                            .addArguments("-vcodec", "copy")
                            .addArguments("-af", "asetrate=22050")
                            .addArguments("-acodec", "aac")
                            .addArguments("-b:a", "96k" )
                            .addOutput(PipeOutput.pumpTo(os)
                                    .disableStream(StreamType.AUDIO)
                                    .disableStream(StreamType.SUBTITLE)
                                    .disableStream(StreamType.DATA)
//                                    .setFrameCount(StreamType.VIDEO, 1000L) // 용량 제한
                                    //1 frame every 10 seconds
                                    .setFrameRate(1)
                                    // 시간 제한
                                    .setDuration(10, TimeUnit.DAYS)
                                    .setFormat("ismv"))
                            .addArgument("-nostdin")
                            .execute();
                });
    }

    @GetMapping("/cctvcheck")
    public ResponseEntity<?> cctvcheck(){
        return ResponseEntity.ok().body(videoService.findcount("CCTV"));
    }
}
