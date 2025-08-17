package dev.piotrulla.vouchers;

import com.google.gson.Gson;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class VoucherLoader implements PluginLoader {

    private static final Logger LOGGER = Logger.getLogger(VoucherLoader.class.getName());
    private static final Gson GSON = new Gson();
    private static final String LIBRARIES_JSON_PATH = "/paper-libraries.json";

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        try {
            MavenLibraryResolver resolver = new MavenLibraryResolver();
            PluginLibraries pluginLibraries = loadLibraries();

            pluginLibraries.asDependencies().forEach(resolver::addDependency);

            pluginLibraries.asRepositories().forEach(resolver::addRepository);

            classpathBuilder.addLibrary(resolver);

            LOGGER.info("Successfully loaded " + pluginLibraries.dependencies().size() + " dependencies and "
                    + pluginLibraries.repositories().size() + " repositories");
        }
        catch (Exception exception) {
            LOGGER.severe("Failed to load plugin libraries: " + exception.getMessage());
            throw new RuntimeException("Plugin library loading failed", exception);
        }
    }

    private PluginLibraries loadLibraries() {
        try (InputStream inputStream = getClass().getResourceAsStream(LIBRARIES_JSON_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException("Libraries configuration file not found: " + LIBRARIES_JSON_PATH);
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                PluginLibraries libraries = GSON.fromJson(reader, PluginLibraries.class);

                if (libraries == null) {
                    throw new IllegalStateException("Failed to parse libraries configuration");
                }

                return libraries;
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to read libraries configuration", exception);
        }
    }

    private record PluginLibraries(@NotNull Map<String, String> repositories, @NotNull List<String> dependencies) {
        public Stream<Dependency> asDependencies() {
            return dependencies.stream()
                    .map(dependencyString -> new Dependency(new DefaultArtifact(dependencyString), null));
        }

        public Stream<RemoteRepository> asRepositories() {
            return repositories.entrySet()
                    .stream()
                    .map(entry -> new RemoteRepository.Builder(entry.getKey(), "default", entry.getValue()).build());
        }
    }
}
