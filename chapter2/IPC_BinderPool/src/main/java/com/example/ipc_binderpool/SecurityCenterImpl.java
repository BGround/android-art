package com.example.ipc_binderpool;

import android.os.RemoteException;

public class SecurityCenterImpl extends ISecurityCenter.Stub {

//    private static final char SECRET_CODE = '^';
    private static final char SECRET_CODE = 1;

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
//            chars[i] ^= SECRET_CODE;
            chars[i] += SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
//        return encrypt(password);
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
//            chars[i] ^= SECRET_CODE;
            chars[i] -= SECRET_CODE;
        }
        return new String(chars);
    }
}
