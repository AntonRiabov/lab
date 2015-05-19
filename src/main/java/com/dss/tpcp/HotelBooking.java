package com.dss.tpcp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by paladii on 16.05.2015.
 */
@XmlRootElement(name = "hotel-booking")
@XmlAccessorType(XmlAccessType.FIELD)
public class HotelBooking {
    public HotelBooking() {
    }

    public HotelBooking(Integer bookingID, String clientName, String hotelName, String aarrival, String departue) {

        BookingID = bookingID;
        ClientName = clientName;
        HotelName = hotelName;
        Aarrival = aarrival;
        Departue = departue;
    }

    @XmlElement(name = "BookingID")
    public Integer BookingID;

    @XmlElement(name = "ClientName")
    public String ClientName;

    @XmlElement(name = "HotelName")
    public String HotelName;

    @XmlElement(name = "Aarrival")
    public String Aarrival;

    @XmlElement(name = "Departue")
    public String Departue;

    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(HotelBooking.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(new HotelBooking(12, "clientName", "hotelName", "aarrival", "departue"), new File("hotelbooking.xml"));

            context = JAXBContext.newInstance(FlyBooking.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FlyBooking flyBooking = (FlyBooking) unmarshaller.unmarshal( new File("flybooking.xml"));
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(new FlyBooking(12, "clientName", "flyNumber", "from", "to", "date"), new File("flybooking.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
