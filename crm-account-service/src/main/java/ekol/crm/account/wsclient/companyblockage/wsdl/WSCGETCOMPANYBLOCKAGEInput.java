//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.11 at 11:14:16 AM EET 
//


package ekol.crm.account.wsclient.companyblockage.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="I_COMPANY_ID-VARCHAR2-IN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="O_BLOCKAGE_CODE-VARCHAR2-OUT"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "icompanyidvarchar2IN",
    "oblockagecodevarchar2OUT"
})
@XmlRootElement(name = "WSC_GET_COMPANY_BLOCKAGEInput")
public class WSCGETCOMPANYBLOCKAGEInput {

    @XmlElement(name = "I_COMPANY_ID-VARCHAR2-IN", required = true)
    protected String icompanyidvarchar2IN;
    @XmlElement(name = "O_BLOCKAGE_CODE-VARCHAR2-OUT", required = true)
    protected WSCGETCOMPANYBLOCKAGEInput.OBLOCKAGECODEVARCHAR2OUT oblockagecodevarchar2OUT;

    /**
     * Gets the value of the icompanyidvarchar2IN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICOMPANYIDVARCHAR2IN() {
        return icompanyidvarchar2IN;
    }

    /**
     * Sets the value of the icompanyidvarchar2IN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICOMPANYIDVARCHAR2IN(String value) {
        this.icompanyidvarchar2IN = value;
    }

    /**
     * Gets the value of the oblockagecodevarchar2OUT property.
     * 
     * @return
     *     possible object is
     *     {@link WSCGETCOMPANYBLOCKAGEInput.OBLOCKAGECODEVARCHAR2OUT }
     *     
     */
    public WSCGETCOMPANYBLOCKAGEInput.OBLOCKAGECODEVARCHAR2OUT getOBLOCKAGECODEVARCHAR2OUT() {
        return oblockagecodevarchar2OUT;
    }

    /**
     * Sets the value of the oblockagecodevarchar2OUT property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSCGETCOMPANYBLOCKAGEInput.OBLOCKAGECODEVARCHAR2OUT }
     *     
     */
    public void setOBLOCKAGECODEVARCHAR2OUT(WSCGETCOMPANYBLOCKAGEInput.OBLOCKAGECODEVARCHAR2OUT value) {
        this.oblockagecodevarchar2OUT = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class OBLOCKAGECODEVARCHAR2OUT {


    }

}