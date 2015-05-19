package com.dss.tpcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paladii on 16.05.2015.
 */
@XmlRootElement(name = "hotel-booking")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlyBooking {
    public FlyBooking() {
    }

    public FlyBooking(Integer bookingID, String clientName, String flyNumber, String from, String to, String date) {
        BookingID = bookingID;
        ClientName = clientName;
        FlyNumber = flyNumber;
        From = from;
        To = to;
        Date = date;
    }

    @XmlElement(name = "BookingID")
    public Integer BookingID;

    @XmlElement(name = "ClientName")
    public String ClientName;

    @XmlElement(name = "FlyNumber")
    public String FlyNumber;

    @XmlElement(name = "From")
    public String From;

    @XmlElement(name = "To")
    public String To;

    @XmlElement(name = "Date")
    public String Date;

    public Integer getBookingID() {
        return BookingID;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getFlyNumber() {
        return FlyNumber;
    }

    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    public String getDate() {
        return Date;
    }


}
