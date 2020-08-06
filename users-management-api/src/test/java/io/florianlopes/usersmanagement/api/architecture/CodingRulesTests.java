package io.florianlopes.usersmanagement.api.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;
import static io.florianlopes.usersmanagement.api.architecture.ArchitectureTests.BASE_PACKAGES;

import org.slf4j.Logger;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = BASE_PACKAGES, importOptions = ImportOption.DoNotIncludeTests.class)
public class CodingRulesTests {

    @ArchTest private final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest private final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    private final ArchRule loggers_should_be_private_static_final =
            fields().that()
                    .haveRawType(Logger.class)
                    .should()
                    .bePrivate()
                    .andShould()
                    .beStatic()
                    .andShould()
                    .beFinal();

    @ArchTest
    private final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    private final ArchRule no_access_to_standard_streams =
            NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
}
