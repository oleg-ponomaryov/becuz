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
 * <p>Java class for StatisticType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StatisticType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ArrivalFuel"/>
 *     &lt;enumeration value="ContingencyFuel"/>
 *     &lt;enumeration value="PilotExtraFuel"/>
 *     &lt;enumeration value="TaxiFuel"/>
 *     &lt;enumeration value="TaxiInFuel"/>
 *     &lt;enumeration value="TaxiOutFuel"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatisticType")
@XmlEnum
public enum StatisticType {

    @XmlEnumValue("ArrivalFuel")
    ARRIVAL_FUEL("ArrivalFuel"),
    @XmlEnumValue("ContingencyFuel")
    CONTINGENCY_FUEL("ContingencyFuel"),
    @XmlEnumValue("PilotExtraFuel")
    PILOT_EXTRA_FUEL("PilotExtraFuel"),
    @XmlEnumValue("TaxiFuel")
    TAXI_FUEL("TaxiFuel"),
    @XmlEnumValue("TaxiInFuel")
    TAXI_IN_FUEL("TaxiInFuel"),
    @XmlEnumValue("TaxiOutFuel")
    TAXI_OUT_FUEL("TaxiOutFuel");
    private final String value;

    StatisticType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StatisticType fromValue(String v) {
        for (StatisticType c: StatisticType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}