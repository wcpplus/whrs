package org.farm2.main;

import org.farm2.auth.events.EventLogHandle;
import org.farm2.auth.face.FarmParameter;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.base.web.Farm2BeanFactory;
import org.farm2.luser.events.EventLoginHandle;
import org.farm2.service.event.*;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.wdap.event.EventWdapHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitTask implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Farm2BeanFactory.setApplicationContext(applicationContext);
        //log.info(conf.toString());
        FarmParameter parameter = (FarmParameter) Farm2BeanFactory.getBean(FarmParameter.class);
        //将xml参数写入数据库
        parameter.initXmlToDatabase();
        //初始化配置参数中的附件默认地址
        parameter.initDefaultPaths();
        //注册事件处理
        {
            //记录日志
            Farm2Events.getHandles().add((Farm2EventHandle) Farm2BeanFactory.getBean(EventLogHandle.class));
            //记录登录时间|写入授权
            Farm2Events.getHandles().add((Farm2EventHandle) Farm2BeanFactory.getBean(EventLoginHandle.class));
            //生成预览文件
            Farm2Events.getHandles().add((Farm2EventHandle) Farm2BeanFactory.getBean(EventWdapHandle.class));
            //缓存刷新事件
            Farm2Events.getHandles().add((Farm2EventHandle) Farm2BeanFactory.getBean(EventCacheClearHandle.class));
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("----------------------STARTED----------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println("SKC "+Farm2ConfigUtils.getInstance(Farm2ConfigEnum.INFO_PROPERTIES).getData("farm2.conf.server.version"));


    }
}
