package ua.khai.gorbatiuk.taskmanager.web.servlet.task;

import org.apache.log4j.Logger;

import ua.khai.gorbatiuk.taskmanager.entity.bean.EditTaskBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.CriticalUserDataException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.validator.EditTaskBeanValidator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class EditTaskServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditTaskServlet.class);

    private static final String TASKS_URI = "/task";

    private TaskService taskService;
    private CategoryService categoryService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;
    private Converter<HttpServletRequest, EditTaskBean> requestToEditTaskBeanConverter;
    private Converter<EditTaskBean, Task> editTaskBeanToTaskConverter;
    private EditTaskBeanValidator validator;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            editTask(request);
            response.sendRedirect(TASKS_URI);
        } catch (ConverterException e) {
            logger.debug(e);
            throw new CriticalUserDataException(e.getMessage());
        }
    }

    private void editTask(HttpServletRequest request) {
        EditTaskBean editedTask = requestToEditTaskBeanConverter.convert(request);
        Map<String, String> map = validator.validate(editedTask);
        if (map.isEmpty()) {
            try {
                updateTask(request, editedTask);
            } catch (WrongUserDataException e) {
                logger.debug(e);
                throw new CriticalUserDataException(e.getMessage());
            } catch (ServiceException e) {
                logger.debug(e);
            }
        } else {
            request.getSession().setAttribute("mapErrors", map);
        }
    }

    private void updateTask(HttpServletRequest request, EditTaskBean editedTask) {

        Task updatedTask = editTaskBeanToTaskConverter.convert(editedTask);
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);

        Category category = categoryService.getByUserIdAndCategoryId(tasksBean.getUser().getId(), editedTask.getFkCategory());
        updatedTask.setCategory(category);
        updatedTask.setUser(tasksBean.getUser());
        updatedTask.setId(tasksBean.getTask().getId());
        updatedTask.setIsLeaf(tasksBean.getTask().getIsLeaf());
        updatedTask.setRootId(tasksBean.getTask().getRootId());
        updatedTask.setChecked(tasksBean.getTask().getChecked());

        taskService.update(updatedTask);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        validator = (EditTaskBeanValidator) getServletContext().getAttribute(Attributes.EDIT_TASK_BEAN_VALIDATOR);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
        requestToEditTaskBeanConverter = (Converter<HttpServletRequest, EditTaskBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_EDIT_TASK_BEAN_CONVERTER);
        editTaskBeanToTaskConverter = (Converter<EditTaskBean, Task>)
                getServletContext().getAttribute(Attributes.EDIT_TASK_BEAN_TO_TASK_CONVERTER);
    }
}
