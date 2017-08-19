package net.uchoice.exf.core.trace;

/**
 * 记录类型
 *
 */
public enum RecordType {
    /**
     * 会话
     */
    SESSION,
    /**
     * 容器
     */
    CONTAINER,
    /**
     * 节点
     */
    ACTION,
    /**
     * 处理器
     */
    HANDLER,
    /**
     * 参数处理
     */
    VARIABLE,
    /**
     * 匹配器
     */
    MATCH,
    /**
     * 身份识别
     */
    IDENTITY,
    /**
     * Other.
     */
    OTHER;
}
