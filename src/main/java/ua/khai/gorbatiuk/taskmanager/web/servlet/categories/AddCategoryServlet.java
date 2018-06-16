package ua.khai.gorbatiuk.taskmanager.web.servlet.categories;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoriesBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.web.servlet.task.TasksServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddCategoryServlet extends HttpServlet {


    private static final Logger logger = Logger.getLogger(TasksServlet.class);

    private static final String CATEGORIES_PAGE = "jsp/categories.jsp";
    private static final String CATEGORIES_URI = "/categories";
    private static final String CATEGORIES_TO_TIME = "categoriesToTime";
    private static final int ROOT_TASK_ID = 1;

    private CategoryService categoryService;
    private Converter<HttpServletRequest, Category> requestToCategoryConverter;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category currentCategory = requestToCategoryConverter.convert(request);

        Category newCategory = new Category();
        newCategory.setName(currentCategory.getName());
        newCategory.setRootId(currentCategory.getId());
        newCategory.setUser(currentCategory.getUser());

        categoryService.add(newCategory);
        response.sendRedirect(CATEGORIES_URI);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        requestToCategoryConverter = (Converter<HttpServletRequest, Category>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_CATEGORY_CONVERTER);
    }

}
