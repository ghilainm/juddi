
package org.apache.juddi.api_v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for save_publisher complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="save_publisher">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="authInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publisher" type="{urn:juddi-apache-org:api_v3}publisher" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "save_publisher", propOrder = {
    "authInfo",
    "publisher"
})
public class SavePublisher {

    protected String authInfo;
    @XmlElement(required = true)
    protected List<Publisher> publisher;

    /**
     * Gets the value of the authInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthInfo() {
        return authInfo;
    }

    /**
     * Sets the value of the authInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthInfo(String value) {
        this.authInfo = value;
    }

    /**
     * Gets the value of the publisher property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publisher property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublisher().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Publisher }
     * 
     * 
     */
    public List<Publisher> getPublisher() {
        if (publisher == null) {
            publisher = new ArrayList<Publisher>();
        }
        return this.publisher;
    }

}
