package com.example.ipc_binderpool;

import android.os.RemoteException;

public class ComputerImpl extends IComputer.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
