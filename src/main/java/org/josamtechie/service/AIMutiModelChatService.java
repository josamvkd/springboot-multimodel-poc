package org.josamtechie.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIMutiModelChatService
{

    private final ChatClient openAIChatClient;
    private final ChatClient ollamaChatClient;

    public AIMutiModelChatService(ChatClient openAIChatClient, ChatClient ollamaChatClient)
    {
        this.openAIChatClient = openAIChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    public String chatWithOpenAI(String message)
    {
        return openAIChatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String chatWithOllama(String message)
    {
        return ollamaChatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
