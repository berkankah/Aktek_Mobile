package com.aktekbilisim.modals;

import java.io.Serializable;

/**
 * Created by berkan.kahyaoglu on 24.06.2015.
 */
public class LoginDTO implements Serializable {
    private String retcode;
    private String msg;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
