
In the application I have used pixabay to GET the images from the internet.
When you open the application, a splash screen is shown during which the API is hit and the images are loaded in the background,
after a short period, the splash screen fades and the gallery is shown.

Functionalities:
When you long press an image, it is selected. you can see which image is selected by a change in transparency of the image. When a single
image has been selected, a simple tap will add other images to the selected list and a tap on a selected image will remove it from
the selected list. when an image has been selected, an option to select all and delete is shown which deletes the selected images
and selects all images respectively.

when no Image is selected, a single tap will open the image. we also have a button for deleting that specific image when the image is open.
swiping right or left will move the image to the next or previous image in the list.

Pulling the screen from the top will hit the API again and more images will be added to the list.

Architecture:
I have used MVC architecture since the application consisted of a very limited number of screens and the scope of the application was rather
small.
