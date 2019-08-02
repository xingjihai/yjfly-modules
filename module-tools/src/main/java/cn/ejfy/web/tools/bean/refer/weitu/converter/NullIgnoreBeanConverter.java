//package com.weitu.dispuse.common.utils.bean.converter;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
///**
// * 对目标对象所有属性，将源对象的非空匹配属性复制
// *
// * @author Finally
// *
// * @param <S>
// *            源对象类型
// * @param <T>
// *            目标对象类型
// */
//public class NullIgnoreBeanConverter<S, T> extends DefaultBeanConverter<S, T> {
//
//	@Override
//	protected void set(Method setter, T target, Object value) throws IllegalAccessException, InvocationTargetException {
//		if (value != null)
//			super.set(setter, target, value);
//	}
//}
