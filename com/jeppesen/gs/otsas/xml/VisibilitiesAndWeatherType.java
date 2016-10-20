//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.28 at 10:32:25 AM MDT 
//


package com.jeppesen.gs.otsas.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VisibilitiesAndWeatherType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VisibilitiesAndWeatherType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="CeilingAndVisibilityOK" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;sequence>
 *           &lt;element name="Visibilities" type="{http://aeec.aviation-ia.net/633}VisibilityType" minOccurs="0"/>
 *           &lt;element name="Clouds" type="{http://aeec.aviation-ia.net/633}CloudsType" minOccurs="0"/>
 *           &lt;choice minOccurs="0">
 *             &lt;element name="NilSignificantWeather">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;enumeration value="NSW"/>
 *                   &lt;enumeration value="NOSIG"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *             &lt;element name="Weather" maxOccurs="unbounded">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;extension base="{http://aeec.aviation-ia.net/633}WeatherType">
 *                   &lt;/extension>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *           &lt;/choice>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VisibilitiesAndWeatherType", propOrder = {
    "ceilingAndVisibilityOK",
    "visibilities",
    "clouds",
    "nilSignificantWeather",
    "weather"
})
public class VisibilitiesAndWeatherType {

    @XmlElement(name = "CeilingAndVisibilityOK")
    protected String ceilingAndVisibilityOK;
    @XmlElement(name = "Visibilities")
    protected VisibilityType visibilities;
    @XmlElement(name = "Clouds")
    protected CloudsType clouds;
    @XmlElement(name = "NilSignificantWeather")
    protected String nilSignificantWeather;
    @XmlElement(name = "Weather")
    protected List<VisibilitiesAndWeatherType.Weather> weather;

    /**
     * Gets the value of the ceilingAndVisibilityOK property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCeilingAndVisibilityOK() {
        return ceilingAndVisibilityOK;
    }

    /**
     * Sets the value of the ceilingAndVisibilityOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCeilingAndVisibilityOK(String value) {
        this.ceilingAndVisibilityOK = value;
    }

    /**
     * Gets the value of the visibilities property.
     * 
     * @return
     *     possible object is
     *     {@link VisibilityType }
     *     
     */
    public VisibilityType getVisibilities() {
        return visibilities;
    }

    /**
     * Sets the value of the visibilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link VisibilityType }
     *     
     */
    public void setVisibilities(VisibilityType value) {
        this.visibilities = value;
    }

    /**
     * Gets the value of the clouds property.
     * 
     * @return
     *     possible object is
     *     {@link CloudsType }
     *     
     */
    public CloudsType getClouds() {
        return clouds;
    }

    /**
     * Sets the value of the clouds property.
     * 
     * @param value
     *     allowed object is
     *     {@link CloudsType }
     *     
     */
    public void setClouds(CloudsType value) {
        this.clouds = value;
    }

    /**
     * Gets the value of the nilSignificantWeather property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNilSignificantWeather() {
        return nilSignificantWeather;
    }

    /**
     * Sets the value of the nilSignificantWeather property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNilSignificantWeather(String value) {
        this.nilSignificantWeather = value;
    }

    /**
     * Gets the value of the weather property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the weather property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWeather().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VisibilitiesAndWeatherType.Weather }
     * 
     * 
     */
    public List<VisibilitiesAndWeatherType.Weather> getWeather() {
        if (weather == null) {
            weather = new ArrayList<VisibilitiesAndWeatherType.Weather>();
        }
        return this.weather;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://aeec.aviation-ia.net/633}WeatherType">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Weather
        extends WeatherType
    {


    }

}
