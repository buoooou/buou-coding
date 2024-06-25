package com.github.buoooou.service.chatai.service.impl.silicon;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * chatgpt的交互封装集成
 *
 * @author YiHui
 * @date 2023/4/20
 */
@Slf4j
@Service
public class SiliconCloudIntegration {

    @Autowired
    @Getter
    private SiliconConfig config;

    @Getter
    private OkHttpClient okHttpClient;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "silicon")
    public static class SiliconConfig {
        private List<String> keys;

        public String hostUrl = "https://api.siliconflow.cn/v1/chat/completions";
        public String model = "";

        public String fetchKey() {
            int index = RandomUtil.randomInt(keys.size());
            return keys.get(index);
        }
    }

    @Data
    public static class Message {
        public String role;
        public String content;
    }


    @Data
    public static class ResponseData {
        public String id;
        public String object;
        public Integer created;
        public String model;
        private List<SiliconCloudIntegration.Choice> choices;
        private SiliconCloudIntegration.Usage usage;
        public String system_fingerprint;
    }

    @Data
    public static class Choice {
        public Integer index;
        public Message message;
        public String finish_reason;

    }

    @Data
    public static class Usage {
        public Integer prompt_tokens;
        public Integer completion_tokens;
        public Integer total_tokens;
    }


    public List<Message> getMessage(String msg) {
        SiliconCloudIntegration.Message message = new Message();
        message.setRole("user");
        message.setContent(msg);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        return messages;
    }


    @PostConstruct
    public void init() {
        okHttpClient = new OkHttpClient.Builder().build();
    }


}
