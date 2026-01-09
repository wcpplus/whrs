package org.farm2.wdap.event;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.wdap.convertor.utils.WdapFlowRunner;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.farm2.wdap.utils.WdapTaskStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventWdapHandle implements Farm2EventHandle {
    @Autowired
    private WdapTaskServiceInter wdapTaskServiceImpl;
    @Autowired
    private WdapFlowRunner wdapFlowRunner;


    /**
     * 生成预览文件
     *
     * @param event
     */
    @Override
    public void handle(F2Event event) {
        if (event.getAction().getType().equals(F2EActionT.SUBMIT_FILE)) {
            WdapTask task = wdapTaskServiceImpl.addTask(event.getObj().getId());
            if (task != null) {
                FarmProcessUtils.setProcess(event.getObj().getId(), FarmProcessTypeEnum.CONVERT_EXFILE, WdapTaskStateEnum.getDic().get(task.getPstate()));
            }
            wdapFlowRunner.startRunTasks();
        }
        if (event.getAction().getType().equals(F2EActionT.CANCEL_FILE)) {
            wdapTaskServiceImpl.delTaskByFileid(event.getObj().getId());
        }
    }
}
