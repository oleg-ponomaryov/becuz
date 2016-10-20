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
 * <p>Java class for intensityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="intensityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="light"/>
 *     &lt;enumeration value="light to moderate"/>
 *     &lt;enumeration value="moderate"/>
 *     &lt;enumeration value="moderate to severe"/>
 *     &lt;enumeration value="severe"/>
 *     &lt;enumeration value="extreme"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "intensityType")
@XmlEnum
public enum IntensityType {

    @XmlEnumValue("light")
    LIGHT("light"),
    @XmlEnumValue("light to moderate")
    LIGHT_TO_MODERATE("light to moderate"),
    @XmlEnumValue("moderate")
    MODERATE("moderate"),
    @XmlEnumValue("moderate to severe")
    MODERATE_TO_SEVERE("moderate to severe"),
    @XmlEnumValue("severe")
    SEVERE("severe"),
    @XmlEnumValue("extreme")
    EXTREME("extreme");
    private final String value;

    IntensityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IntensityType fromValue(String v) {
        for (IntensityType c: IntensityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}