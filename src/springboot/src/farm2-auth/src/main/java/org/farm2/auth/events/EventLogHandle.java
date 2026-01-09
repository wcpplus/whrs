package org.farm2.auth.events;

import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.domain.EventLog;
import org.farm2.auth.service.EventLogServiceInter;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.tools.base.FarmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventLogHandle implements Farm2EventHandle {
    @Autowired
    private EventLogServiceInter eventLogServiceImpl;
    @Override
    public void handle(F2Event event) {
        EventLog log = new EventLog();
        log.setObjtype(event.getObj().getType().name());
        log.setActiontype(event.getAction().getType().name());
        log.setObjid(event.getObj().getId());
        log.setUserip(event.getAction().getOperator().getIp());
        log.setUserkey(event.getActionUserKey());
        String parasInfo = Arrays.stream(event.getAction().getVals()).map(node -> node + "").collect(Collectors.joining(","));
        log.setNote(FarmStringUtils.subString((event.getNote() != null ? event.getNote() : "") + parasInfo, 200));
        log.setOuserkey(event.getObjUserKey());
        eventLogServiceImpl.insertEventLogEntity(log);
    }
}
