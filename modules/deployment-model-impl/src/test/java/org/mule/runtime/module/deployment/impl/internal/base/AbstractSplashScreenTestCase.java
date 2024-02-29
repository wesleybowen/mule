/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.deployment.impl.internal.base;

import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.mule.runtime.core.api.config.MuleProperties.MULE_HOME_DIRECTORY_PROPERTY;
import static org.mule.runtime.core.internal.util.splash.SplashScreen.RUNTIME_VERBOSE;

import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.core.internal.util.splash.SplashScreen;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.junit4.rule.SystemProperty;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.hamcrest.Matcher;

public abstract class AbstractSplashScreenTestCase<S extends SplashScreen> extends AbstractMuleTestCase {

  @ClassRule
  public static TemporaryFolder workingDirectory = new TemporaryFolder();
  @Rule
  public SystemProperty muleHome = new SystemProperty(MULE_HOME_DIRECTORY_PROPERTY, workingDirectory.getRoot().getAbsolutePath());

  protected S splashScreen;

  protected abstract void setUpSplashScreen();

  protected abstract Matcher<String> getSimpleLogMatcher();

  protected abstract Matcher<String> getComplexLogMatcher();

  @Test
  public void simpleLogWhenVerbosityOff() {
    try {
      System.setProperty(RUNTIME_VERBOSE, "false");
      setUpSplashScreen();
      assertThat(splashScreen.toString(), getSimpleLogMatcher());
    } finally {
      System.clearProperty(RUNTIME_VERBOSE);
    }
  }

  @Test
  public void complexLogWhenVerbosityOn() {
    try {
      System.setProperty(RUNTIME_VERBOSE, "true");
      setUpSplashScreen();
      assertThat(splashScreen.toString(), getComplexLogMatcher());
    } finally {
      System.clearProperty(RUNTIME_VERBOSE);
    }
  }

  @Test
  public void complexLogWhenNoVerbositySpecified() {
    checkArgument(System.getProperty(RUNTIME_VERBOSE) == null, "Runtime verbosity should not be specified.");
    setUpSplashScreen();
    assertThat(splashScreen.toString(), getComplexLogMatcher());
  }
}
