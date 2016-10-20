//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.28 at 10:32:25 AM MDT 
//


package com.jeppesen.gs.otsas.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneralErrorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneralErrorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="erroneousSMI" type="{http://aeec.aviation-ia.net/633}char3Type" />
 *       &lt;attribute name="erroneousService" use="required" type="{http://aeec.aviation-ia.net/633}char3Type" />
 *       &lt;attribute name="erroneousElement" use="required" type="{http://aeec.aviation-ia.net/633}char3Type" />
 *       &lt;attribute name="erroneousVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="errorClass" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="errorType" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="errorData">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tryAgain" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneralErrorType")
public class GeneralErrorType {

    @XmlAttribute(name = "erroneousSMI")
    protected String erroneousSMI;
    @XmlAttribute(name = "erroneousService", required = true)
    protected String erroneousService;
    @XmlAttribute(name = "erroneousElement", required = true)
    protected String erroneousElement;
    @XmlAttribute(name = "erroneousVersion", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger erroneousVersion;
    @XmlAttribute(name = "errorClass")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger errorClass;
    @XmlAttribute(name = "errorType")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger errorType;
    @XmlAttribute(name = "errorData")
    protected String errorData;
    @XmlAttribute(name = "tryAgain")
    protected Boolean tryAgain;

    /**
     * Gets the value of the erroneousSMI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErroneousSMI() {
        return erroneousSMI;
    }

    /**
     * Sets the value of the erroneousSMI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErroneousSMI(String value) {
        this.erroneousSMI = value;
    }

    /**
     * Gets the value of the erroneousService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErroneousService() {
        return erroneousService;
    }

    /**
     * Sets the value of the erroneousService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErroneousService(String value) {
        this.erroneousService = value;
    }

    /**
     * Gets the value of the erroneousElement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErroneousElement() {
        return erroneousElement;
    }

    /**
     * Sets the value of the erroneousElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErroneousElement(String value) {
        this.erroneousElement = value;
    }

    /**
     * Gets the value of the erroneousVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErroneousVersion() {
        return erroneousVersion;
    }

    /**
     * Sets the value of the erroneousVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErroneousVersion(BigInteger value) {
        this.erroneousVersion = value;
    }

    /**
     * Gets the value of the errorClass property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorClass() {
        return errorClass;
    }

    /**
     * Sets the value of the errorClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorClass(BigInteger value) {
        this.errorClass = value;
    }

    /**
     * Gets the value of the errorType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorType() {
        return errorType;
    }

    /**
     * Sets the value of the errorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorType(BigInteger value) {
        this.errorType = value;
    }

    /**
     * Gets the value of the errorData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorData() {
        return errorData;
    }

    /**
     * Sets the value of the errorData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorData(String value) {
        this.errorData = value;
    }

    /**
     * Gets the value of the tryAgain property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTryAgain() {
        return tryAgain;
    }

    /**
     * Sets the value of the tryAgain property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTryAgain(Boolean value) {
        this.tryAgain = value;
    }

}
