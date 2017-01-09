package com.example.wladek.pockeregapp.util;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wladek on 9/2/16.
 */
public class LoginRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = StringUtils.SERVER_URL+"/admin_auth";
    private Map<String , String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST , REGISTER_REQUEST_URL , listener , null);
        params = new HashMap<>();
        params.put("username" , username);
        params.put("password" , password);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}