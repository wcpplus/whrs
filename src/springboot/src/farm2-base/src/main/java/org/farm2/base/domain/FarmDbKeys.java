package org.farm2.base.domain;

import org.farm2.tools.base.FarmMd5Utils;
import org.farm2.tools.base.FarmStringUtils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

public class FarmDbKeys {

    public static String getMachineCode() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            List<String> macList = new ArrayList<>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    byte[] mac = networkInterface.getHardwareAddress();
                    if (mac != null) {
                        StringBuilder sb = new StringBuilder();
                        for (byte aMac : mac) {
                            sb.append(String.format("%02X", aMac));
                        }
                        macList.add(sb.toString());
                    }
                }
            }

            // 对收集到的MAC地址列表进行排序
            Collections.sort(macList);

            // 将排序后的MAC地址连接成一个字符串
            StringBuilder allMacs = new StringBuilder();
            for (String mac : macList) {
                allMacs.append(mac);
            }

            if (allMacs.length() > 0) {
                String md5 = FarmMd5Utils.generateMD5(allMacs.toString());
                List<String> keyList = FarmStringUtils.splitStringByLength(md5.substring(md5.length() - 8), 2);
                return keyList.stream().collect(Collectors.joining("-"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Machine Code: " + getMachineCode());
    }
}