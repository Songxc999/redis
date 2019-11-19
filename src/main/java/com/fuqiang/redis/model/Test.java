package com.fuqiang.redis.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>Title: Test</p>
 * <p>Description: Test</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/19 0019 10:35 Create by Fuqiang
 * </pre>
 */
// TODO     1.所有的对应表的Model类都需要增加标注 @TableName("tablename")
//          2.所有的属性类型尽量不使用值类型，例如int使用integer, long使用Long
//          3.ID列对应的属性增加标注@TableId
//          4.非表内字段属性（例如多表连接查询出来的其他表属性，临时属性等）加标注@TableField(exist = false)
//          5.实体属性和数据库字段名称不一致使用@TableField注解
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("test")
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * @TableId 决定主键的类型,不写会采取默认值,默认值可以在yml中配置
     * AUTO: 数据库ID自增
     * INPUT: 用户输入ID
     * ID_WORKER: 全局唯一ID，Long类型的主键
     * ID_WORKER_STR: 字符串全局唯一ID
     * UUID: 全局唯一ID，UUID类型的主键
     * NONE: 该类型为未设置主键类型
     */
    @TableId(value = "id")
    private String id;
    @TableField("name")
    private String name;
    private Integer age;
    private String sex;
}
