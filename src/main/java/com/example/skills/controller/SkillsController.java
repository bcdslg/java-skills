package com.example.skills.controller;

import com.example.skills.client.AiChatService;
import org.springframework.web.bind.annotation.*;

/**
 * REST 控制器
 * 提供 HTTP 接口调用 AI Skills
 */
@RestController
@RequestMapping("/api/skills")
public class SkillsController {

    private final AiChatService aiChatService;

    public SkillsController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * 聊天接口 - AI 会自动选择合适的技能执行
     * @param message 用户消息
     * @return AI 响应
     */
    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiChatService.chat(message);
    }

    /**
     * 直接执行技能
     * @param skillName 技能名称
     * @param params 参数（可选）
     * @return 执行结果
     */
    @PostMapping("/execute/{skillName}")
    public String executeSkill(
            @PathVariable String skillName,
            @RequestBody(required = false) Object[] params) {
        if (params == null) {
            return aiChatService.executeSkill(skillName);
        }
        return aiChatService.executeSkill(skillName, params);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public String health() {
        return "Skills service is running!";
    }
}
