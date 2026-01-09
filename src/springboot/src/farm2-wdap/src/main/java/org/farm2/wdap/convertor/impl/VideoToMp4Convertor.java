package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.impl.VideoMp4FileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import ws.schild.jave.*;
import ws.schild.jave.encode.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoToMp4Convertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new VideoToMp4Convertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        paras.add(new ConvertorParam("audio-codec", "音频编解码器", "可以选择音频编解码器，例如AAC", "AAC"));
        paras.add(new ConvertorParam("video-codec", "视频编解码器", "设置视频编解码器为libx264（即H.264）", "libx264"));
        paras.add(new ConvertorParam("video-bit-rate", "视频比特率", "设置比特率，这里是0.5 Mbps", "500000"));
        paras.add(new ConvertorParam("video-frame-rate", "帧率", "设置帧率，这里是30 fps", "30"));
        return paras;
    }


    @Override
    public String getTitle() {
        return "视频文件转换为Mp4";
    }

    @Override
    public String getNote() {
        return "视频文件转换为Mp4";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        VideoMp4FileMode mp4FileMode = new VideoMp4FileMode();
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        //----------------------------------------------------
        File mp4File = mp4FileMode.getFile(resourcefile, resourceFileServiceImpl);
        if (!file.exists()) {
            throw new RuntimeException("输入文件不存在: " + file);
        }

        // 创建多媒体对象
        MultimediaObject multimediaObject = new MultimediaObject(file);

        // 设置音频属性（可选）
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec(params.get("audio-codec").getValue()); // 可以选择音频编解码器，例如AAC

        // 设置视频属性
        VideoAttributes video = new VideoAttributes();
        video.setCodec(params.get("video-codec").getValue()); // 设置视频编解码器为libx264（即H.264）
        video.setBitRate(Integer.valueOf(params.get("video-bit-rate").getValue())); // 设置比特率，这里是2.5 Mbps
        video.setFrameRate(Integer.valueOf(params.get("video-frame-rate").getValue())); // 设置帧率，这里是30 fps

        // 创建编码属性并设置音视频属性
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4"); // 使用 setOutputFormat 方法设置输出格式
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        // 创建编码器并执行转换
        Encoder encoder = new Encoder();
        try {
            encoder.encode(multimediaObject, mp4File, attrs);
            System.out.println("转换完成！");
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        //-----------------------------------------------------
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(mp4FileMode.getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        List<FileModel> list = new ArrayList<>();
        list.add(new ResourceFileMode());
        return list;
    }

    @Override
    public FileModel getTargetFileModel() {
        return new VideoMp4FileMode();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
    }
}
