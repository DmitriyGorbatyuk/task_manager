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
                <li><a href="/tasks"><fmt:message key="menu.all_tasks"/></a></li>
                <li class="active"><a href="/todaysTasks"><fmt:message key="menu.today"/></a></li>
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

        <div class="col-md-2">
        </div>

        <div class="col-md-8">
            <div class="form-group">
                <c:choose>
                    <c:when test="${not empty datedTasks}">

                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <td></td>
                                <td><a href="/todaysTasks?sort=name"><h6><fmt:message key="table.name"/></h6></a></td>
                                <td><a href="/todaysTasks?sort=complexity"><h6><fmt:message key="table.complexity"/></h6></a></td>
                                <td><a href="/todaysTasks?sort=category"><h6><fmt:message key="table.category"/></h6></a></td>
                                <td><a href="/todaysTasks?sort=time"><h6><fmt:message key="table.time"/></h6></a></td>
                                <td><a href="/todaysTasks?sort=none"><h6><fmt:message key="table.none"/></h6></a></td>
                                <td><a href="/todaysTasks?sort=date"><h6><fmt:message key="edittask.date"/></h6></a></td>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${datedTasks}" var="child">
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
                                                <form action="/executeTask" method="post">
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
                                        <td>${child.date.dayOfMonth} ${child.date.month} ${child.date.hour}:${child.date.minute}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <h4 class="text-center"><fmt:message key="today.no"/></h4>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="col-md-2">
        </div>

    </div>
</div>
</body>
</html>