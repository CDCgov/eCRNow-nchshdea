package com.drajer.eca.model;

import com.drajer.ecrapp.util.ApplicationUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.SetUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchedTriggerCodes {

  private static final Logger logger = LoggerFactory.getLogger(MatchedTriggerCodes.class);

  private Set<String> matchedCodes;
  private Set<String> matchedValues;
  private String valueSet;
  private String valueSetVersion;
  private String matchedPath;

  public MatchedTriggerCodes() {
    matchedCodes = new HashSet<>();
    matchedValues = new HashSet<>();
  }

  public Set<String> getMatchedCodes() {
    return matchedCodes;
  }

  public void setMatchedCodes(Set<String> matchedCodes) {
    this.matchedCodes = matchedCodes;
  }

  public String getValueSet() {
    return valueSet;
  }

  public void setValueSet(String valueSet) {
    this.valueSet = valueSet;
  }

  public String getValueSetVersion() {
    return valueSetVersion;
  }

  public void setValueSetVersion(String valueSetVersion) {
    this.valueSetVersion = valueSetVersion;
  }

  public String getMatchedPath() {
    return matchedPath;
  }

  public void setMatchedPath(String matchedPath) {
    this.matchedPath = matchedPath;
  }

  public Set<String> getMatchedValues() {
    return matchedValues;
  }

  public void setMatchedValues(Set<String> matchedValues) {
    this.matchedValues = matchedValues;
  }

  public Set<String> hasMatchedTriggerCodesFromCodeableConcept(
      String type, List<CodeableConcept> cds) {

    Set<String> retval = null;

    if (cds != null
        && matchedCodes != null
        && !matchedCodes.isEmpty()
        && matchedPath != null
        && matchedPath.contains(type)) {

      Set<String> codes = ApplicationUtils.convertR4CodeableConceptsToString(cds);

      logger.info(
          " Size of Matched Codes is {}, and size of passed in codes is {}",
          matchedCodes.size(),
          codes.size());

      Set<String> intersection = SetUtils.intersection(codes, matchedCodes);

      if (intersection != null && !intersection.isEmpty()) {

        logger.info(" Number of Matched Codes = {}", intersection.size());

        retval = intersection;

      } else {

        logger.info(" No Matched codes found for Codeable Concepts passed ");
      }
    }

    return retval;
  }

  public Boolean containsMatch(ResourceType rt) {

    if (hasMatchedTriggerCodes(rt.toString()) || hasMatchedTriggerValue(rt.toString())) return true;
    else return false;
  }

  public Boolean hasMatchedTriggerCodes(String type) {

    return matchedCodes != null
        && !matchedCodes.isEmpty()
        && matchedPath != null
        && matchedPath.contains(type);
  }

  public Boolean hasMatchedTriggerValue(String type) {

    return matchedValues != null
        && !matchedValues.isEmpty()
        && matchedPath != null
        && matchedPath.contains(type);
  }

  public void addCodes(Set<String> codes) {

    if (matchedCodes == null) matchedCodes = new HashSet<>();

    if (codes != null) {
      Set<String> union = SetUtils.union(matchedCodes, codes);

      matchedCodes = union;
    }
  }

  public void addCode(String code) {

    if (matchedCodes == null) matchedCodes = new HashSet<>();

    if (code != null) {

      matchedCodes.add(code);
    }
  }

  public void addValues(Set<String> values) {

    if (matchedValues == null) matchedValues = new HashSet<>();

    if (values != null) {
      Set<String> union = SetUtils.union(matchedValues, values);

      matchedValues = union;
    }
  }

  public void addValue(String value) {

    if (matchedValues == null) matchedValues = new HashSet<>();

    if (value != null) {

      matchedValues.add(value);
    }
  }

  public void log() {

    logger.info(" *** START Printing Matched Trigger Codes *** ");

    logger.info(" Matched Path {}", matchedPath);
    logger.info(" Matched ValueSet {}", valueSet);
    logger.info(" Matched ValueSetVersion {}", valueSetVersion);

    matchedCodes.forEach(matchedCode -> logger.info("Matched Code: {} ", matchedCode));
    matchedValues.forEach(matchedValue -> logger.info("Matched Code: {} ", matchedValue));

    logger.info(" *** END Printing Matched Trigger Codes *** ");
  }
}
