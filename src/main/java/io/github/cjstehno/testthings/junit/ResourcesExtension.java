package io.github.cjstehno.testthings.junit;

import lombok.val;
import org.junit.jupiter.api.extension.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static io.github.cjstehno.testthings.Resources.*;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotatedFields;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isStatic;

// FIXME: docuemnt
public class ResourcesExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

    /*
    FIXME: document
    x when annotating a File or Path -> injects the resolved file or path value
    X when annotating a String, byte[] or InputStream -> injects the resolved resource
    - FIXME: when annotating an object (other than above) -> deserialize (?) how to determine what used and type?
 */

    private static final List<Class<?>> RESOURCE_TYPES = List.of(
        Path.class, File.class, byte[].class, String.class, InputStream.class, Reader.class
    );

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
        return paramContext.isAnnotated(Resource.class) && RESOURCE_TYPES.contains(paramType);
    }

    @Override
    public Object resolveParameter(final ParameterContext paramContext, final ExtensionContext extContext) throws ParameterResolutionException {
        val paramType = paramContext.getParameter().getType();
        val anno = paramContext.getParameter().getAnnotation(Resource.class);

        try {
            return resolveResource(anno, paramType);

        } catch (final Exception ex) {
            throw new ParameterResolutionException(
                "Unable to resolve parameter (%s): %s".formatted(
                    paramContext.getParameter().getName(),
                    ex.getMessage()
                ),
                ex
            );
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

    private static Object resolveResource(final Resource anno, final Class<?> type) throws URISyntaxException, IOException {
        if (Path.class.equals(type)) {
            return resourcePath(anno.value());

        } else if (File.class.equals(type)) {
            return resourceFile(anno.value());

        } else if (String.class.equals(type)) {
            return resourceToString(anno.value());

        } else if (InputStream.class.equals(type)) {
            return resourceStream(anno.value());

        } else if (Reader.class.equals(type)) {
            return resourceReader(anno.value());

        } else if (byte[].class.equals(type)) {
            return resourceToBytes(anno.value());

        } else {
            throw new IllegalArgumentException("Resource type (" + type.getSimpleName() + ") is not supported.");
        }
    }
}
