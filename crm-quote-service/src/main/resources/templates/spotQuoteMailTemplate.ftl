<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type"  content="text/html; charset=UTF-8" />
    <style>
        table {
            font-family: Arial; /* Bu fontu pdf'i oluştururken tanımlıyoruz. */
            font-style: normal;
            font-size: 12px;
        }
        img {
            display: block;
        }
        table.a4Page {
            margin: 0px;
            border: 0px solid red;
            padding: 0px;
            width: 100%;
            border-spacing: 0;
        }
        td.a4Page_cell {
            margin: 0px;
            border: 0px solid yellow;
            padding: 0px;
        }
        td.detailsTable_initialRow_cell {
            border-top: 1px solid black;
            border-left: 1px solid black;
            border-bottom: 1px solid black;
            padding: 5px;
        }
        td.detailsTable_initialRow_rightMostCell {
            border: 1px solid black;
            padding: 5px;
        }
        td.detailsTable_nonInitialRow_cell {
            border-left: 1px solid black;
            border-bottom: 1px solid black;
            padding: 5px;
        }
        td.detailsTable_nonInitialRow_rightMostCell {
            border-left: 1px solid black;
            border-bottom: 1px solid black;
            border-right: 1px solid black;
            padding: 5px;
        }
        .externalHtmlContainer {
            line-height: 1.5;
            text-align: justify;
        }
        .externalHtmlContainer > * { /* externalHtmlContainer'ın tüm ilk seviye çocukları... */
            padding-bottom: 8px;
        }
    </style>
</head>
<body>

<table class="a4Page">
	<tbody>	
	    <tr>
	        <td class="a4Page_cell" style="padding-top: 10mm;">
	            <table style="width: 100%;">
					<tbody>	
		                <tr>
		                    <td style="width: 12%;"><b>${quoteNumberLabel}</b></td>
		                    <td style="width: 1%;"><b>:</b></td>
		                    <td>${quoteNumber}</td>
		                </tr>
		                <tr>
		                    <td style="width: 12%;"><b>${quadroLabel}</b></td>
		                    <td style="width: 1%;"><b>:</b></td>
		                    <td>${quadro}</td>
		                </tr>
                        <tr>
                            <td style="width: 12%;"><b>${createdByLabel}</b></td>
                            <td style="width: 1%;"><b>:</b></td>
                            <td>${createdBy}</td>
                        </tr>
		                <tr>
		                    <td style="width: 12%;"><b>${accountLabel}</b></td>
		                    <td style="width: 1%;"><b>:</b></td>
		                    <td>${account}</td>
		                </tr>
		                <tr>
		                    <td style="width: 12%;"><b>${accountLocationLabel}</b></td>
		                    <td style="width: 1%;"><b>:</b></td>
		                    <td>${accountLocation}</td>
		                </tr>
					</tbody>	
	            </table>
	        </td>
	    </tr>
	</tbody>	
</table>
<br>
<br>

<span style="page-break-before: always;"/>

<table class="a4Page">
	<tbody>
	    <tr>
	        <td class="a4Page_cell">
	            <p><b>${detailsTableLabel}</b></p>
	            <table style="padding: 0px; border-spacing: 0; width: 60%;">
					<tbody>
	                <#list detailsTableRows as row>
	                    <#if row?index == 0>
	                        <#if row.hasDifferentBackgroundColor>
	                            <tr>
	                                <td class="detailsTable_initialRow_cell" style="width: 40%; background-color: lightgrey;">${row.label}</td>
	                                <td class="detailsTable_initialRow_rightMostCell" style="width: 60%; background-color: lightgrey;">${row.value}</td>
	                            </tr>
	                        <#else>
	                            <tr>
	                            	<#if row.label?has_content>
	                               		<td class="detailsTable_initialRow_cell" style="width: 40%;">${row.label}</td>
	                                	<td class="detailsTable_initialRow_rightMostCell" style="width: 60%;">${row.value}</td>
	                                <#else>
	                                	<td colspan="2" class="detailsTable_initialRow_cell detailsTable_nonInitialRow_rightMostCell">${row.value}</td>
	                           		</#if>
	                            </tr>
	                        </#if>
	                    <#else>
	                        <#if row.hasDifferentBackgroundColor>
	                            <tr>
	                                <td class="detailsTable_nonInitialRow_cell" style="width: 40%; background-color: lightgrey;">${row.label}</td>
	                                <td class="detailsTable_nonInitialRow_rightMostCell" style="width: 60%; background-color: lightgrey;">${row.value}</td>
	                            </tr>
	                        <#else>
	                            <tr>
	                            	<#if row.label?has_content>
	                                	<td class="detailsTable_nonInitialRow_cell" style="width: 40%;">${row.label}</td>
	                                	<td class="detailsTable_nonInitialRow_rightMostCell" style="width: 60%;">${row.value}</td>
	                                <#else>
	                                	<td colspan="2" class="detailsTable_initialRow_cell detailsTable_nonInitialRow_rightMostCell">${row.value}</td>
	                           		</#if>
	                            </tr>
	                        </#if>
	                    </#if>
	                </#list>
					</tbody>
	            </table>
	        </td>
	    </tr>
	    <tr>
            <td class="a4Page_cell">
                <br />
                <div class="externalHtmlContainer">${specialNotes}</div>
            </td>
        </tr>
	</tbody>	
</table>

<span style="page-break-before: always;"/>


</body>
</html>
