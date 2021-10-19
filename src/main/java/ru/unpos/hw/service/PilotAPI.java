package ru.unpos.hw.service;

import ru.unpos.hw.service.acquiring.sber.commands.*;
import ru.unpos.hw.service.requests.ReturnSaleRequest;
import ru.unpos.hw.service.requests.SaleRequest;
import ru.unpos.hw.service.acquiring.sber.Pilot;
import ru.unpos.hw.service.acquiring.sber.PilotException;
import ru.unpos.hw.service.acquiring.sber.types.TransactionData;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sb-pilot")
public class PilotAPI {

    @Inject
    Pilot pilot;

    //@Inject
    InMemoryConfigSource cfg;

    @Inject
    Cfg cfg1;

    @Path("checkTotal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionData checkTotal() {
        try {
            return pilot.executeCommand(new CheckTotal());
        } catch (PilotException e) {
            TransactionData data = new TransactionData();
            data.setError(e);
            return data;
        }
    }

    @Path("sale")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionData sale(SaleRequest saleRequest) {

        //System.out.println("KOI8 " + Charset.isSupported("KOI8"));
        //System.out.println("KOI8-R " + Charset.isSupported("KOI8-R"));
        //System.out.println("cp866 " + Charset.isSupported("cp866"));

        try {
            return pilot.executeCommand(new Sale(saleRequest.getAmount(), saleRequest.getCardType(), saleRequest.getTrack2()));
        } catch (PilotException e) {
            TransactionData data = new TransactionData();
            data.setError(e);
            return data;
        }
    }

    @Path("returnSale")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionData returnSale(ReturnSaleRequest returnSaleRequest) {
        try {
            return pilot.executeCommand(new ReturnSale(returnSaleRequest.getAmount(), returnSaleRequest.getCardType(), returnSaleRequest.getTrack2()));
        } catch (PilotException e) {
            TransactionData data = new TransactionData();
            data.setError(e);
            return data;
        }
    }

    @Path("rollback")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionData rollback() {
        try {
            return pilot.executeCommand(new Rollback());
        } catch (PilotException e) {
            e.printStackTrace();
            TransactionData data = new TransactionData();
            data.setError(e);
            return data;
        }
    }

    @Path("readTrack2")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionData readTrack2(ReturnSaleRequest returnSaleRequest) {
        try {
            return pilot.executeCommand(new ReadTrack2());
        } catch (PilotException e) {
            TransactionData data = new TransactionData();
            data.setError(e);
            return data;
        }
    }

}