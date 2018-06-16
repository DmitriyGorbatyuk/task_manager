package ua.khai.gorbatiuk.taskmanager.web.servlet.categories;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoriesBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.CategoryTimeBean;
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
import java.util.*;
import java.util.stream.Collectors;

public class CategoriesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TasksServlet.class);

    private static final String CATEGORIES_PAGE = "jsp/categories.jsp";
    private static final String CATEGORIES_URI = "/categories";
    private static final String CATEGORIES_TO_TIME = "categoriesToTime";
    private static final int ROOT_TASK_ID = 1;
    private static final String CURRENT_CATEGORY = "currentCategory";

    private TaskService taskService;
    private CategoryService categoryService;
    private Converter<HttpServletRequest, Category> requestToCategoryConverter;

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
            Category category = requestToCategoryConverter.convert(request);
            setCurrentCategory(request, category);
            List<Category> allCategories = categoryService.getAllByUserId(category.getUser().getId());
            List<Task> tasks = taskService.getAllByUserId(category.getUser().getId());

            List<CategoryTimeBean> categoryTimeBeans = calculateCategoryTime(allCategories, tasks);

            readTimeRecursively(
                    categoryTimeBeans.stream()
                    .min(Comparator.comparing(a -> a.getCategory().getId()))
                    .get(),
                    categoryTimeBeans);

            request.setAttribute(CATEGORIES_TO_TIME,
                    categoryTimeBeans.stream()
                            .filter(item -> item.getCategory().getRootId().equals(category.getId()))
                            .collect(Collectors.toList()));
        } catch (WrongUserDataException e) {
            logger.debug(e.getMessage());
        }
    }

    private void setCurrentCategory(HttpServletRequest request, Category category) {
        if(category.getId() != 1) {
            Category currentCategory = categoryService.getByUserIdAndCategoryId(category.getUser().getId(), category.getId());
            request.getSession().setAttribute(CURRENT_CATEGORY, currentCategory);
        } else {
            request.getSession().setAttribute(CURRENT_CATEGORY, category);
        }
    }

    private List<CategoryTimeBean> calculateCategoryTime(List<Category> categories, List<Task> tasks) {
        List<CategoryTimeBean> categoryTimeBeans = new ArrayList<>(categories.size());

        categories.forEach(item -> categoryTimeBeans.add(new CategoryTimeBean(item, 0)));

        tasks.stream()
                .filter(task -> categories.contains(task.getCategory()))
                .forEach(task -> categoryTimeBeans.stream()
                    .filter(categoryTimeBean -> categoryTimeBean.getCategory().equals(task.getCategory()))
                    .findFirst().get().addTime(task.getTime()));

        return categoryTimeBeans;
    }

    private void readTimeRecursively(CategoryTimeBean currentCategory, List<CategoryTimeBean> all) {
        List<CategoryTimeBean>  children = all.stream()
                .filter(item -> item.getCategory().getRootId().equals(currentCategory.getCategory().getId()))
                .collect(Collectors.toList());
        for (CategoryTimeBean child : children) {
            readTimeRecursively(child, all);
        }
        if (!children.isEmpty()) {
            currentCategory.setTime(children.stream().mapToInt(CategoryTimeBean::getTime).sum());
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        requestToCategoryConverter = (Converter<HttpServletRequest, Category>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_CATEGORY_CONVERTER);
    }
}
