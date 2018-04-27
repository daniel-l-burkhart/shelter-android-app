package com.example.shelterconnect.database;

/**
 * Database API that contains URL and API calls
 * Created by daniel on 2/23/18.
 */

public class Api {

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;
    private static final String ROOT_URL = "http://codd.cs.gsu.edu/~dburkhart1/Api.php?apicall=";

    public static final String URL_CREATE_ITEM = ROOT_URL + "createitem";
    public static final String URL_READ_ITEMS = ROOT_URL + "getitems";
    public static final String URL_UPDATE_ITEM = ROOT_URL + "updateitem";
    public static final String URL_DELETE_ITEM = ROOT_URL + "deleteitem&itemID=";

    public static final String URL_CREATE_EMPLOYEE = ROOT_URL + "createworker";
    public static final String URL_READ_EMPLOYEE = ROOT_URL + "getworkers";
    public static final String URL_UPDATE_EMPLOYEE = ROOT_URL + "updateworker";
    public static final String URL_DELETE_EMPLOYEE = ROOT_URL + "deleteworker&workerID=";

    public static final String URL_CREATE_REQUEST = ROOT_URL + "createrequest";
    public static final String URL_READ_REQUESTS = ROOT_URL + "getrequests";
    public static final String URL_UPDATE_REQUEST = ROOT_URL + "updaterequest";
    public static final String URL_DELETE_REQUEST = ROOT_URL + "deleterequest&rID=";

    public static final String URL_CREATE_DONOR = ROOT_URL + "createdonor";
    public static final String URL_READ_DONORS = ROOT_URL + "getdonors";
    public static final String URL_UPDATE_DONOR = ROOT_URL + "updatedonor";
    public static final String URL_DELETE_DONOR = ROOT_URL + "deletedonor&id=";

    public static final String URL_CREATE_DONATION = ROOT_URL + "createdonation";

    public static final String URL_READ_USERS = ROOT_URL + "getusers";

    public static final String URL_GET_DONORID_FROM_EMAIL = ROOT_URL + "getDonorID&donorEmail=";

    public static final String URL_GET_DONATIONS_FOR_USER = ROOT_URL + "getDonationsForUser&donorEmail=";

}
