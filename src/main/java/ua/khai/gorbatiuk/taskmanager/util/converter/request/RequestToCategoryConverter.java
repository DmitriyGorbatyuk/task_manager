package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoriesBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToCategoryConverter implements Converter<HttpServletRequest, Category> {
    private static final String CURRENT_CATEGORY_ID = "currentCategoryId";
    private static final String NEW_CATEGORY_NAME = "newCategoryName";
    private static final String NEW_CATEGORY_COLOR = "newCategoryColor";


    @Override
    public Category convert(HttpServletRequest source) throws ConverterException {
        User user = (User) source.getSession().getAttribute(RequestParameter.CURRENT_USER);

        Category category = new Category();
        category.setUser(user);

        setCurrentCategory(source, category);

        return category;
    }

    private void setCurrentCategory(HttpServletRequest source, Category category) {
        category.setName(source.getParameter(NEW_CATEGORY_NAME));
        category.setColor(source.getParameter(NEW_CATEGORY_COLOR));
        try {
            String currentCategoryId = source.getParameter(CURRENT_CATEGORY_ID);
            if (currentCategoryId != null) {
                category.setId(Integer.parseInt(currentCategoryId));
            } else {
                Category currentCategory = (Category)source.getSession().getAttribute("currentCategory");
                if(currentCategory != null) {
                    category.setRootId(currentCategory.getRootId());
                    category.setId(currentCategory.getId());
                } else {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            category.setId(1);
        }
    }

}