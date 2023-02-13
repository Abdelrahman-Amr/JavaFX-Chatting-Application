package gov.iti.jets.client.network.service;


import gov.iti.jets.client.Dina.MyID;
import gov.iti.jets.client.callBack.Martinily;
import gov.iti.jets.common.dto.UserSessionDto;
import gov.iti.jets.common.network.server.UserRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class LoginService {
    public UserSessionDto login(String phone, String password){
        //Add current user ID
        MyID.getInstance(phone,password);

        UserSessionDto userSessionDto = null;
        try {
            UserRemote obj = RMIManager.lookUpRegister();
            System.out.println(obj);
            userSessionDto = obj.login(phone,password, new Martinily());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        return userSessionDto;
    }
}
