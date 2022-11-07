/**
 * Copyright (C) 2022 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.testthings.junit;

import io.github.cjstehno.testthings.Resources;
import io.github.cjstehno.testthings.serdes.JacksonJsonSerdes;
import io.github.cjstehno.testthings.serdes.SerdesProvider;
import lombok.val;
import org.junit.jupiter.api.extension.*;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

import static io.github.cjstehno.testthings.Resources.resourceDeserialized;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotatedFields;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isStatic;

/**
 * A JUnit 5 extension that provides injection of classpath resources based on object type annotated with the {@link Resource}
 * annotation - the supported types are as follows:
 * <p>
 * <strong>Path.</strong> A {@link Path} will be populated with the path representation of the provided classpath value.
 * <p>
 * <strong>File.</strong> A {@link File} will be populated with the file representation of the provided classpath value.
 * <p>
 * <strong>String.</strong> A {@link String} will be populated with the contents of the file at the classpath location,
 * as a String.
 * <p>
 * <strong>InputStream.</strong> An {@link InputStream} will be populated with the content of the file at the classpath
 * location, as an InputStream.
 * <p>
 * <strong>Reader.</strong> A {@link Reader} will be populated with the content of the file at the classpath location,
 * as a Reader.
 * <p>
 * <strong>byte[].</strong> A byte array will be populated with the content of the file at the classpath location, as a
 * array of bytes.
 * <p>
 * <strong>Everything else.</strong> Any other object type will attempt to deserialize the contents of the file at the
 * classpath location using the configured "serdes" value of the annotation (defaulting to {@link JacksonJsonSerdes} if
 * none is specified.
 * <p>
 * The annotated types may be on:
 * <p>
 * <strong>Static Fields.</strong> A "static" field annotated with the {@link Resource} annotation will be populated
 * during the "BeforeAll" callback.
 * <p>
 * <strong>Non-Static Fields.</strong> A non-static field annotated with the {@link Resource} annotation will be populated
 * during the "BeforeEach" callback.
 * <p>
 * <strong>Callback or Test Method Parameters.</strong> A lifecycle callback or test method parameter annotated with the
 * {@link Resource} annotation will be populated when that method is called by the test framework.
 * <p>
 * Note: All injected fields will be cleared (set to null) during the appropriate "after" callback.
 */
public class ResourcesExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

    private static final Map<Class<?>, Function<String, Object>> RESOLVERS = Map.of(Path.class, Resources::resourcePath, File.class, Resources::resourceFile, String.class, Resources::resourceToString, InputStream.class, Resources::resourceStream, Reader.class, Resources::resourceReader, byte[].class, Resources::resourceToBytes);

    @Override public void beforeAll(final ExtensionContext context) throws Exception {
        val testClass = context.getRequiredTestClass();
        updateAnnotatedFields(testClass, testClass, true);
    }

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        updateAnnotatedFields(context.getRequiredTestClass(), context.getRequiredTestInstance(), false);
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        clearAnnotatedFields(context.getRequiredTestClass(), context.getRequiredTestInstance(), false);
    }

    @Override public void afterAll(final ExtensionContext context) throws Exception {
        val testClass = context.getRequiredTestClass();
        clearAnnotatedFields(testClass, testClass, true);
    }

    @Override
    public boolean supportsParameter(final ParameterContext paramContext, final ExtensionContext extContext) throws ParameterResolutionException {
        val paramType = paramContext.getParameter().getType();
        return paramContext.isAnnotated(Resource.class); // && RESOURCE_TYPES.contains(paramType);
    }

    @Override
    public Object resolveParameter(final ParameterContext paramContext, final ExtensionContext extContext) throws ParameterResolutionException {
        val paramType = paramContext.getParameter().getType();
        val anno = paramContext.getParameter().getAnnotation(Resource.class);

        try {
            return resolveResource(anno, paramType);

        } catch (final Exception ex) {
            throw new ParameterResolutionException("Unable to resolve parameter (%s): %s".formatted(paramContext.getParameter().getName(), ex.getMessage()), ex);
        }
    }

    private static void updateAnnotatedFields(final Class<?> testClass, final Object invokeOn, final boolean isStatic) throws Exception {
        for (val field : findAnnotatedFields(testClass, Resource.class, f -> isStatic(f) == isStatic, TOP_DOWN)) {
            field.setAccessible(true);
            field.set(invokeOn, resolveResource(field.getAnnotation(Resource.class), field.getType()));
        }
    }

    private static void clearAnnotatedFields(final Class<?> testClass, final Object invokeOn, final boolean isStatic) throws Exception {
        for (val field : findAnnotatedFields(testClass, Resource.class, f -> isStatic(f) == isStatic, TOP_DOWN)) {
            field.setAccessible(true);
            field.set(invokeOn, null);
        }
    }

    private static Object resolveResource(final Resource anno, final Class<?> type) {
        return RESOLVERS.getOrDefault(type, path -> resourceDeserialized(instantiateSerdesProvider(anno.serdes()), path, type)).apply(anno.value());
    }

    private static SerdesProvider instantiateSerdesProvider(final Class<? extends SerdesProvider> providerClass) {
        try {
            return providerClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
