<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<li class="dropdown">
  <a class="dropdown-toggle" data-toggle="dropdown">Language
    <span class="caret"></span></a>
  <ul class="dropdown-menu">
    <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">En</a></li>
    <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Ru</a></li>
  </ul>
</li>