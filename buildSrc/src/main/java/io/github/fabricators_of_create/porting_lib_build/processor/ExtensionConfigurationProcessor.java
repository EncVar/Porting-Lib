package io.github.fabricators_of_create.porting_lib_build.processor;

import io.github.fabricators_of_create.porting_lib_build.PortingLibExtension;
import io.github.fabricators_of_create.porting_lib_build.ProjectProcessor;

import org.gradle.api.Project;

import java.util.List;

public class ExtensionConfigurationProcessor implements ProjectProcessor {
	@Override
	public void apply(Project project) {
		project.getExtensions().create("portingLib", PortingLibExtension.class);
	}
}
