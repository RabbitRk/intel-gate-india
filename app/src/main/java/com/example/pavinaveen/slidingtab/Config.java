package com.example.pavinaveen.slidingtab;

public class Config {
    public static final String REGISTER_URL = "http://192.168.43.252:8080/IntelGate/addUser.php";
    public static final String WALLET_URL = "http://192.168.43.252:8080/IntelGate/addWallet.php";
    public static final String VEHICLE_LIST = "http://192.168.43.252:8080/IntelGate/getVehicles.php";
    public static final String SET_VEHICLE_LIST = "http://192.168.43.252:8080/IntelGate/addVehicles.php";


    public static final String KEY_USERNAME = "username";
    public static final String KEY_LISENCE = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "status";
}
