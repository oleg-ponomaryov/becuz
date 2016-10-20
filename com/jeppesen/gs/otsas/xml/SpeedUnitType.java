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
 * <p>Java class for speedUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="speedUnitType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="kt"/>
 *     &lt;enumeration value="m/s"/>
 *     &lt;enumeration value="km/h"/>
 *     &lt;enumeration value="ft/min"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "speedUnitType")
@XmlEnum
public enum SpeedUnitType {

    @XmlEnumValue("kt")
    KT("kt"),
    @XmlEnumValue("m/s")
    M_S("m/s"),
    @XmlEnumValue("km/h")
    KM_H("km/h"),
    @XmlEnumValue("ft/min")
    FT_MIN("ft/min");
    private final String value;

    SpeedUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SpeedUnitType fromValue(String v) {
        for (SpeedUnitType c: SpeedUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}