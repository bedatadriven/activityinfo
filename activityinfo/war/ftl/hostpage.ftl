<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="gwt:property" content="locale=${user.locale}"/>
    <title>ActivityInfo</title>
    <style type="text/css">
        #loading {
            position: absolute; left: 45%; top: 40%; padding: 2px;
            margin-left:-45px; z-index:20001; border:1px solid #ccc;
        }
        #loading .loading-indicator {
            background:#eef;font:bold 13px tahoma,arial,helvetica;
            padding:10px;margin:0;height:auto;color:#444;
        }
        #loading .loading-indicator img {
            margin-right:8px; float:left; vertical-align:top;
        }
        #loading-msg {font:normal 10px arial,tahoma,sans-serif;}
    </style>
    <script type="text/javascript">
        var UserInfo = {
            authToken: "${id}",
            email: "${user.email}"
        };
    </script>

    <script language='javascript' src='Application/gxt/flash/swfobject.js'></script>
    <script type="text/javascript" language="javascript" src="Application/Application.nocache.js"></script>
    <link rel="stylesheet" type="text/css" href="Application/gxt/css/gxt-all.css" />
    <link rel="stylesheet" type="text/css" href="report.css" />

</head>
<body>
<div id="loading">
    <div class="loading-indicator">
        <img src="Application/gxt/images/default/shared/large-loading.gif" alt=""/>
        ActivityInfo v0.3<br />
        <span id="loading-msg">Chargement en cours...</span>
    </div>
</div>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
<iframe src="javascript:''" name="_downloadFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
</body>
</html>
