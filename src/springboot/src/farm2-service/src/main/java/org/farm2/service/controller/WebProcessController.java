package org.farm2.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2ProcessState;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 事务进度获取
 *
 * @author cbtg自动生成  2025-2-4 12:13:51
 */
@RestController
@RequestMapping("/api/process")
@Slf4j
public class WebProcessController {
    @Autowired
    private WdapExtendFileServiceInter wdapExtendFileServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private WdapTaskServiceInter wdapTaskServiceImpl;
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam String key, @RequestParam String type) {
        //超时时间为10分钟
        SseEmitter emitter = new SseEmitter(60000L * 10);
        emitter.onError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //错误
                throwable.printStackTrace();
            }
        });
        // 模拟定时发送数据
        executorService.submit(() -> {
            Farm2ProcessState state = FarmProcessUtils.getProcess(key, FarmProcessTypeEnum.valueOf(type));
            int backNum = 0;
            while ((state != null && !state.isEnd()) || (backNum <= 0)) {
                backNum = backNum + 1;
                Farm2ProcessState.sleep(500);
                state = FarmProcessUtils.getProcess(key, FarmProcessTypeEnum.valueOf(type));
                try {
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data(state));
                } catch (IOException e) {
                    state = null;
                    throw new RuntimeException(e);
                }
            }
            emitter.complete();
        });
        return emitter;
    }

}
