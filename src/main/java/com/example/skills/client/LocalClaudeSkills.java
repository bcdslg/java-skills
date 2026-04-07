package com.example.skills.client;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * 本地 Claude Skills 服务类
 * 定义可以通过 AI 调用的工具方法
 */
@Service
public class LocalClaudeSkills {

    /**
     * 计算两个数的和
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之和
     */
    @Tool(description = "计算两个数字的和")
    public double add(double a, double b) {
        System.out.println("执行技能：加法计算 - " + a + " + " + b);
        return a + b;
    }

    /**
     * 计算两个数的乘积
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之积
     */
    @Tool(description = "计算两个数字的乘积")
    public double multiply(double a, double b) {
        System.out.println("执行技能：乘法计算 - " + a + " * " + b);
        return a * b;
    }

    /**
     * 获取当前天气信息（模拟）
     * @param city 城市名称
     * @return 天气信息
     */
    @Tool(description = "查询指定城市的天气信息")
    public String getWeather(String city) {
        System.out.println("执行技能：查询天气 - 城市：" + city);
        // 这里可以替换为实际调用本地 Claude skill 的逻辑
        return "当前 " + city + " 的天气是晴朗，温度 25°C";
    }

    /**
     * 搜索本地知识库
     * @param query 搜索关键词
     * @return 搜索结果
     */
    @Tool(description = "在本地知识库中搜索相关信息")
    public String searchKnowledge(String query) {
        System.out.println("执行技能：知识搜索 - 关键词：" + query);
        // 这里可以替换为实际调用本地 Claude skill 的逻辑
        return "关于 \"" + query + "\" 的搜索结果：找到 3 条相关记录...";
    }
}
