package org.josamtechie.controller;

import org.josamtechie.service.AIMutiModelChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multi-model/chat")
public class AIMutiModelChatController
{
    @Autowired
    private AIMutiModelChatService service;

    @GetMapping("/openai")
    public String chatWithOpenAILLM(@RequestParam String message)
    {
        return service.chatWithOpenAI(message);
    }

    @GetMapping("/ollama")
    public String chatWithOllamaLLM(@RequestParam String message)
    {
        return service.chatWithOllama(message);
    }
}
