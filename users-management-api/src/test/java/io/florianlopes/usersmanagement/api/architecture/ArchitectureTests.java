package io.florianlopes.usersmanagement.api.architecture;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static io.florianlopes.usersmanagement.api.architecture.ArchitectureTests.BASE_PACKAGES;

import java.util.regex.Pattern;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SliceAssignment;
import com.tngtech.archunit.library.dependencies.SliceIdentifier;

@AnalyzeClasses(packages = BASE_PACKAGES, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTests {

    static final String BASE_PACKAGES = "io.florianlopes.usersmanagement.api";
    private static final Pattern DOT_PATTERN = Pattern.compile(".", Pattern.LITERAL);

    // https://github.com/TNG/ArchUnit-Examples/tree/master/example-junit5/src/test/java/com/tngtech/archunit/exampletest/junit5

    // Define domain slices
    static SliceAssignment domainSlices =
            new SliceAssignment() {
                @Override
                public SliceIdentifier getIdentifierOf(JavaClass javaClass) {
                    String packageName = javaClass.getPackageName();
                    if (packageName.startsWith(BASE_PACKAGES + ".config")
                            || packageName.startsWith(BASE_PACKAGES + ".common")) {
                        // Ignore .config and .common packages
                        return SliceIdentifier.ignore();
                    } else if (packageName.startsWith(BASE_PACKAGES + ".")) {
                        // Deduce domain from package name:
                        // io.florianlopes.usersmanagement.api.users.(...): 'users' domain
                        String domainName =
                                DOT_PATTERN
                                        .split(packageName.substring(BASE_PACKAGES.length() + 1))[
                                        0];
                        return SliceIdentifier.of(domainName);
                    }
                    return SliceIdentifier.ignore();
                }

                @Override
                public String getDescription() {
                    return "Domain packages";
                }
            };

    @ArchTest
    static final ArchRule domains_packages_are_independent =
            slices().assignedFrom(domainSlices).should().notDependOnEachOther();

    @ArchIgnore(reason = "No packages yet")
    @ArchTest
    private final ArchRule onion_architecture_is_respected =
            onionArchitecture()
                    .applicationServices(BASE_PACKAGES + ".config..")
                    .domainModels("..domain.model..", "..common..")
                    .domainServices("..domain.usecase", "..domain.adapter")
                    .adapter("persistence", "..persistence..")
                    .adapter("web", "..web..");
}
