package cn.comtom.linkage.main.access.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccessService {


    void service(HttpServletRequest request, HttpServletResponse response);

}
