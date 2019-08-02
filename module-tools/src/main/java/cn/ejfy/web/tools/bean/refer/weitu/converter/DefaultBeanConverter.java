//package cn.ejfy.web.tools.bean.refer.weitu.converter;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//
//import com.weitu.dispuse.common.error.CommonErrorCode;
//import com.weitu.framework.web.exception.ServiceException;
//
///**
// * 对目标对象所有属性，将源对象的匹配属性复制
// *
// * @author Finally
// *
// * @param <S>
// *            源对象类型
// * @param <T>
// *            目标对象类型
// */
//public class DefaultBeanConverter<S, T> implements BeanConverter<S, T> {
//
//	@Override
//	public void convert(S src, T target) {
//		Class<?> srcClass = src.getClass();
//		Method[] methods = target.getClass().getMethods();
//		for (Method method : methods) {
//			if (Modifier.isStatic(method.getModifiers()))
//				continue;
//			String methodName = method.getName();
//			Class<?>[] parameterTypes = method.getParameterTypes();
//			if (methodName.startsWith("set") && parameterTypes.length == 1) {
//				String getterName = methodName.replaceFirst("s", "g");
//				Method getter;
//				try {
//					getter = srcClass.getMethod(getterName);
//					if (Modifier.isStatic(getter.getModifiers()))
//						continue;
//					doConvert(getter, src, method, target);
//				} catch (NoSuchMethodException | IllegalAccessException e) {
//					continue;
//				} catch (InvocationTargetException e) {
//					throw new ServiceException(CommonErrorCode.COMMON_BEAN_INVOCATION_TARGET, e.getMessage());
//				}
//			}
//		}
//	}
//
//	protected void doConvert(Method getter, S src, Method setter, T target)
//			throws IllegalAccessException, InvocationTargetException {
//		Object value = get(getter, src);
//		set(setter, target, value);
//	}
//
//	protected Object get(Method getter, S src) throws IllegalAccessException, InvocationTargetException {
//		return getter.invoke(src);
//	}
//
//	protected void set(Method setter, T target, Object value) throws IllegalAccessException, InvocationTargetException {
//		setter.invoke(target, value);
//	}
//}