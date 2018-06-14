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

public class CategoriesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TasksServlet.class);

    private static final String CATEGORIES_PAGE = "jsp/categories.jsp";
    private static final String CATEGORIES_URI = "/categories";
    private static final String CATEGORIES_TO_TIME = "categoriesToTime";
    private static final int ROOT_TASK_ID = 1;

    private TaskService taskService;
    private CategoryService categoryService;
    private Converter<HttpServletRequest, CategoriesBean> requestToCategoriesBeanConverter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        replaceParametersFromSession(request);
        putCategories(request);

        request.getRequestDispatcher(CATEGORIES_PAGE).forward(request, response);
    }

    private void replaceParametersFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

    }

    private void putCategories(HttpServletRequest request) {
        try {
            CategoriesBean categoriesBean = requestToCategoriesBeanConverter.convert(request);
            List<Category> categories = categoryService.getAllByUserIdAndCategoryRootId(categoriesBean.getUser().getId(), categoriesBean.getCategory().getRootId());
            List<Task> tasks = taskService.getAllByUserId(categoriesBean.getUser().getId());

            Map<Category, Integer> categoryToTimeMap = calculateCategoryTime(categories, tasks);
            request.setAttribute(CATEGORIES_TO_TIME, categoryToTimeMap);
        } catch (WrongUserDataException e) {}
    }

    private Map<Category, Integer> calculateCategoryTime(List<Category> categories, List<Task> tasks) {
        Map<Category, Integer> categoryToTimeMap = new HashMap<>();
        categories.stream().forEach(item -> categoryToTimeMap.put(item, 0));
        tasks.stream().forEach(task ->
                categoryToTimeMap.put(task.getCategory(), task.getTime() + categoryToTimeMap.get(task.getCategory())));
        return categoryToTimeMap;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        requestToCategoriesBeanConverter = (Converter<HttpServletRequest, CategoriesBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_CATEGORIES_BEAN_CONVERTER);
    }
}
