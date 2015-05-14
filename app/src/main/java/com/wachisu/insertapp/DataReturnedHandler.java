package com.wachisu.insertapp;


public class DataReturnedHandler {

    private int _isSuccess;
    private String _returnedMessage;
    private String _returnedSessionUsername;
    private String _returnedSessionPassword;
    private String _returnedSessionDatabase;

    public int get_isSuccess() {
        return _isSuccess;
    }

    public void set_isSuccess(int _isSuccess) {
        this._isSuccess = _isSuccess;
    }

    public String get_returnedMessage() {
        return _returnedMessage;
    }

    public void set_returnedMessage(String _returnedMessage) {
        this._returnedMessage = _returnedMessage;
    }

    public String get_returnedSessionUsername() {
        return _returnedSessionUsername;
    }

    public void set_returnedSessionUsername(String _returnedSessionUsername) {
        this._returnedSessionUsername = _returnedSessionUsername;
    }

    public String get_returnedSessionPassword() {
        return _returnedSessionPassword;
    }

    public void set_returnedSessionPassword(String _returnedSessionPassword) {
        this._returnedSessionPassword = _returnedSessionPassword;
    }

    public String get_returnedSessionDatabase() {
        return _returnedSessionDatabase;
    }

    public void set_returnedSessionDatabase(String _returnedSessionDatabase) {
        this._returnedSessionDatabase = _returnedSessionDatabase;
    }
}