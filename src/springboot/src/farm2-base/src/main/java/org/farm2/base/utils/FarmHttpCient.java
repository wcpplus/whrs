package org.farm2.base.utils;

import okhttp3.*;
import org.farm2.tools.time.FarmTimeTool;

import java.io.IOException;

public class FarmHttpCient {
    public static void main(String[] args) {
        // 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 构造 form 表单参数
        RequestBody formBody = new FormBody.Builder()
                .add("sysid", "2c958090949108560196801c8fe23783")
                .add("ctime14", FarmTimeTool.getTimeDate14())
                .add("objid", "OBJID")
                .add("objname", "OBJNAME")
                .add("modelkey", "BIT")
                .add("title", "title")
                .add("operatorLoginname", "skcapi")
                .add("operatorPassword", "skcapi")
                .add("secret", "F59BD65F7EDAFB087A81D4DCA06C4910")
                .build();

        // 构建请求对象
        Request request = new Request.Builder()
                .url("http://wlp.demo.wcpknow.com/wuc/api/event/on.do")  // 替换为目标登录地址
                .post(formBody)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
            } else {
                System.out.println("Request failed: " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
