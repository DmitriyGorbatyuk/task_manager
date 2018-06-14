package ua.khai.gorbatiuk.taskmanager.web.servlet.categories;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoriesBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.web.servlet.task.TasksServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCategoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TasksServlet.class);

    private static final String CATEGORIES_PAGE = "jsp/categories.jsp";
    private static final String CATEGORIES_URI = "/categories";
    private static final String CATEGORIES_TO_TIME = "categoriesToTime";
    private static final int ROOT_TASK_ID = 1;

    private CategoryService categoryService;
    private Converter<HttpServletRequest, CategoriesBean> requestToCategoriesBeanConverter;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoriesBean categoriesBean = requestToCategoriesBeanConverter.convert(request);

        categoryService.update(categoriesBean.getCategory());
        response.sendRedirect(CATEGORIES_URI);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        requestToCategoriesBeanConverter = (Converter<HttpServletRequest, CategoriesBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_CATEGORIES_BEAN_CONVERTER);
    }

}
