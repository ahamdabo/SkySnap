# SkySnap
Is android app that helps citizen scientists and astronomy hobbiests to take notes regarding horizon and sky color.

The color of the sky provides us with valuable clues about its condition.
Learning about the significance of sky color will allow us to make educated guesses about the presence of natural haze and air pollution simply by looking out a window.

This project is an entry point to use a smartphone camera to 
-quantitatively assess sky color using sky color protocols.
-Pull in the nearest Aeronet (or similar) data.
-Present the sky color and Aeronet information together for further analysis.
-Allow users to compare the data from a single location in different times and locations.

As  theNear the horizon it is typically lighter due to the presence of aerosols. 
The darkest part of sky can often be seen about half way between the horizon and directly overhead, 
in the "anti- sun" direction - that is, when you look at the sky with your shadow in front of you. 
When observing Sky Color your should classify the darkest (bluest) color of the sky.

So it's advisable to capture the image with angle 45 degree above the horizon.

Validating the photo capture angle using sensors shall works as shown below.. 

![Alt text](https://s3.amazonaws.com/media-p.slid.es/uploads/saadzanfal/images/1262067/Screenshot_2015-04-12-12-33-02.png "AngleGuide")
![Alt text](https://s3.amazonaws.com/media-p.slid.es/uploads/saadzanfal/images/1262069/Screenshot_2015-04-12-12-32-47.png "AngleGuide")

-Capture the dominant color of the photo, coordinates and a timestamp as following.
![Alt text](https://s3.amazonaws.com/media-p.slid.es/uploads/saadzanfal/images/1262072/Screenshot_2015-04-12-12-32-13.png "CapturedPhoto")

The server side would provide data visualized in a way as follow
![Alt text](https://s3.amazonaws.com/media-p.slid.es/uploads/saadzanfal/images/1262089/Server.jpg "WebGL Globe")

