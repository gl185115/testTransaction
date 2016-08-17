/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SampleModel
 *
 * Sample Model Class for testing
 *
 * Meneses, Chris Niven D.
 */
package ncr.res.mobilepos.helper.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="Sample")
public class SampleModel {
    @XmlElement(name="StringElem")
    private String stringElem;

    @XmlElement(name="IntElem")
    private int intElem;
    
    public final void setStringElem(final String value){
        stringElem = value;
    }
    
    public final void setIntElem(final int value){
        intElem = value;
    }
    
    public final String getStringElem(){
        return stringElem;
    }
    public final int getIntElem(){
        return intElem;
    }
}
