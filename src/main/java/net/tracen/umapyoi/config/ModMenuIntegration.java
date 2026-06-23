package net.tracen.umapyoi.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.network.chat.Component;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.config.helper.*;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.LongFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.gui.controllers.slider.LongSliderController;

@SuppressWarnings("unused")
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(configText("title"))
                .categories(buildCategories())
                .save(UmapyoiConfig.HANDLER::save)
                .build()
                .generateScreen(parentScreen);
    }

    private static Collection<ConfigCategory> buildCategories() {
        Collection<ConfigCategory> categories = new ArrayList<>();
        ConfigCategory.Builder categoryBuilder = null;
        for (Field field : UmapyoiConfig.class.getFields()) {
            if (!field.isAnnotationPresent(SerialEntry.class))
                continue;

            var category = field.getAnnotation(Category.class);
            if (category != null) {
                if (categoryBuilder != null) categories.add(categoryBuilder.build());
                categoryBuilder = ConfigCategory.createBuilder()
                        .name(configText("category." + category.value()));
            }

            if (categoryBuilder == null)
                continue;

            Class<?> clazz = field.getType();
            Option<?> option;
            if (clazz == double.class) {
                Function<Option<Double>, ControllerBuilder<Double>> controllerBuilder;

                var doubleSlider = field.getAnnotation(DoubleSlider.class);
                if (doubleSlider != null) {
                    Method valueFormatterMethod = getValueFormatterMethod(
                            doubleSlider.valueFormatter(), Double.class);
                    ValueFormatter<Double> valueFormatter = createValueFormatter(
                            valueFormatterMethod, DoubleSliderController.DEFAULT_FORMATTER::apply);
                    controllerBuilder = opt -> DoubleSliderControllerBuilder.create(opt)
                            .range(doubleSlider.min(), doubleSlider.max())
                            .step(doubleSlider.step())
                            .formatValue(valueFormatter);
                }
                else {
                    var doubleField = field.getAnnotation(DoubleField.class);
                    controllerBuilder = opt -> {
                        var builder = DoubleFieldControllerBuilder.create(opt);
                        if (doubleField != null) {
                            Method valueFormatterMethod = getValueFormatterMethod(
                                    doubleField.valueFormatter(), Double.class);
                            ValueFormatter<Double> valueFormatter = createValueFormatter(
                                    valueFormatterMethod, DoubleSliderController.DEFAULT_FORMATTER::apply);
                            builder = builder.min(doubleField.min())
                                    .max(doubleField.max())
                                    .formatValue(valueFormatter);
                        }
                        return builder;
                    };
                }

                option = createOption(field, controllerBuilder);
            }
            else if (clazz == int.class) {
                var integerField = field.getAnnotation(IntegerField.class);
                Function<Option<Integer>, ControllerBuilder<Integer>> controllerBuilder = opt -> {
                    var builder = IntegerFieldControllerBuilder.create(opt);
                    if (integerField != null) {
                        Method valueFormatterMethod = getValueFormatterMethod(
                                integerField.valueFormatter(), Integer.class);
                        ValueFormatter<Integer> valueFormatter = createValueFormatter(
                                valueFormatterMethod, IntegerSliderController.DEFAULT_FORMATTER::apply);
                        builder = builder.min(integerField.min())
                                .max(integerField.max())
                                .formatValue(valueFormatter);
                    }
                    return builder;
                };

                option = createOption(field, controllerBuilder);
            }
            else if (clazz == long.class) {
                var longField = field.getAnnotation(LongField.class);
                Function<Option<Long>, ControllerBuilder<Long>> controllerBuilder = opt -> {
                    var builder = LongFieldControllerBuilder.create(opt);
                    if (longField != null) {
                        Method valueFormatterMethod = getValueFormatterMethod(
                                longField.valueFormatter(), Long.class);
                        ValueFormatter<Long> valueFormatter = createValueFormatter(
                                valueFormatterMethod, LongSliderController.DEFAULT_FORMATTER::apply);
                        builder = builder.min(longField.min())
                                .max(longField.max())
                                .formatValue(valueFormatter);
                    }
                    return builder;
                };

                option = createOption(field, controllerBuilder);
            }
            else if (clazz == boolean.class) {
                option = createOption(field, BooleanControllerBuilder::create);
            }
            else {
                throw new RuntimeException("Unsupported type in config class: " + clazz.getName());
            }

            categoryBuilder = categoryBuilder.option(option);
        }

        // add the last category before returning
        if (categoryBuilder != null)
            categories.add(categoryBuilder.build());

        return categories;
    }

    private static <T> Option<T> createOption(
            Field field,
            Function<Option<T>, ControllerBuilder<T>> controllerBuilder
    ) {
        var key = "option." + field.getName().toLowerCase();
        var defaults = UmapyoiConfig.HANDLER.defaults();

        T defaultValue;
        try {
            @SuppressWarnings("unchecked")
            T val = (T) field.get(defaults);
            defaultValue = val;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return Option.<T>createBuilder()
                .name(configText(key))
                .description(OptionDescription.of(optionalConfigText(key + ".desc")))
                .binding(defaultValue,
                        () -> {
                            try {
                                @SuppressWarnings("unchecked")
                                T value = (T) field.get(Umapyoi.CONFIG);
                                return value;
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        val -> {
                            try {
                                field.setAccessible(true);
                                field.set(Umapyoi.CONFIG, val);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .controller(controllerBuilder)
                .build();
    }

    private static Method getValueFormatterMethod(String methodName, Class<?> param) {
        Method valueFormatterMethod = null;
        if (!methodName.isEmpty()) {
            try {
                valueFormatterMethod = UmapyoiConfig.class.getMethod(methodName, param);
            }
            catch (Exception ignored) {}
        }
        return valueFormatterMethod;
    }

    private static <T> ValueFormatter<T> createValueFormatter(
            @Nullable Method valueFormatterMethod,
            ValueFormatter<T> defaultFormatter
    ) {
        return valueFormatterMethod != null
                ? val -> {
                    try {
                        return (Component) valueFormatterMethod.invoke(null, val);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                : defaultFormatter;
    }

    private static Component configText(String key) {
        return Component.translatable("text.config.umapyoi." + key);
    }

    private static Component optionalConfigText(String key) {
        return Component.translatableWithFallback("text.config.umapyoi." + key, "");
    }
}
