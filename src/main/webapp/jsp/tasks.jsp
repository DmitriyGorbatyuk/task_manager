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
    <div class="row">
        <div class="col-md-10">
            <h3>Tasks</h3>
        </div>
        <div class="col-md-2">
            <form method="post" action="/logout">
                <input class="btn btn-default" type="submit" value="Log out"/>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-default">
                <form action="/tasks" method="get">
                    <ul class="nav flex-column">
                        <c:if test="${neighbors[0].rootId ne 1}">
                            <li class="nav-item">
                                <a href="/tasks?currentTaskId=${neighbors[0].rootId}">Go back one level</a>
                            </li>
                        </c:if>
                        <c:forEach items="${neighbors}" var="neighbor">
                            <li class="nav-item">
                                <a href="/tasks?currentTaskId=${neighbor.id}"> ${neighbor.name}</a>
                            </li>
                        </c:forEach>

                        <form action="/tasks" method="post">
                            <input type="text" name="action" value="add" hidden/>
                            <div class="col-md-8">
                                <input type="text" name="newTaskName" placeholder="Name" class="form-control"/>
                            </div>
                            <div class="col-md-2">
                                <input type="submit" value="Add" class="btn btn-default"/>
                            </div>
                        </form>
                    </ul>
                </form>
            </div>


        </div>

        <div class="col-md-6">
            <div class="form-group">

                <form action="/tasks" method="post">
                    <input type="text" name="action" value="add" hidden/>
                    <div class="col-md-10">
                        <input type="text" name="newTaskName" placeholder="Name" class="form-control"/>
                    </div>
                    <div class="col-md-2">
                        <input type="submit" value="Add" class="btn btn-default"/>
                    </div>
                </form>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td></td>
                        <td>Name</td>
                        <td>Complexity</td>
                        <td>Category</td>
                        <td>Spent time</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty children}">
                            <c:forEach items="${children}" var="child">
                                <tr>

                                    <div class="checkbox">
                                        <td>
                                            <input type="checkbox" ${child.checked ? 'checked' : ''}>
                                        </td>
                                    </div>
                                    <td>
                                        <a href="/tasks?currentTaskId=${child.id}">${child.name}</a>
                                    </td>
                                    <td>${child.complexity}</td>
                                    <td>
                                        <div id="circle" style="background:#${child.category.color}"/>
                                    </td>
                                    <td>${child.time} c</td>
                                    <td><a href="">Start job</a></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center">Add new task</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    </tbody>
                </table>

            </div>
        </div>
        <div class="col-md-3">
            <c:if test="${not empty currentTask}">
                <form class="form-group">
                    <fieldset>
                        <legend>Edit task</legend>
                        <input type="text" name="taskId" value="${currentTask.id}" hidden/>
                        <input type="text" name="taskId" value="${currentTask.rootId}" hidden/>
                        <small class="text-muted">Task name:</small>
                        <br>
                        <input class="form-control" type="text" name="taskName" value="${currentTask.name}"/>
                        <small class="text-muted">Expiration date:</small>
                        <br>
                        <input class="form-control" type="datetime-local" name="taskDate" value="${currentTask.date}"/>
                        <small class="text-muted">Complexity:</small>
                        <br>
                        <input class="form-control" type="number" name="taskComplexity" min="0" max="10"
                               value="${currentTask.complexity}"/>
                        <small class="text-muted">Description:</small>
                        <br>
                        <textarea class="form-control" rows="5"
                                  name="taskDescription">${currentTask.description}</textarea>
                        <small class="text-muted">Spent time:</small>
                        <br>
                        <input class="form-control" type="number" name="taskTime" min="0" value="${currentTask.time}">
                        <small class="text-muted">Category:</small>
                        <select class="form-control" name="taskCategoryId">
                            <c:forEach items="${allCategories}" var="category">
                                <option value="${category.id}" ${category.id == currentTask.category.id ? 'selected' : ''}>${category.name}</option>
                            </c:forEach>

                        </select>
                        <br>
                        <input class="form-control btn btn-info" type="submit" value="Edit">
                        <br><br>
                        <input class="form-control btn btn-danger" type="submit" value="Delete">
                    </fieldset>
                </form>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>