package ua.khai.gorbatiuk.taskmanager.util.converter.request;


import ua.khai.gorbatiuk.taskmanager.entity.bean.LoginUserBean;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToLoginBeanConverter implements Converter<HttpServletRequest, LoginUserBean> {
    @Override
    public LoginUserBean convert(HttpServletRequest source) {
        LoginUserBean loginUserBean = new LoginUserBean();

        loginUserBean.setEmail(source.getParameter(RequestParameter.USER_EMAIL));
        loginUserBean.setPassword(source.getParameter(RequestParameter.USER_PASS));

        return loginUserBean;
    }
}
