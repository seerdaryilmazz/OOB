<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type"  content="text/html; charset=UTF-8" />
    <style>
		body {
	        font-family: Arial;
	        font-style: normal;
	        font-size: 12px;
	    }
    </style>
</head>
<body>
	<div>
		${greetingMessage}
	</div>
	</br></br>
	<span style="page-break-before: always;"/>
	
	<table class="a4Page" style="width: 100%">
    	<tbody>
		    <tr>
		        <td class="a4Page_cell">
		            <table style="padding: 0px; border-spacing: 0; width: 60%;" border="1" cellpadding="5px" style="border-collapse: collapse;">
		            	<tbody>
		                <#list detailsTableRows as row>
		                    <#if row?index == 0>
		                        <#if row.hasDifferentBackgroundColor>
		                            <tr>
		                                <td style="width: 40%; background-color: lightgrey; color: black; font-size:15px">${row.label}</td>
		                                <td style="width: 60%; background-color: lightgrey; color: black; font-size:15px">${row.value}</td>
		                            </tr>
		                        <#else>
		                            <tr>
		                                <td style="width: 40%; color: black; font-size:15px">${row.label}</td>
		                                <td style="width: 60%; font-size:15px; white-space: pre-wrap">${row.value}</td>
		                            </tr>
		                        </#if>
		                    <#else>
		                        <#if row.hasDifferentBackgroundColor>
		                            <tr>
		                                <td style="width: 40%; background-color: lightgrey; color: black; font-size:15px">${row.label}</td>
		                                <td style="width: 60%; background-color: lightgrey; color: black; font-size:15px">${row.value}</td>
		                            </tr>
		                        <#else>
		                            <tr>
		                                <td style="width: 40%; color: black; font-size:15px">${row.label}</td>
		                                <td style="width: 60%; color: black; font-size:15px; white-space: pre-wrap">${row.value}</td>
		                            </tr>
		                        </#if>
		                    </#if>
		                </#list>
		                </tbody>
		            </table>
		        </td>
		    </tr>
        </tbody>
	</table>
	
	<span style="page-break-before: always;"/>
</body>
</html>
