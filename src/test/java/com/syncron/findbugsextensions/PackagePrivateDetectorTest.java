package com.syncron.findbugsextensions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.syncron.findbugsextensions.otherpackage.AnnotationForbiddenUser;
import com.syncron.findbugsextensions.otherpackage.PackagePrivateForbiddenExtender;
import com.syncron.findbugsextensions.otherpackage.PackagePrivateForbiddenUser;
import com.syncron.findbugsextensions.utils.BaseDetectorTestCase;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugPattern;

public class PackagePrivateDetectorTest extends BaseDetectorTestCase {

	@Test
	public void shouldAllowPackagePrivateForMethod() {
		// given
		Class<?> okClass = PackagePrivateUser.class;
		Class<?> packagePrivate = PackagePrivateAnnotatedMethodAndConstructor.class;

		// when
		List<BugInstance> bugs = runDetector(okClass, packagePrivate);

		// then
		assertTrue(bugs.isEmpty(), "There should be no PACKAGE_PRIVATE_USAGE bugs in " + okClass);
	}

	@Test
	public void shouldForbidPackagePrivateForMethod() {
		// given
		Class<?> classWithProblem = PackagePrivateForbiddenUser.class;
		Class<?> packagePrivate = PackagePrivateAnnotatedMethodAndConstructor.class;

		// when
		List<BugInstance> bugs = runDetector(classWithProblem, packagePrivate);

		// then
		assertEquals(bugs.size(), 2, "There should be 2 PACKAGE_PRIVATE_USAGE bugs in " + classWithProblem);
	}

	@Test
	public void shouldAllowPackagePrivateForClass() {
		// given
		Class<?> okClass = PackagePrivateExtender.class;
		Class<?> packagePrivate = PackagePrivateAnnotatedClass.class;

		// when
		List<BugInstance> bugs = runDetector(okClass, packagePrivate);

		// then
		assertEquals(bugs.size(), 0, "There should be no PACKAGE_PRIVATE_USAGE bug in " + okClass);
	}

	@Test
	public void shouldForbidPackagePrivateForClass() {
		// given
		Class<?> classWithProblem = PackagePrivateForbiddenExtender.class;
		Class<?> packagePrivate = PackagePrivateAnnotatedClass.class;

		// when
		List<BugInstance> bugs = runDetector(classWithProblem, packagePrivate);

		// then
		assertEquals(bugs.size(), 1, "There should be 1 PACKAGE_PRIVATE_USAGE bug in " + classWithProblem);
	}

	@Test
	public void shouldForbidPackagePrivateForMethodAnnotation() {
		// given
		Class<?> classWithProblem = AnnotationForbiddenUser.class;
		Class<?> packagePrivate = AnnotationPackagePrivate.class;

		// when
		List<BugInstance> bugs = runDetector(classWithProblem, packagePrivate);

		// then
		assertEquals(bugs.size(), 1, "There should be 1 PACKAGE_PRIVATE_USAGE bug in " + classWithProblem);
	}

	private List<BugInstance> runDetector(Class<?> offendingClass, Class<?> offendedClass) {
		BugPattern bugPattern = new BugPattern("PACKAGE_PRIVATE_USAGE", "PPU", "CORRECTNESS", true, "", "", "");

		// set up cache
		Collection<BugInstance> gathererBugs = runDetector(new PackagePrivateGatherer(getBugReporter()), offendedClass,
				bugPattern);
		Assert.assertTrue(gathererBugs.isEmpty(), "PackagePrivateGatherer should report no bugs");

		// find forbidden @PackagePrivate usage
		Collection<BugInstance> bugs = runDetector(new PackagePrivateDetector(getBugReporter()), offendingClass,
				bugPattern);
		return new LinkedList<BugInstance>(bugs);
	}
}
