package gov.iti.jets.common.network.client;

import gov.iti.jets.common.dto.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IClient extends Remote {
    public void receive() throws RemoteException;
    public void receiveMessage(long chatId, MessageDto messageDto) throws RemoteException;
    public void addToGroup(String userId, long chatId) throws RemoteException;
    public void removefromGroup(String userId, long chatId) throws RemoteException;
    public void addFriend(ArrayList<ContactDto> contactDtoList) throws RemoteException;
    public void removeFriend(ContactDto contactDto) throws RemoteException;
    public void editUser(ContactDto contactDto) throws RemoteException;
    public void readFile(long chatId, String senderId, byte[] bytes ,String fileName ) throws RemoteException;
    public void receiveInvitation(InvitationDto invitationDto) throws RemoteException;
    public void addNewSessetion(UserSessionDto userSessionDto) throws RemoteException;
    public void receiveMessageBot(long chatId, MessageDto messageDto, String messageFromBot) throws RemoteException ;
    public void receiveAnnouncement(String message) throws RemoteException ;

    }