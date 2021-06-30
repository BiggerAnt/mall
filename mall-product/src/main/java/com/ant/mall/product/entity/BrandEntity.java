package com.ant.mall.product.entity;

import com.ant.common.valid.AddGroup;
import com.ant.common.valid.ListValue;
import com.ant.common.valid.UpdateGroup;
import com.ant.common.valid.UpdateStatusGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 16:51:02
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	//分组校验，如果字段不加分组属性，那么校验会失效
	@NotNull(message = "修改必须指定品牌id" ,groups = {UpdateGroup.class})
	@Null(message = "新增不能指定id", groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	//必须是一个至少含一个字符的字符串
	@NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	//必须是一个url地址
	@NotEmpty(groups = {AddGroup.class})
	@URL(message = "logo必须是一个合法的url地址", groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
	@ListValue(values = {0,1}, groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	//自定义校验规则
	@NotEmpty(groups = {AddGroup.class})
	@Pattern(regexp = "[a-zA-Z]", message = "检索首字母必须是一个字母", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	//指定最小值
	@NotNull(groups = {AddGroup.class})
	@Min(value = 0, message = "排序必须是一个大于等于0的整数", groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
