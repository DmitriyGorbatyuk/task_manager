<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--===========================================================================
JSTL core tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--===========================================================================
JSTL functions tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--===========================================================================
JSTL i18n tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Tasks</title>

    <style>
        <%@include file="/static/css/bootstrap.min.css" %>
        <%@include file="/static/css/style.css"%>
        <%@include file="/static/js/jquery.min.js" %>
        <%@include file="/static/js/bootstrap.min.js" %>
    </style>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<div class="container-fluid">
    <br>

    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/tasks">Task manager</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="/tasks"><fmt:message key="menu.all_tasks"/></a></li>
                <li><a href="/todaysTasks"><fmt:message key="menu.today"/></a></li>
                <li><a href="/datedTasks"><fmt:message key="menu.schedule"/></a></li>
                <li><a href="/categories"><fmt:message key="menu.categories"/></a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">

                <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">En</a></li>
                <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Ru</a></li>

                <li>
                    <form method="post" action="/logout">
                        <input class="btn navbar-btn " type="submit" value="<fmt:message key="menu.logout"/>"/>
                        <br>
                    </form>
                </li>
            </ul>
        </div>
    </nav>
    <div class="row">

        <div class="col-md-3">
            <div class="panel panel-default">
                <ul class="nav flex-column ">

                    <c:if test="${neighbors[0].rootId ne 1}">
                        <li class="nav-item">
                            <a href="/tasks?currentTaskId=${neighbors[0].rootId}"><fmt:message key="back_one_lvl"/></a>
                        </li>
                    </c:if>
                    <c:forEach items="${neighbors}" var="neighbor">
                        <li>
                            <a href="/tasks?currentTaskId=${neighbor.id}"
                               class=${neighbor.id eq currentTask.id ? 'bg-primary' : ''}> ${neighbor.name}</a>
                        </li>
                    </c:forEach>
                    <br>
                    <form action="/addTasks" method="post">
                        <input type="text" name="position" value="neighbors" hidden/>
                        <div class="col-md-8">
                            <input type="text" name="newTaskName" placeholder="<fmt:message key="add.placeholder"/>" class="form-control" maxlength="44"
                                   required/>
                        </div>
                        <div class="col-md-2">
                            <input type="submit" value="<fmt:message key="add.button"/>" class="btn btn-default"/>
                        </div>
                    </form>
                    <br><br>
                </ul>
            </div>


        </div>

        <div class="col-md-6">
            <div class="form-group">
                <c:choose>
                    <c:when test="${not empty currentTask}">
                        <form action="/addTasks" method="post">
                            <input type="text" name="position" value="children" hidden/>
                            <div class="col-md-10">
                                <input type="text" name="newTaskName" placeholder="<fmt:message key="add.placeholder"/>" class="form-control"
                                       maxlength="44" required/>
                            </div>
                            <div class="col-md-2">
                                <input type="submit" value="<fmt:message key="add.button"/>" class="btn btn-default"/>
                            </div>
                        </form>


                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <td></td>
                                <td><a href="/tasks?sort=name"><h6><fmt:message key="table.name"/></h6></a></td>
                                <td><a href="/tasks?sort=complexity"><h6><fmt:message key="table.complexity"/></h6></a></td>
                                <td><a href="/tasks?sort=category"><h6><fmt:message key="table.category"/></h6></a></td>
                                <td><a href="/tasks?sort=time"><h6><fmt:message key="table.time"/></h6></a></td>
                                <td><a href="/tasks?sort=none"><h6><fmt:message key="table.none"/></h6></a></td>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty children}">
                                    <c:forEach items="${children}" var="child">
                                        <c:if test="${not child.checked}">
                                            <tr>
                                                <div class="checkbox">
                                                    <td>
                                                        <c:if test="${child.isLeaf}">
                                                            <form action="/checkTask" method="post">
                                                                <input type="text" name="executingTaskBeanId"
                                                                       value="${child.id}" hidden>
                                                                <input type="checkbox" ${child.checked ? 'checked' : ''}
                                                                       onclick="this.form.submit()">
                                                            </form>
                                                        </c:if>
                                                    </td>
                                                </div>
                                                <td>
                                                    <a href="/tasks?currentTaskId=${child.id}">${child.name}</a>
                                                </td>

                                                <td>${child.complexity}</td>
                                                <td>
                                                    <div class="circle" style="background:${child.category.color}"/>
                                                </td>
                                                <td>${child.formattedTime}</td>
                                                <td class="col-md-2">
                                                    <c:if test="${child.isLeaf}">
                                                        <form action="/executeTask" method="post" style="margin:0px">
                                                            <input type="text" name="executingTaskBeanId"
                                                                   value="${child.id}" hidden>
                                                            <c:choose>
                                                                <c:when test="${child.id eq executingTask.id}">
                                                                    <input class="form-control" type="submit"
                                                                           value="<fmt:message key="table.finish_task"/>">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input class="form-control" type="submit"
                                                                           value="<fmt:message key="table.start_task"/>">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:if test="${child.checked}">
                                            <tr>
                                                <div class="checkbox">
                                                    <td>
                                                        <form action="/checkTask" method="post">
                                                            <input type="text" name="executingTaskBeanId"
                                                                   value="${child.id}" hidden>
                                                            <input type="checkbox" ${child.checked ? 'checked' : ''}
                                                                   onclick="this.form.submit()">
                                                        </form>
                                                    </td>
                                                </div>
                                                <td>
                                                    <del>${child.name}</del>
                                                </td>

                                                <td>${child.complexity}</td>
                                                <td>
                                                    <div class="circle" style="background:${child.category.color}"/>
                                                </td>
                                                <td>${child.formattedTime}</td>
                                                <td class="col-md-2">
                                                </td>
                                            </tr>
                                        </c:if>

                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center"><fmt:message key="no_subtasks"/>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>

                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <h4 class="text-center"><fmt:message key="choose_task"/></h4>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="col-md-3">
            <c:if test="${not empty currentTask}">

                <fieldset>
                    <form class="form-group" action="/editTask" method="post">
                        <legend><fmt:message key="edittask.edit"/></legend>
                        <small class="text-muted"><fmt:message key="edittask.name"/>:</small>
                        <br>
                        <input class="form-control" type="text" name="taskName" value="${currentTask.name}"/>
                        <small class="text-muted"><fmt:message key="edittask.date"/>:</small>
                        <br>
                        <input class="form-control" type="datetime-local" step="60" name="taskDate"
                               value="${currentTask.date}"/>
                        <small class="text-muted"><fmt:message key="edittask.repeat"/>:</small>
                        <br>
                        <input class="form-control" type="number" name="repeatAfter" min="1" max="365"
                               value="${currentTask.repeatAfter}"/>
                        <small class="text-muted"><fmt:message key="edittask.complexity"/>:</small>
                        <br>
                        <input class="form-control" type="number" name="taskComplexity" min="0" max="10"
                               value="${currentTask.complexity}"/>
                        <small class="text-muted"><fmt:message key="edittask.description"/>:</small>
                        <br>
                        <textarea class="form-control" rows="5"
                                  name="taskDescription">${currentTask.description}</textarea>
                        <small class="text-muted"><fmt:message key="edittask.time"/>:</small>
                        <br>
                        <input class="form-control" type="number" name="taskTime" min="0"
                               value="${currentTask.time}" ${currentTask.isLeaf ? '' : 'disabled'}>
                        <small class="text-muted"><fmt:message key="edittask.category"/>:</small>
                        <select class="form-control" name="taskCategoryId">
                            <c:forEach items="${allCategories}" var="category">
                                <option value="${category.id}" ${category.id == currentTask.category.id ? 'selected' : ''}>${category.name}</option>
                            </c:forEach>

                        </select>
                        <br>
                        <input class="form-control btn btn-info" type="submit" value="<fmt:message key="edittask.edit"/>">
                    </form>
                    <form class="form-group" action="/deleteTask" method="post">

                        <input class="form-control btn btn-danger" type="submit" value="<fmt:message key="edittask.delete"/>">
                    </form>
                </fieldset>
            </c:if>
        </div>

    </div>
</div>
</body>
</html>