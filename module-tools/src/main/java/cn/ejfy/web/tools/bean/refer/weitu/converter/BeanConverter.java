package com.weitu.dispuse.common.utils.bean.converter;

public interface BeanConverter<S, T> {

	void convert(S src, T target);
}
