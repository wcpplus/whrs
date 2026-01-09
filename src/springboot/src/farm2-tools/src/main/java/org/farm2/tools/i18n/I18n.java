package org.farm2.tools.i18n;

import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.db.commons.FarmSqls;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18n {
    public static String msg(String template, Object... paras) {
        if (template == null) {
            return null;
        }
        template = I18nConfigReader.getMsg(template);
        template=template.replace("？","?");
        if (paras != null) {
        for (Object para : paras) {
            if (para == null) {
                para = "NONE";
            }
            template = template.replaceFirst("\\?", para.toString());
        }
        }
        return template.trim();
    }

    /**
     * 从表达式中解析消息 farm2.validate.template.size[{min},{max}]
     * farm2.validate.template.size是entry的key
     * [para1,para2]为两个参数
     * 例如：校驗ValidationMessages.properties配置文件中的farm2.validate.template.size[2,8]
     *
     * @return
     */
    @SuppressWarnings("varargs")
    public static String msgByExpression(String message) {
        if (StringUtils.isBlank(message)) {
            return null;
        }
        String msg = null;
        String[] paras = null;
        if (message.indexOf(":") > 0) {
            msg = message.split(":")[0];
            String inparas = message.split(":")[1];
            paras = inparas.split(",");
        } else {
            msg = message;
        }
        msg = I18nConfigReader.getMsg(msg);
        msg = msg.replace("？", "?");
        if (paras != null) {
            for (Object para : paras) {
                if (para == null) {
                    para = "NONE";
                }
                msg = msg.replaceFirst("\\?", para.toString());
            }
        }
        return msg.trim();
    }
}
