package com.chatgptdemo.chat.controller;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

@RestController
public class ChatController {

    @PostMapping("/chat")
    public void chat(@RequestBody String content) {
        System.out.println("hello!");
        OpenAiStreamClient client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList(""))
                .keyStrategy(new KeyRandomStrategy())
                .apiHost("https://proxy.cocopilot.org/v1/chat/completions/")
                .build();

        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content(content).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}