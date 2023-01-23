package com.drajer.bsa.controller;

import static com.drajer.bsa.controller.ExpectedOutcome.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.TestPropertySource;

// The eRSD spec allows specifying action conditions as either CQL or FHIRPath
// This test disables CQL processing to ensure FhirPath functions as expected
// IOW, CQL alternative expressions should always be ignored.
@RunWith(Parameterized.class)
@TestPropertySource(
    properties = {
      "kar.directory=src/test/resources/Bsa/Scenarios/kars/fhirpath",
      "cql.enabled=false"
    })
public class FhirPathTest extends BaseKarsTest {
  protected FhirContext fhirContext = FhirContext.forCached(FhirVersionEnum.R4);

  public FhirPathTest(TestCaseInfo testCaseInfo) {
    super(testCaseInfo);
  }

  @Test
  public void test() throws Exception {
    // super.testScenarioAndValidate();
  }

  // This generates a list of "TestCaseInfos" that describe the scenario
  // Comment out lines that don't work to temporarily disable test cases.
  // for eCSD tests the main points are
  // 1. Whether or not a reporting Bundle is generated (i.e. initial-pop = true)
  // 2. The Bundle contains a Measure report (TODO: validate that the other
  // resources are present)
  // 3. The MeasureReport has the correct info for the test case
  @Parameters(name = "{0}")
  public static Collection<TestCaseInfo> data() {
    return Arrays.asList(
        new TestCaseInfo(
            "FhirPath",
            "http://hl7.org/fhir/us/ecr/PlanDefinition/FhirPath",
            "today_or_now",
            REPORTED));
  }
}
