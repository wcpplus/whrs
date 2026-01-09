package org.farm2.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.farm2.tools.json.FarmJsons;
import org.farm2.tools.time.FarmTimeTool;

import java.io.IOException;
import java.util.Map;

public class FarmWucEventUtils {
    private static String sysid = "2c9580909b2aba56019b886adf2a6019";
    private static String modelkey = "BIT";
    private static String operatorLoginname = "skcapi";
    private static String operatorPassword = "skcapi";
    private static String secret = "F59BD65F7EDAFB087A81D4DCA06C4910";

    public static void main(String[] args) {
        bit("SKC知识库", "SKC智能知识库", 16, "0.0.0");
    }

    public static void bit(String title, String tootTitle, long usernum, String version) {
        try {
            runEvent(bitEvent(title, tootTitle, usernum, version));
        } catch (Exception e) {
            //错误
            // e.printStackTrace();
        }
    }

    public static void runEvent(String eventId) {
// 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();
        // 构造 form 表单参数
        RequestBody formBody = new FormBody.Builder()
                .add("eventid", eventId)
                .add("operatorLoginname", operatorLoginname)
                .add("operatorPassword", operatorPassword)
                .add("secret", secret)
                .build();

        // 构建请求对象
        Request request = new Request.Builder()
                .url("http://wlp.demo.wcpknow.com/wuc/api/event/over.do")  // 替换为目标登录地址
                .post(formBody)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                //成功
            } else {
                throw new RuntimeException(response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String bitEvent(String title, String footTitle, long usernum, String version) {
// 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 构造 form 表单参数
        RequestBody formBody = new FormBody.Builder()
                .add("sysid", sysid)
                .add("ctime14", FarmTimeTool.getTimeDate14())
                .add("objid", title)
                .add("objname", title)
                .add("modelkey", modelkey)
                .add("title", title)
                .add("operatorLoginname", operatorLoginname)
                .add("operatorPassword", operatorPassword)
                .add("secret", secret)//
                //
                .add("text1", footTitle)
                .add("texttitle1", "footTitle")
                .add("text2", version)
                .add("texttitle2", "version")
                .add("num1", String.valueOf(usernum))
                .add("numtitle1", "Unum")
                //
                .build();

        // 构建请求对象
        Request request = new Request.Builder()
                .url("http://wlp.demo.wcpknow.com/wuc/api/event/on.do")  // 替换为目标登录地址
                .post(formBody)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                Map<String, Object> objMap = FarmJsons.jsonToMap(response.body().string());
                return objMap.get("EVENTID").toString();
            } else {
                throw new RuntimeException(response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
