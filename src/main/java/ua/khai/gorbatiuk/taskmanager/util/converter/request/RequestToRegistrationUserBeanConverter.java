package ua.khai.gorbatiuk.taskmanager.util.converter.request;


import ua.khai.gorbatiuk.taskmanager.entity.bean.RegistrationUserBean;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToRegistrationUserBeanConverter implements Converter<HttpServletRequest, RegistrationUserBean> {
    @Override
    public RegistrationUserBean convert(HttpServletRequest source){
        RegistrationUserBean registrationUserBean = new RegistrationUserBean();

        registrationUserBean.setEmail(source.getParameter(RequestParameter.USER_EMAIL));
        registrationUserBean.setPassword(source.getParameter(RequestParameter.USER_PASS));
        registrationUserBean.setConfirmedPassword(source.getParameter(RequestParameter.USER_CONFIRM_PASS));

        return registrationUserBean;
    }
}
