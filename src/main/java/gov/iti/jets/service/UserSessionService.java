package gov.iti.jets.service;

import gov.iti.jets.dto.ChatDto;
import gov.iti.jets.dto.ContactDto;
import gov.iti.jets.dto.NotificationDto;
import gov.iti.jets.dto.UserSessionDto;
import gov.iti.jets.entity.*;
import gov.iti.jets.mapper.ChatMapper;
import gov.iti.jets.mapper.NotificationMapper;
import gov.iti.jets.mapper.UserMapper;
import gov.iti.jets.persistence.dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is service which responsible for sending session to user once he loged in
 * this session contain all user contacts data, chats data, notifications and user data
 * All these lists are sorted according to creation and modification date
 */
public class UserSessionService {
    private User user;
    private UserDao userDao;
    private ChatDao chatDao;
    private FriendsDao friendsDao;
    private ChatUserDao chatUserDao;
    private NotificationDao notificationDao;
    private UserMapper userMapper;
    private ChatMapper chatMapper;
    private NotificationMapper notificationMapper;
    private UserSessionDto userSessionDto;

    public UserSessionService(User user) {
        this.user = user;
        userDao = new UserDao();
        chatDao = new ChatDao();
        friendsDao = new FriendsDao();
        chatUserDao = new ChatUserDao();
        notificationDao = new NotificationDao();
        userMapper = new UserMapper();
        chatMapper = new ChatMapper();
        notificationMapper = new NotificationMapper();
    }

    public UserSessionDto getSessionDto() {
        //Get user chats from database and map it to dto then order by modified date
        List<ChatUser> chatUserList = chatUserDao.getChatsByUserId(user.getId());
        List<Chat> chatList = chatUserList.stream().map(x -> chatDao.findById(x.getChat_id())).toList();
        ArrayList<ChatDto> chatDtoList = chatList.stream().map(x -> chatMapper.toDTO(x))
                .sorted((x,y)-> x.getModified_on().compareTo(y.getModified_on()))
                .collect(Collectors.toCollection(ArrayList::new));

        //Get user contacts
        List<Friends> contactList = friendsDao.findAllById(user.getId());
        List<String> idsList = contactList.stream().map(x -> new ArrayList<String>(){{
                    add(x.getId1());
                    add(x.getId2());
                }})
                .flatMap(x -> x.stream()).toList();
        //Convert all contacts to set to remove duplicated user
        Set<User> userSet = idsList.stream().map(x -> userDao.findById(x)).collect(Collectors.toSet());
        //Map all contacts to dto
        ArrayList<ContactDto> userDtoList = userSet.stream().map(x -> userMapper.toContactDTO(x))
                .collect(Collectors.toCollection(ArrayList::new));

        //Get all user notification then order by created date
        ArrayList<NotificationDto> notificationList = notificationDao.getNotificationsByUserId(user.getId())
                .stream()
                .sorted((x,y)-> x.getCreated_on().compareTo(y.getCreated_on()))
                .map( x -> notificationMapper.toDTO(x))
                .collect(Collectors.toCollection(ArrayList::new));

        //Create session and return to user
        userSessionDto = UserSessionDto.builder()
                .user(userMapper.toDTO(user))
                .chatListDto(chatDtoList)
                .contactListDto(userDtoList)
                .notificationListDto(notificationList)
                .build();

        return userSessionDto;
    }
}
