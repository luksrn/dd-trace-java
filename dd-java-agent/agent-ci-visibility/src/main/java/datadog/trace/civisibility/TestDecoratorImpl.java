package datadog.trace.civisibility;

import datadog.trace.api.DDTags;
import datadog.trace.api.civisibility.decorator.TestDecorator;
import datadog.trace.api.sampling.PrioritySampling;
import datadog.trace.bootstrap.instrumentation.api.AgentSpan;
import datadog.trace.bootstrap.instrumentation.api.Tags;
import datadog.trace.bootstrap.instrumentation.api.UTF8BytesString;
import java.util.Map;

public class TestDecoratorImpl implements TestDecorator {

  private static final UTF8BytesString CIAPP_TEST_ORIGIN = UTF8BytesString.create("ciapp-test");

  private final String component;
  private final String testFramework;
  private final String testFrameworkVersion;
  private final Map<String, String> ciTags;

  public TestDecoratorImpl(
      String component,
      String testFramework,
      String testFrameworkVersion,
      Map<String, String> ciTags) {
    this.component = component;
    this.testFramework = testFramework;
    this.testFrameworkVersion = testFrameworkVersion;
    this.ciTags = ciTags;
  }

  protected String testType() {
    return TEST_TYPE;
  }

  protected String runtimeName() {
    return System.getProperty("java.runtime.name");
  }

  protected String runtimeVendor() {
    return System.getProperty("java.vendor");
  }

  protected String runtimeVersion() {
    return System.getProperty("java.version");
  }

  protected String osArch() {
    return System.getProperty("os.arch");
  }

  protected String osPlatform() {
    return System.getProperty("os.name");
  }

  protected String osVersion() {
    return System.getProperty("os.version");
  }

  protected UTF8BytesString origin() {
    return CIAPP_TEST_ORIGIN;
  }

  @Override
  public CharSequence component() {
    return component;
  }

  @Override
  public AgentSpan afterStart(final AgentSpan span) {
    span.setTag(Tags.TEST_FRAMEWORK, testFramework);
    span.setTag(Tags.TEST_FRAMEWORK_VERSION, testFrameworkVersion);
    span.setTag(Tags.TEST_TYPE, testType());
    span.setSamplingPriority(PrioritySampling.SAMPLER_KEEP);
    span.setTag(Tags.RUNTIME_NAME, runtimeName());
    span.setTag(Tags.RUNTIME_VENDOR, runtimeVendor());
    span.setTag(Tags.RUNTIME_VERSION, runtimeVersion());
    span.setTag(Tags.OS_ARCHITECTURE, osArch());
    span.setTag(Tags.OS_PLATFORM, osPlatform());
    span.setTag(Tags.OS_VERSION, osVersion());
    span.setTag(DDTags.ORIGIN_KEY, CIAPP_TEST_ORIGIN);
    span.setTag(Tags.COMPONENT, component());

    for (final Map.Entry<String, String> ciTag : ciTags.entrySet()) {
      span.setTag(ciTag.getKey(), ciTag.getValue());
    }

    return span;
  }

  @Override
  public AgentSpan beforeFinish(AgentSpan span) {
    return span;
  }
}
