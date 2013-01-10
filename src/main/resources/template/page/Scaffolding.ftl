<#macro scaffolding title>

<!DOCTYPE html>
<html>
<head>
  <title>ActivityInfo - Login</title>
  <!-- CSS Files -->
  <link href="css/bootstrap.css" rel="stylesheet" media="screen">
  <link href="css/bootstrap-responsive.css" rel="stylesheet" media="screen">
  <link href="css/style.css" rel="stylesheet" media="screen">
</head>
<body>
    <header role="banner">
    
      <div id="inner-header" class="clearfix">
        
        <div class="navbar navbar-static-top">
          <div class="navbar-inner">
            <div class="container-fluid nav-container">
              <nav role="navigation">
                <a class="brand" href="#" style=""><img src="img/logo-shadow.png"><span>ActivityInfo</span></a>
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                </a>
               
              </nav>
            </div>
          </div>
  		</div><!-- /.navbar -->
      
      </div> <!-- end #inner-header -->
    
    </header> <!-- end header -->

	<#nested>

	  
</body>
</html>
</#macro>

<#macro content>
    <div class="container-fluid">
		<div id="content" class="clearfix row-fluid">

		<#nested>
		
	  
    	</div> <!-- end #content -->
	</div>
</#macro>

<#macro scripts>
  <script src="http://code.jquery.com/jquery-latest.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <#nested>
</#macro>