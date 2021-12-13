package com.appwxx.konsulid.app;

public class EndPoints {

    // localhost url
    public static final String BASE_URL = "http://konsulid.razha.co/modul/chat.php";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = "http://konsulid.razha.co/modul/chathistory.php?id=_ID_";
    public static final String CHAT_ROOM_MESSAGE = "http://konsulid.razha.co/modul/chatadd.php?id=_ID_";
}
