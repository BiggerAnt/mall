package com.ant.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    //初始化
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        int[] values = constraintAnnotation.values();
        for(int i : values){
            set.add(i);
        }
    }

    /**
     *
     * @param value 需要校验的值
     * @param context
     * @return
     */
    //判断是否校验成功
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
