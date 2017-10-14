package net.uchoice.exf.design.action;

import java.util.List;

import lombok.Data;

/**
 * @author CodeDoge 2017/10/12
 * @version 1.0
 * @since 1.0
 */
@Data
public class ExfAction {

    /** 编码 */
    private String code;
    /** 名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 属性 */
    private List<ActionVariable> fileds;
    /** 参数 */
    private List<ActionVariable> parameters;

}
