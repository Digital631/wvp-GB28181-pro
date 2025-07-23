package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.gb28181.session.SseSessionManager;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * SSE 推送.
 *
 * @author lawrencehj
 * @author <a href="mailto:xiaoQQya@126.com">xiaoQQya</a>
 * @since 2021/01/20
 */
@Slf4j
@Tag(name = "SSE 推送")
@RestController
@RequestMapping("/api")
public class SseController {

    @Resource
    private SseSessionManager sseSessionManager;

    /**
     * SSE 推送.
     *
     * @param browserId 浏览器ID
     */
    @GetMapping("/emit")
    @ApiResponse(description = "SSE 推送,必须传入浏览器id")
    public SseEmitter emit(HttpServletResponse response, @RequestParam String browserId) throws IOException, InterruptedException {
//        response.setContentType("text/event-stream");
//        response.setCharacterEncoding("utf-8");
        return sseSessionManager.conect(browserId);
    }
    @GetMapping("/isStatus")
    @ApiResponse(description = "SSE 推送,必须传入浏览器id然后通过sse流的方式定向推送这个实时数据")
    public SseEmitter VieowisStatus(HttpServletResponse response, @RequestParam String browserId) throws IOException, InterruptedException {
//        response.setContentType("text/event-stream");
//        response.setCharacterEncoding("utf-8");
       log.info("browserId:{}",browserId);
       log.info("response:{}",response);
        return sseSessionManager.conect(browserId);
    }
}
