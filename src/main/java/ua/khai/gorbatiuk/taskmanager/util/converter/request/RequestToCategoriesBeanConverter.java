package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoriesBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToCategoriesBeanConverter implements Converter<HttpServletRequest, CategoriesBean> {
    private static final String CURRENT_CATEGORY_ID = "currentCategoryId";
    public static final String NEW_CATEGORY_NAME = "newCategoryName";
    public static final String NEW_CATEGORY_COLOR = "newCategoryColor";


    @Override
    public CategoriesBean convert(HttpServletRequest source) throws ConverterException {
        User user = (User) source.getSession().getAttribute(RequestParameter.CURRENT_USER);

        CategoriesBean bean = new CategoriesBean();
        bean.setUser(user);

        setCurrentCategory(source, bean);

        return bean;
    }

    private void setCurrentCategory(HttpServletRequest source, CategoriesBean bean) {
        Category currentCategory = new Category();
        currentCategory.setName(source.getParameter(NEW_CATEGORY_NAME));
        currentCategory.setColor(source.getParameter(NEW_CATEGORY_COLOR));
        try {
            String currentCategoryId = source.getParameter(CURRENT_CATEGORY_ID);
            if (currentCategoryId != null) {
                currentCategory.setRootId(Integer.parseInt(currentCategoryId));
                bean.setCategory(currentCategory);
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | NullPointerException e) {
            currentCategory.setRootId(1);
            bean.setCategory(currentCategory);
        }
    }

}