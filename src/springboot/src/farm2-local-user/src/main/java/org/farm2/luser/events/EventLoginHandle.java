package org.farm2.luser.events;

import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.domain.EventLog;
import org.farm2.auth.service.EventLogServiceInter;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.tools.base.FarmBase36Utils;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.files.Farm2FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
public class EventLoginHandle implements Farm2EventHandle {
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;

    @Override
    public void handle(F2Event event) {
        if (event.getAction().getType().equals(F2EActionT.LOGIN)) {
            //登录事件
            localUserServiceImpl.updateLoginTime(event.getAction().getOperator().getUserKey());
        }
    }
}
