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
 * <p>Java class for altitudeUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="altitudeUnitType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ft/100"/>
 *     &lt;enumeration value="ft/1000"/>
 *     &lt;enumeration value="ft"/>
 *     &lt;enumeration value="m/100"/>
 *     &lt;enumeration value="m/10"/>
 *     &lt;enumeration value="m"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "altitudeUnitType")
@XmlEnum
public enum AltitudeUnitType {

    @XmlEnumValue("ft/100")
    FT_100("ft/100"),
    @XmlEnumValue("ft/1000")
    FT_1000("ft/1000"),
    @XmlEnumValue("ft")
    FT("ft"),
    @XmlEnumValue("m/100")
    M_100("m/100"),
    @XmlEnumValue("m/10")
    M_10("m/10"),
    @XmlEnumValue("m")
    M("m");
    private final String value;

    AltitudeUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AltitudeUnitType fromValue(String v) {
        for (AltitudeUnitType c: AltitudeUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
