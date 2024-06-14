package com.easing.commons.android.annotation_configuration;

import com.easing.commons.android.annotation_processor.AnnotationHandler;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui_component.module_button.ModuleInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//通过注解来管理配置信息
@SuppressWarnings("all")
public class AnnotationConfiguration {

    static Class defaultConfigurationClass;

    public static void defaultConfigurationClass(Class<? extends IConfiguration> configuration) {
        AnnotationConfiguration.defaultConfigurationClass = configuration;
    }

    public static Integer findInt(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            IntValue annotation = method.getAnnotation(IntValue.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Long findLong(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            LongValue annotation = method.getAnnotation(LongValue.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Double findDouble(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            DoubleValue annotation = method.getAnnotation(DoubleValue.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Boolean findBool(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            BoolValue annotation = method.getAnnotation(BoolValue.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Boolean findBool(Class<? extends IConfiguration> configuration, String configName, boolean defaultValue) {
        Boolean config = findBool(configuration, configName);
        if (config == null)
            return defaultValue;
        return config;
    }

    public static String findString(Class configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            StringValue annotation = method.getAnnotation(StringValue.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Integer findId(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            IdValue annotation = method.getAnnotation(IdValue.class);
            String name = annotation.value();
            int id = AnnotationHandler.findLayout(name);
            return id;
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Integer findDrawable(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            DrawableValue annotation = method.getAnnotation(DrawableValue.class);
            String name = annotation.value();
            int drawable = AnnotationHandler.findDrawable(name);
            return drawable;
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Integer findLayout(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            LayoutValue annotation = method.getAnnotation(LayoutValue.class);
            String name = annotation.value();
            int layout = AnnotationHandler.findLayout(name);
            return layout;
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Class findType(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            TypeValue annotation = method.getAnnotation(TypeValue.class);
            if (Texts.isEmpty(annotation.className()))
                return annotation.clazz();
            return Reflection.findClass(annotation.className());
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static String[] findStringArray(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        if (configuration == null)
            return null;
        try {
            Method method = configuration.getMethod(configName);
            StringArray annotation = method.getAnnotation(StringArray.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Class[] findTypeArray(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        if (configuration == null)
            return null;
        try {
            Method method = configuration.getMethod(configName);
            TypeArray annotation = method.getAnnotation(TypeArray.class);
            if (Collections.isEmpty(annotation.classNames()))
                return annotation.classes();
            List<Class> classList = new ArrayList();
            for (String className : annotation.classNames())
                classList.add(Reflection.findClass(className));
            return Collections.toArray(classList, Class[]::new);
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }

    public static Class<? extends ModuleInfo>[] findModules(Class<? extends IConfiguration> configuration, String configName) {
        if (configuration == null)
            configuration = AnnotationConfiguration.defaultConfigurationClass;
        try {
            Method method = configuration.getMethod(configName);
            ModuleArray annotation = method.getAnnotation(ModuleArray.class);
            return annotation.value();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return null;
    }
}
