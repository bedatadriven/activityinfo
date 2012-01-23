<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.apache.log4j.LogManager" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Arrays" %>
<% long beginPageLoadTime = System.currentTimeMillis();%>

<html>
<head>
    <title>Log4J Administration</title>
    <style type="text/css">

        <!--
        #content {
            margin: 0px;

            padding: 0px;
            text-align: center;
            background-color: #ccc;

            border: 1px solid #000;
            width: 100%;
        }

        body {
            position: relative;
            margin: 10px;

            padding: 0px;
            color: #333;
        }

        h1 {
            margin-top: 20px;
            font: 1.5em Verdana, Arial, Helvetica sans-serif;
        }

        h2 {
            margin-top: 10px;
            font: 0.75em Verdana, Arial, Helvetica sans-serif;
            text-align: left;
        }

        a, a:link, a:visited, a:active {
            color: red;
            text-decoration: none;
            text-transform: uppercase;
        }

        table {
            width: 100%;
            background-color: #000;
            padding: 3px;
            border: 0px;
        }

        th {
            font-size: 0.75em;
            background-color: #ccc;
            color: #000;
            padding-left: 5px;
            text-align: center;
            border: 1px solid #ccc;
            white-space: nowrap;

        }

        td {
            font-size: 0.75em;
            background-color: #fff;
            white-space: nowrap;

        }

        td.center {
            font-size: 0.75em;
            background-color: #fff;
            text-align: center;

            white-space: nowrap;
        }

        .filterForm {

            font-size: 0.9em;
            background-color: #000;
            color: #fff;
            padding-left: 5px;
            text-align: left;
            border: 1px solid #000;

            white-space: nowrap;
        }

        .filterText {

            font-size: 0.75em;
            background-color: #fff;
            color: #000;
            text-align: left;

            border: 1px solid #ccc;
            white-space: nowrap;

        }

        .filterButton {
            font-size: 0.75em;

            background-color: #000;
            color: #fff;

            padding-left: 5px;
            padding-right: 5px;

            text-align: center;
            border: 1px solid #ccc;

            width: 100px;
            white-space: nowrap;
        }

        -->
    </style>
</head>
<body onLoad="javascript:document.logFilterForm.logNameFilter.focus();">

<%
    String containsFilter = "Contains";
    String beginsWithFilter = "Begins With";

    String[] logLevels = {"debug", "info", "warn", "error", "fatal", "off"};
    String targetOperation = (String) request.getParameter("operation");
    String targetLogger = (String) request.getParameter("logger");
    String targetLogLevel = (String) request.getParameter("newLogLevel");
    String logNameFilter = (String) request.getParameter("logNameFilter");
    String logNameFilterType = (String) request.getParameter("logNameFilterType");

%>
<div id="content">
<h1>Log4J Administration</h1>

<div class="filterForm">

    <form action="log4jAdmin.jsp" name="logFilterForm">Filter Loggers:&nbsp;&nbsp;
        <input name="logNameFilter" type="text" size="50" value="<%=(logNameFilter == null ? "":logNameFilter)%>"

               class="filterText"/>
        <input name="logNameFilterType" type="submit" value="<%=beginsWithFilter%>" class="filterButton"/>&nbsp;

        <input name="logNameFilterType" type="submit" value="<%=containsFilter%>" class="filterButton"/>&nbsp;

        <input name="logNameClear" type="button" value="Clear" class="filterButton"

               onmousedown='javascript:document.logFilterForm.logNameFilter.value="";'/>
        <input name="logNameReset" type="reset" value="Reset" class="filterButton"/>

        <param name="operation" value="changeLogLevel"/>
    </form>
</div>

<table cellspacing="1">
    <tr>
        <th width="25%">Logger</th>

        <th width="25%">Parent Logger</th>
        <th width="15%">Effective Level</th>

        <th width="35%">Change Log Level To</th>
    </tr>

    <%
        Enumeration loggers = LogManager.getCurrentLoggers();

        HashMap loggersMap = new HashMap(128);
        Logger rootLogger = LogManager.getRootLogger();

        if (!loggersMap.containsKey(rootLogger.getName())) {

            loggersMap.put(rootLogger.getName(), rootLogger);
        }

        while (loggers.hasMoreElements()) {
            Logger logger = (Logger) loggers.nextElement();

            if (logNameFilter == null || logNameFilter.trim().length() == 0) {

                loggersMap.put(logger.getName(), logger);
            } else if (containsFilter.equals(logNameFilterType)) {

                if (logger.getName().toUpperCase().indexOf(logNameFilter.toUpperCase()) >= 0) {

                    loggersMap.put(logger.getName(), logger);
                }

            } else {
// Either was no filter in IF, contains filter in ELSE IF, or begins with in ELSE
                if (logger.getName().startsWith(logNameFilter)) {

                    loggersMap.put(logger.getName(), logger);
                }

            }
        }
        Set loggerKeys = loggersMap.keySet();

        String[] keys = new String[loggerKeys.size()];

        keys = (String[]) loggerKeys.toArray(keys);

        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.length; i++) {

            Logger logger = (Logger) loggersMap.get(keys[i]);

// MUST CHANGE THE LOG LEVEL ON LOGGER BEFORE GENERATING THE LINKS AND THE
// CURRENT LOG LEVEL OR DISABLED LINK WON'T MATCH THE NEWLY CHANGED VALUES
            if ("changeLogLevel".equals(targetOperation) && targetLogger.equals(logger.getName())) {

                Logger selectedLogger = (Logger) loggersMap.get(targetLogger);

                selectedLogger.setLevel(Level.toLevel(targetLogLevel));
            }

            String loggerName = null;
            String loggerEffectiveLevel = null;
            String loggerParent = null;
            if (logger != null) {
                loggerName = logger.getName();
                loggerEffectiveLevel = String.valueOf(logger.getEffectiveLevel());
                loggerParent = (logger.getParent() == null ? null : logger.getParent().getName());

            }
    %>
    <tr>
        <td><%=loggerName%>
        </td>

        <td><%=loggerParent%>
        </td>
        <td><%=loggerEffectiveLevel%>

        </td>
        <td class="center">
            <%
                for (int cnt = 0; cnt < logLevels.length; cnt++) {

                    String url = "log4jAdmin.jsp?operation=changeLogLevel&logger=" + loggerName + "&newLogLevel=" + logLevels[cnt] + "&logNameFilter=" + (logNameFilter != null ? logNameFilter : "") + "&logNameFilterType=" + (logNameFilterType != null ? logNameFilterType : "");

                    if (logger.getLevel() == Level.toLevel(logLevels[cnt]) || logger.getEffectiveLevel() == Level.toLevel(logLevels[cnt])) {

            %>
            [<%=logLevels[cnt].toUpperCase()%>]

            <%
            } else {
            %>
            <a href='<%=url%>'>[<%=logLevels[cnt]%>]</a>&nbsp;

            <%
                    }
                }
            %>
        </td>
    </tr>

    <%
        }
    %>
</table>
<h2>
    Revision: 1.0<br/>
    Page Load Time (Millis): <%=(System.currentTimeMillis() - beginPageLoadTime)%>
</h2>
</div>
</body>
</html>
