package com.genersoft.iot.vmp.gb28181.session;

import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.conf.DynamicTask;
import com.genersoft.iot.vmp.media.service.IMediaServerService;
import com.genersoft.iot.vmp.streamProxy.dto.StreamProxyDisplayDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseSessionManager {

    private static final Map<String, SseEmitter> sseSessionMap = new ConcurrentHashMap<>();

    @Autowired
    private DynamicTask dynamicTask;
    @Autowired
    private IMediaServerService mediaServerService;

    /*
     * pulling: true: 正在拉流, false: 停用
     * enable: true: 开启, false: 停用
     * @author MysticShadow
     * @date 2025/7/23 17:15
     * @param browserId
     * @return org.springframework.web.servlet.mvc.method.annotation.SseEmitter
     */
    public SseEmitter conect(String browserId){
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err)-> {
            log.error("[SSE推送] 连接错误, 浏览器 ID: {}, {}", browserId, err.getMessage());
            sseSessionMap.remove(browserId);
            sseEmitter.completeWithError(err);
        });

//        sseEmitter.onTimeout(() -> {
//            log.info("[SSE推送] 连接超时, 浏览器 ID: {}", browserId);
//            sseSessionMap.remove(browserId);
//            sseEmitter.complete();
//            dynamicTask.stop(key);
//        });

        sseEmitter.onCompletion(() -> {
            log.info("[SSE推送] 连接结束, 浏览器 ID: {}", browserId);
            sseSessionMap.remove(browserId);
        });
        sseSessionMap.put(browserId, sseEmitter);

        log.info("[SSE推送] 连接已建立, 浏览器 ID: {}, 当前在线数: {}", browserId, sseSessionMap.size());
        return sseEmitter;
    }

    @Scheduled(fixedRate = 5000)   //每1秒执行一次
    public void execute(){
        if (sseSessionMap.isEmpty()){
            return;
        }
        List<Map<String, Object>> all = mediaServerService.getStreamInfoByAppAndStreamWithCheckAll("zlmediakit-local");
        sendForAll("keepalive",all);
        log.info("[SSE推送] 浏览器已连接,正在推送数据中: {}", all);
        //sendForAll("keepalive", "alive");
    }


    public void sendForAll(String event, Object data) {
        for (String browserId : sseSessionMap.keySet()) {
            SseEmitter sseEmitter = sseSessionMap.get(browserId);
            if (sseEmitter == null) {
                continue;
            };
            try {
                sseEmitter.send(SseEmitter.event().name(event).data(data));
            } catch (Exception e) {
                log.error("[SSE推送] 发送失败: {}", e.getMessage());
                sseSessionMap.remove(browserId);
                sseEmitter.completeWithError(e);
            }
        }
    }
}
