package br.com.navita.patrimonio.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import br.com.navita.patrimonio.audit.AuditRevisionEntity;
import br.com.navita.patrimonio.dto.LoginDTO;
import br.com.navita.patrimonio.dto.MarcaDTO;
import br.com.navita.patrimonio.dto.PatrimonioDTO;
import br.com.navita.patrimonio.dto.PerfilDTO;
import br.com.navita.patrimonio.dto.TokenDTO;
import br.com.navita.patrimonio.dto.UsuarioDTO;

class PojoDynamicTest {

	@Test
	void testPojos() {
		dynamicAccess(
			MarcaDTO.class,
			PatrimonioDTO.class,
			PerfilDTO.class,
			UsuarioDTO.class,
			Marca.class,
			Patrimonio.class,
			Perfil.class,
			Usuario.class,
			LoginDTO.class,
			TokenDTO.class,
			AuditRevisionEntity.class
		);
	}

	private void dynamicAccess(final Class<?> ... clazzz) {
		for (final Class<?> clazz : clazzz) {
			final Object t1 = getInstance(clazz);
			final Object t2 = getInstance(clazz);
			final Object t3 = getInstance(clazz);
			final List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
			methods.sort((m1, m2) -> m2.getName().compareTo(m1.getName()));
			for (final Method method : methods) {
				try {
					final Object[] argNull = { null };
					final String name = method.getName();
					if (name.startsWith("get")
							|| name.equals("toString")
							|| name.equals("hashCode")) {
						method.invoke(t1);
						method.invoke(t3);
					} else if (name.equals("equals")
							|| name.equals("canEqual")) {
						final Object t4 = getInstance(clazz);
						final Object t5 = getInstance(clazz);
						final Object t6 = null;
						BeanUtils.copyProperties(t1, t4);
						method.invoke(t1, ""); //Other class
						method.invoke(t1, t1); //Same object
						method.invoke(t1, t2); //Different object and different field
						method.invoke(t1, t4); //Different object and same field
						method.invoke(t1, t3); //Different object, field1 not null and field3 null
						method.invoke(t3, t4); //Different object, field3 null and field4 not null
						method.invoke(t3, t5); //Different object, field3 null and field5 null
						method.invoke(t1, t6); //Different object, null object
					} else if (name.startsWith("set")) {
						final Parameter[] parameters = method.getParameters();
						final Object[] args1 = new Object[parameters.length];
						final Object[] args2 = new Object[parameters.length];
						for (int i = 0; i < parameters.length; i++) {
							final Class<?> pClass = parameters[i].getType();
							args1[i] = getValorDefault(pClass);
							args2[i] = getValorDefault(pClass);
						}
						method.invoke(t1, args1);
						method.invoke(t2, args2);
						method.invoke(t3, argNull);
					} else if (name.startsWith("convertToEntity")) {
						final Parameter[] parameters = method.getParameters();
						final Class<?> pClass = parameters[0].getType();
						final Object[] args = new Object[parameters.length];
						final Class<?> rClass = method.getReturnType();
						if (rClass.isEnum()) {
							final Object objEnum = getValues(rClass)[0];
							for (final Method methodEnum : objEnum.getClass().getDeclaredMethods()) {
								final Class<?> returnType = methodEnum.getReturnType();
								if (pClass.equals(returnType)) {
									args[0] = methodEnum.invoke(objEnum);
								}
							}
							method.invoke(t1, args);
						}
						method.invoke(t1, argNull);
						try {
							if (Character.class.isAssignableFrom(pClass)) {
									method.invoke(t1, Character.valueOf('Z'));
							} else if (String.class.isAssignableFrom(pClass)) {
								method.invoke(t1, "Z");

							}
						} catch (final Exception e) {
						}
					} else if (name.startsWith("convertToDatabase")) {
						final Parameter[] parameters = method.getParameters();
						final Object[] args = new Object[parameters.length];
						final Class<?> pClass = parameters[0].getType();
						if (pClass.isEnum()) {
							args[0] = getValues(pClass)[0];
						}
						method.invoke(t1, args);
					}
				} catch (final Exception e) {
				}
			}
			final Constructor<?>[] cons = clazz.getDeclaredConstructors();
			for (final Constructor<?> con : cons) {
				final Parameter[] parameters = con.getParameters();
				final Object[] args = new Object[parameters.length];
				for (int i = 0; i < parameters.length; i++) {
					try {
						final Class<?> pClass = parameters[i].getType();
						args[i] = getValorDefault(pClass);
						con.newInstance(args);
					} catch (final Exception e) {
					}
				}
			}
		}
	}

	private Object getInstance(final Class<?> clazz) {
		return !clazz.isEnum() ? BeanUtils.instantiateClass(clazz) : getValues(clazz)[0];
	}

	private Object getValorDefault(final Class<?> pClass) throws Exception {
		if (pClass.isEnum()) {
			return getValues(pClass)[0];
		} else if (XMLGregorianCalendar.class.isAssignableFrom(pClass)) {
			final GregorianCalendar gc = new GregorianCalendar();
			final DatatypeFactory dtf = DatatypeFactory.newInstance();
			return dtf.newXMLGregorianCalendar(gc);
		} else if (LocalDate.class.isAssignableFrom(pClass)) {
			return LocalDate.now();
		} else if (LocalDateTime.class.isAssignableFrom(pClass)) {
			return LocalDateTime.now();
		} else if (Character.class.isAssignableFrom(pClass)) {
			return Character.valueOf('A');
		} else if (Integer.class.isAssignableFrom(pClass) ||
				int.class.isAssignableFrom(pClass)) {
			return 1;
		} else if (Double.class.isAssignableFrom(pClass) ||
				double.class.isAssignableFrom(pClass)) {
			return 1D;
		} else if (Long.class.isAssignableFrom(pClass) ||
				long.class.isAssignableFrom(pClass)) {
			return 1L;
		} else if (Float.class.isAssignableFrom(pClass) ||
				float.class.isAssignableFrom(pClass)) {
			return 1F;
		} else if (BigDecimal.class.isAssignableFrom(pClass)) {
			return BigDecimal.ONE;
		} else {
			return BeanUtils.instantiateClass(pClass);
		}
	}

	private Object[] getValues(final Class<?> pClass) {
		try {
			final Method valuesMethod = pClass.getDeclaredMethod("values");
			final Object[] values = (Object[]) valuesMethod.invoke(null);
			return values;
		} catch (final Exception e) {
			return null;
		}
	}

}
