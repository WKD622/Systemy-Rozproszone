// **********************************************************************
//
// Copyright (c) 2003-2017 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.7.0
//
// <auto-generated>
//
// Generated from file `Bank.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package generated;

public interface Account extends com.zeroc.Ice.Object
{
    long getBalance(com.zeroc.Ice.Current current);

    long addIncome(long value, com.zeroc.Ice.Current current);

    long addOutcome(long value, com.zeroc.Ice.Current current);

    String getPesel(com.zeroc.Ice.Current current);

    String getAccountType(com.zeroc.Ice.Current current);

    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::generated::Account"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::generated::Account";
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getBalance(Account obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        long ret = obj.getBalance(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeLong(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addIncome(Account obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        long iceP_value;
        iceP_value = istr.readLong();
        inS.endReadParams();
        long ret = obj.addIncome(iceP_value, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeLong(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addOutcome(Account obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        long iceP_value;
        iceP_value = istr.readLong();
        inS.endReadParams();
        long ret = obj.addOutcome(iceP_value, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeLong(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getPesel(Account obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        String ret = obj.getPesel(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getAccountType(Account obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        String ret = obj.getAccountType(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    final static String[] _iceOps =
    {
        "addIncome",
        "addOutcome",
        "getAccountType",
        "getBalance",
        "getPesel",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
    };

    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_addIncome(this, in, current);
            }
            case 1:
            {
                return _iceD_addOutcome(this, in, current);
            }
            case 2:
            {
                return _iceD_getAccountType(this, in, current);
            }
            case 3:
            {
                return _iceD_getBalance(this, in, current);
            }
            case 4:
            {
                return _iceD_getPesel(this, in, current);
            }
            case 5:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 6:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 7:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 8:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
