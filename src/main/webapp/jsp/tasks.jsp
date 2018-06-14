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
                <li class="active"><a href="/tasks">All task</a></li>
                <li ><a href="/todaysTasks">Today</a></li>
                <li><a href="/categories">Categories</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form method="post" action="/logout">
                        <input class="btn navbar-btn " type="submit" value="Log out"/>
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
                            <a href="/tasks?currentTaskId=${neighbors[0].rootId}">Go back one level</a>
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
                            <input type="text" name="newTaskName" placeholder="Name" class="form-control" maxlength="44"
                                   required/>
                        </div>
                        <div class="col-md-2">
                            <input type="submit" value="Add" class="btn btn-default"/>
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
                                <input type="text" name="newTaskName" placeholder="Name" class="form-control"
                                       maxlength="44" required/>
                            </div>
                            <div class="col-md-2">
                                <input type="submit" value="Add" class="btn btn-default"/>
                            </div>
                        </form>


                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <td></td>
                                <td><a href="/tasks?sort=name"><h6>Name</h6></a></td>
                                <td><a href="/tasks?sort=complexity"><h6>Complexity</h6></a></td>
                                <td><a href="/tasks?sort=category"><h6>Category</h6></a></td>
                                <td><a href="/tasks?sort=time"><h6>Spent time</h6></a></td>
                                <td><a href="/tasks?sort=none"><h6>Without sorting</h6></a></td>
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
                                                    <div class="circle" style="background:#${child.category.color}"/>
                                                </td>
                                                <td>${child.formattedTime}</td>
                                                <td class="col-md-2">
                                                    <c:if test="${child.isLeaf}">
                                                        <form action="/executeTask" method="post">
                                                            <input type="text" name="executingTaskBeanId"
                                                                   value="${child.id}" hidden>
                                                            <c:choose>
                                                                <c:when test="${child.id eq executingTask.id}">
                                                                    <input class="form-control" type="submit"
                                                                           value="Finish task">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input class="form-control" type="submit"
                                                                           value="Start task">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:if test="${child.checked}">
                                            <tr>
                                                <small>
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
                                                        <del>
                                                            <a href="/tasks?currentTaskId=${child.id}">${child.name}</a>
                                                        </del>
                                                    </td>

                                                    <td>${child.complexity}</td>
                                                    <td>
                                                        <div id="circle" style="background:#${child.category.color}"/>
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
                                        <td colspan="6" class="text-center">There is no subtasks. You can add new one</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>

                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <h4 class="text-center">Choose any task</h4>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="col-md-3">
            <c:if test="${not empty currentTask}">
                <form class="form-group" action="/editTask" method="post">
                    <fieldset>
                        <legend>Edit task</legend>
                        <small class="text-muted">Task name:</small>
                        <br>
                        <input class="form-control" type="text" name="taskName" value="${currentTask.name}"/>
                        <small class="text-muted">Expiration date:</small>
                        <br>
                        <input class="form-control" type="datetime-local" step="60" name="taskDate"
                               value="${currentTask.date}"/>
                        <small class="text-muted">Complexity:</small>
                        <br>
                        <input class="form-control" type="number" name="taskComplexity" min="0" max="10"
                               value="${currentTask.complexity}"/>
                        <small class="text-muted">Description:</small>
                        <br>
                        <textarea class="form-control" rows="5"
                                  name="taskDescription">${currentTask.description}</textarea>
                        <small class="text-muted">Spent time(seconds):</small>
                        <br>
                        <input class="form-control" type="number" name="taskTime" min="0"
                               value="${currentTask.time}" ${currentTask.isLeaf ? '' : 'disabled'}>
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
<script>
    <%@include file="/static/js/stopwatch.js" %>
</script>
</body>
</html>