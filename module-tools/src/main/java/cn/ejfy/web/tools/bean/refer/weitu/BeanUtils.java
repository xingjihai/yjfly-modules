//package cn.ejfy.web.tools.bean.refer.weitu;
//
//import java.lang.reflect.Modifier;
//
//import com.weitu.dispuse.common.error.CommonErrorCode;
//import com.weitu.dispuse.common.utils.bean.converter.BeanConverter;
//import com.weitu.dispuse.common.utils.bean.converter.DefaultBeanConverter;
//import com.weitu.dispuse.common.utils.bean.converter.NullIgnoreBeanConverter;
//import com.weitu.framework.web.exception.ServiceException;
//
//public class BeanUtils {
//
//	private BeanUtils() {}
//
//	/**
//	 * 将src中的属性复制到target
//	 * @param src
//	 *            源对象
//	 * @param target
//	 *            目标对象
//	 * @param config
//	 *            设置
//	 */
//	public static <S, T> void copy(S src, T target, BeanConverter<S, T> converter) {
//		converter.convert(src, target);
//	}
//
//	/**
//	 * 将src中的属性复制到target
//	 * @param src
//	 *            源对象
//	 * @param target
//	 *            目标对象
//	 */
//	public static <S, T> void copy(S src, T target) {
//		new DefaultBeanConverter<S, T>().convert(src, target);
//	}
//
//	/**
//	 * 将src中的非空属性复制到target
//	 * @param src
//	 *            源对象
//	 * @param target
//	 *            目标对象
//	 */
//	public static <S, T> void merge(S src, T target) {
//		new NullIgnoreBeanConverter<S, T>().convert(src, target);
//	}
//
//	public static <S, T> T clone(S src, Class<? extends T> targetClass) {
//		String detailMessage = null;
//		if (targetClass.isArray()) {
//			detailMessage = "目标类型不能是数组类型";
//		} else if (targetClass.isPrimitive()) {
//			detailMessage = "目标类型不能是基本类型";
//		} else if (targetClass.isEnum()) {
//			detailMessage = "目标类型不能是枚举类型";
//		} else if (Modifier.isAbstract(targetClass.getModifiers())) {
//			detailMessage = "目标类型不能是抽象类、接口或注解类型";
//		}
//		if (detailMessage != null)
//			throw new ServiceException(CommonErrorCode.COMMON_BEAN_INVALID_TYPE, detailMessage);
//		T target;
//		try {
//			target = targetClass.newInstance();
//		} catch (InstantiationException e) {
//			throw new ServiceException(CommonErrorCode.COMMON_BEAN_INVOCATION_TARGET, e.getMessage());
//		} catch (IllegalAccessException e) {
//			throw new ServiceException(CommonErrorCode.COMMON_BEAN_ILLEGAL_ACCESS, e.getMessage());
//		}
//		copy(src, target);
//		return target;
//	}
//}
