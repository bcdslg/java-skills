package com.example.skills.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 * AI 聊天服务类
 * 使用 Spring Alibaba AI 调用本地 Claude Skills
 */
@Service
public class AiChatService {

    private final ChatClient chatClient;
    private final ToolCallbackProvider toolCallbackProvider;

    public AiChatService(ChatModel chatModel, LocalClaudeSkills localClaudeSkills) {
        // 创建工具回调提供者，将 @Tool 注解的方法注册为可用工具
        this.toolCallbackProvider = MethodToolCallbackProvider.builder()
                .toolObjects(localClaudeSkills)
                .build();

        // 构建带工具功能的 ChatClient
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(toolCallbackProvider)
                .defaultSystem("你是一个有帮助的助手，可以调用各种技能来帮助用户完成任务。")
                .build();
    }

    /**
     * 与 AI 对话并自动调用合适的技能
     * @param prompt 用户输入
     * @return AI 响应
     */
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    /**
     * 执行特定技能（直接调用）
     * @param skillName 技能名称
     * @param params 参数
     * @return 执行结果
     */
    public String executeSkill(String skillName, Object... params) {
        return "技能执行请求已接收：" + skillName;
    }
}
