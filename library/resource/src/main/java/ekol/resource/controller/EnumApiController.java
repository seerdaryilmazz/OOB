package ekol.resource.controller;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.CaseFormat;

import ekol.exceptions.*;


@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class EnumApiController {

	private EnumCollection enumCollection = this.getClass().getAnnotation(EnumCollection.class);

	public EnumApiController() {
		if(null == enumCollection) {
			throw new ValidationException("no EnumCollection annotation is specified");
		}
		if(ArrayUtils.isEmpty(enumCollection.packages()) && ArrayUtils.isEmpty(enumCollection.enums())) {
			throw new ValidationException("no EnumCollection annotation attribute is specified");
		}
	}

	@GetMapping("/{enumName}")
	public Object[] listEnumValues(@PathVariable String enumName, @RequestParam Map<String, String> params) {
		if(CollectionUtils.isEmpty(params)) {
			return getEnumType(enumName).getEnumConstants();
		} else {
			return Stream.of(getEnumType(enumName).getEnumConstants()).filter(t->this.filter(t, params)).toArray();
		}
	}

	@GetMapping("/{enumName}/{name}")
	public Object listEnumValues(@PathVariable String enumName, @PathVariable String name) {
		try {
			return Enum.valueOf(getEnumType(enumName), name);
		} catch (IllegalArgumentException e ) {
			throw new ValidationException("{} lookup value not found", name);
		}
	}

	private Class getEnumType(String enumName) {
		String className = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, enumName);
		for (Class<?> cls : enumCollection.enums()) {
			if(Objects.equals(cls.getSimpleName(), className)) {
				return cls;
			}
		}
		for (String pkg : enumCollection.packages()) {
			try {
				return Class.forName(pkg + "." + className);
			} catch(Exception e) {}
		}
		throw new ApplicationException("No enum type is found");
	}

	private boolean filter(Object enumConstant, Map<String, String> params) {
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Field field = enumConstant.getClass().getDeclaredField(entry.getKey());
				if(Objects.nonNull(field)) {
					field.setAccessible(true);
					if(!Objects.equals(field.getType().cast(entry.getValue()), field.get(enumConstant))){
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
