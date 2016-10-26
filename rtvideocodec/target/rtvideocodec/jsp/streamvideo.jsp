<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=320, initial-scale=1"/>
    <title>jsmpeg streaming</title>
    <style type="text/css">
        body {
            background: #333;
            text-align: center;
            margin-top: 10%;
        }
        #videoCanvas {
            /* Always stretch the canvas to 640x480, regardless of its
            internal size. */
            width: ${width}px; //640
            height: ${height}px;//480
        }
    </style>
</head>
<body>
<!-- The Canvas size specified here is the "initial" internal resolution. jsmpeg will
    change this internal resolution to whatever the source provides. The size the
    canvas is displayed on the website is dictated by the CSS style.
-->
<canvas id="videoCanvas" width="${width}" height="${height}">
    <p>
        Please use a browser that supports the Canvas Element, like
        <a href="http://www.google.com/chrome">Chrome</a>,
        <a href="http://www.mozilla.com/firefox/">Firefox</a>,
        <a href="http://www.apple.com/safari/">Safari</a> or Internet Explorer 10
    </p>
</canvas>
<!-- <script type="text/javascript" src="/springmvc/js/jsmpg.js"></script> -->
<script type="text/javascript" src="/rtvideocodec/js/jsmpg.js"></script>
<script type="text/javascript">
    // Setup the WebSocket connection and start the player weburl=ws://127.0.0.1:9094/
    var client = new WebSocket( "${weburl}" );
    var canvas = document.getElementById('videoCanvas');
    var player = new jsmpeg(client, {canvas:canvas});
</script>
</body>
</html>
