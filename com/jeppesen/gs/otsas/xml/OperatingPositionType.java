//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.28 at 10:32:25 AM MDT 
//


package com.jeppesen.gs.otsas.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperatingPositionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperatingPositionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ReliefCrew"/>
 *     &lt;enumeration value="InspectionRoleByTheCompany"/>
 *     &lt;enumeration value="InspectionRoleByTheAuthority"/>
 *     &lt;enumeration value="ZeroFlightHoursFlightTrainee"/>
 *     &lt;enumeration value="Route InstructorRole"/>
 *     &lt;enumeration value="LineCheckRole"/>
 *     &lt;enumeration value="ExtraOnFlight"/>
 *     &lt;enumeration value="StationInspectorRole"/>
 *     &lt;enumeration value="Observer"/>
 *     &lt;enumeration value="Trainer"/>
 *     &lt;enumeration value="TraineePurser"/>
 *     &lt;enumeration value="TraineeCabin"/>
 *     &lt;enumeration value="AssessorPurser"/>
 *     &lt;enumeration value="CabinSafetyCheckByAssessorPurser"/>
 *     &lt;enumeration value="Observer"/>
 *     &lt;enumeration value="AccountsOfficer"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OperatingPositionType")
@XmlEnum
public enum OperatingPositionType {

    @XmlEnumValue("ReliefCrew")
    RELIEF_CREW("ReliefCrew"),
    @XmlEnumValue("InspectionRoleByTheCompany")
    INSPECTION_ROLE_BY_THE_COMPANY("InspectionRoleByTheCompany"),
    @XmlEnumValue("InspectionRoleByTheAuthority")
    INSPECTION_ROLE_BY_THE_AUTHORITY("InspectionRoleByTheAuthority"),
    @XmlEnumValue("ZeroFlightHoursFlightTrainee")
    ZERO_FLIGHT_HOURS_FLIGHT_TRAINEE("ZeroFlightHoursFlightTrainee"),
    @XmlEnumValue("Route InstructorRole")
    ROUTE_INSTRUCTOR_ROLE("Route InstructorRole"),
    @XmlEnumValue("LineCheckRole")
    LINE_CHECK_ROLE("LineCheckRole"),
    @XmlEnumValue("ExtraOnFlight")
    EXTRA_ON_FLIGHT("ExtraOnFlight"),
    @XmlEnumValue("StationInspectorRole")
    STATION_INSPECTOR_ROLE("StationInspectorRole"),
    @XmlEnumValue("Observer")
    OBSERVER("Observer"),
    @XmlEnumValue("Trainer")
    TRAINER("Trainer"),
    @XmlEnumValue("TraineePurser")
    TRAINEE_PURSER("TraineePurser"),
    @XmlEnumValue("TraineeCabin")
    TRAINEE_CABIN("TraineeCabin"),
    @XmlEnumValue("AssessorPurser")
    ASSESSOR_PURSER("AssessorPurser"),
    @XmlEnumValue("CabinSafetyCheckByAssessorPurser")
    CABIN_SAFETY_CHECK_BY_ASSESSOR_PURSER("CabinSafetyCheckByAssessorPurser"),
    @XmlEnumValue("AccountsOfficer")
    ACCOUNTS_OFFICER("AccountsOfficer");
    private final String value;

    OperatingPositionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperatingPositionType fromValue(String v) {
        for (OperatingPositionType c: OperatingPositionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
