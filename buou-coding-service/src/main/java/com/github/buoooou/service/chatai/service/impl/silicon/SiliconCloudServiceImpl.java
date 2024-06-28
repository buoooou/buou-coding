package com.github.buoooou.service.chatai.service.impl.silicon;

import com.github.buoooou.api.model.enums.ChatAnswerTypeEnum;
import com.github.buoooou.api.model.enums.ai.AISourceEnum;
import com.github.buoooou.api.model.enums.ai.AiChatStatEnum;
import com.github.buoooou.api.model.vo.chat.ChatItemVo;
import com.github.buoooou.api.model.vo.chat.ChatRecordsVo;
import com.github.buoooou.core.util.JsonUtil;
import com.github.buoooou.service.chatai.service.AbsChatService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class SiliconCloudServiceImpl extends AbsChatService {
    @Autowired
    private SiliconCloudIntegration siliconCloudIntegration;
    private OkHttpClient client;

    @Override
    public AiChatStatEnum doAnswer(Long user, ChatItemVo chat) {
        client = siliconCloudIntegration.getOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, buildBody(chat.getQuestion()));
        Request request = new Request.Builder()
                .url(siliconCloudIntegration.getConfig().getHostUrl())
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + siliconCloudIntegration.getConfig().fetchKey())
                .build();

        try {
            Response rep = client.newCall(request).execute();

            ResponseBody responseBody = rep.body();

            SiliconCloudIntegration.ResponseData responseData =
                    JsonUtil.toObj(responseBody.string(), SiliconCloudIntegration.ResponseData.class);

            chat.appendAnswer(responseData.getChoices().get(0).getMessage().getContent());
            if (log.isDebugEnabled()) {
                log.debug("siliconCloud返回内容: {}", responseData);
            }
        } catch (IOException e) {
            log.warn("silicon 连接失败! {}", chat, e);
            // 返回异常的场景
            chat.appendAnswer("Error:" + e)
                    .setAnswerType(ChatAnswerTypeEnum.STREAM_END);
        }
        return AiChatStatEnum.END;

    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Long user, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        return AiChatStatEnum.IGNORE;
    }


    private String buildBody(String question) {
        HashMap map = new HashMap();
        map.put("model", siliconCloudIntegration.getConfig().getModel());
        map.put("messages", siliconCloudIntegration.getMessage(question));
        return JsonUtil.toStr(map);
    }

    @Override
    public AISourceEnum source() {
        return AISourceEnum.SILICON_CLOUD;
    }
}
