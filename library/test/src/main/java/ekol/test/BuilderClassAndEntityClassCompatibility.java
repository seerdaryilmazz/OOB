package ekol.test;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BuilderClassAndEntityClassCompatibility {

    /**
     * Bazen bir class'ta 'getXxx' veya 'isXXX' isimli bir metod varken 'xxx' isimli bir property olmayabiliyor.
     * Bu şekilde bir istisnası olan class'lar için bir girdi oluşturuyoruz.
     * key: entity class
     * value: entity class'ın içinde 'get' veya 'is' metodu olup aslında olmayan property'lerin listesi
     */
    private static final Map<String, Set<String>> ignoredPropertyDescriptorMap = new HashMap<>();

    static {
        ignoredPropertyDescriptorMap.put("ekol.orders.domain.TransportOrderRequest", new HashSet<String>(Arrays.asList("readyDateRemainingDays")));
        ignoredPropertyDescriptorMap.put("ekol.location.domain.ZoneZipCode", new HashSet<String>(Arrays.asList("rep")));
        ignoredPropertyDescriptorMap.put("ekol.location.domain.Route", new HashSet<String>(Arrays.asList("display")));
        ignoredPropertyDescriptorMap.put("ekol.location.domain.PolygonRegion", new HashSet<String>(Arrays.asList("hasParent", "hasChildren")));
    }

    private boolean isCompatible;

    private List<String> missingMethodsInBuilderClass = new ArrayList<>();

    private List<String> missingPropertiesInEntityClass = new ArrayList<>();

    public void addToMissingMethodsInBuilderClass(String methodName) {
        missingMethodsInBuilderClass.add(methodName);
    }

    public void addToMissingPropertiesInEntityClass(String propertyName) {
        missingPropertiesInEntityClass.add(propertyName);
    }

    public boolean isCompatible() {
        return isCompatible;
    }

    public void setCompatible(boolean compatible) {
        isCompatible = compatible;
    }

    public List<String> getMissingMethodsInBuilderClass() {
        return missingMethodsInBuilderClass;
    }

    public List<String> getMissingPropertiesInEntityClass() {
        return missingPropertiesInEntityClass;
    }

    /**
     * Standart bir builder class ile entity class'ın uyumlu olup olmadığını kontrol eder.
     */
    public static BuilderClassAndEntityClassCompatibility check(Class<?> builderClass, Class<?> entityClass) {
        BuilderClassAndEntityClassCompatibility result = new BuilderClassAndEntityClassCompatibility();
        result.setCompatible(true);

        // entityClass'ın içinde; ismi 'get' veya 'is' ile başlayan ve parametresi olmayan public metodları baz alarak
        // property listesini elde ediyoruz. Yani entityClass'ın JavaBean notasyonuna uyduğunu varsayıyoruz.
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(entityClass);

        // Bu map'i aşağıdaki tersten kontrolde kullanacağız.
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();

        String correspondingMethodName = null;
        Method correspondingMethod = null;

        for (PropertyDescriptor pd : propertyDescriptors) {

            // 1'inci koşul: Object'teki 'getClass' metodundan dolayı entityClass'ta 'class' isimli bir property varmış
            // gibi bir sonuç çıkıyor ancak bizim bunu yok saymamız gerekiyor.

            // 2'nci koşul: Bazen entityClass'ta 'getXxx' veya 'isXXX' isimli bir metod varken 'xxx' isimli bir property olmayabiliyor.
            // Bu nedenle property'nin gerçekten olup olmadığını kontrol ediyoruz.

            if (!pd.getName().equals("class") &&
                    (!ignoredPropertyDescriptorMap.containsKey(entityClass.getName()) || !ignoredPropertyDescriptorMap.get(entityClass.getName()).contains(pd.getName()))) {

                // Bu map'i aşağıdaki tersten kontrolde kullanacağız.
                propertyDescriptorMap.put(pd.getName(), pd);

                correspondingMethodName = "with" + StringUtils.capitalize(pd.getName());

                try {
                    correspondingMethod = builderClass.getMethod(correspondingMethodName, pd.getPropertyType());
                } catch (NoSuchMethodException e) {
                    correspondingMethod = null;
                }

                if (correspondingMethod == null || correspondingMethod.getModifiers() != Modifier.PUBLIC
                        || !correspondingMethod.getReturnType().equals(builderClass)) {

                    result.setCompatible(false);
                    result.addToMissingMethodsInBuilderClass("public " + builderClass.getName() + " " + correspondingMethodName + "(" + pd.getPropertyType().getName() + ")");
                }
            }
        }

        // Yukarıda, entityClass'taki her property'ye karşılık builderClass'ta bir method var mı diye
        // kontrol ettik. Burası ise tersten kontrol için.

        String correspondingPropertyName = null;
        PropertyDescriptor correspondingProperty = null;

        for (Method m : builderClass.getMethods()) {
            if (m.getModifiers() == Modifier.PUBLIC && m.getReturnType().equals(builderClass)
                    && m.getName().startsWith("with") && m.getParameterCount() == 1) {

                correspondingPropertyName = StringUtils.uncapitalize(StringUtils.replaceOnce(m.getName(), "with", ""));
                correspondingProperty = propertyDescriptorMap.get(correspondingPropertyName);

                if (correspondingProperty == null || !correspondingProperty.getPropertyType().equals(m.getParameterTypes()[0])) {
                    result.setCompatible(false);
                    result.addToMissingPropertiesInEntityClass(m.getParameterTypes()[0].getName() + " " + correspondingPropertyName);
                }
            }
        }

        return result;
    }
}
