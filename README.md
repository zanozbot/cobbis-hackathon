# Stocktaking Android Application

Solving problem of slow stocktaking with an Android application.
The application was developed during the Cobiss Hackathon 2019.

## Instructions

The goal of the hackathon is to develop an application that reads through a list of barcodes as quickly as possible.
The prototype needs to be developed for the Android platform. You can develop with the tools of your liking (Android Studio, Flutter, Java, Kotlin).

The application has to have one or two buttons on the home page that trigger the start and the end of stocktaking (e.g. **START** and **STOP** buttons). When the stocktaking begins the application needs to call the `REST ("/start")` endpoint. This endpoint also removes all of the previous attempts and resets the timer. Then, the camera starts scanning barcodes. For each barcode read, the `REST ("/scan/{number})` endpoint is called. The endpoint returns
- `OK` or `LOANED` (if the book is borrowed)
- `DUPLICATE` in case we already scanned the book
- `NOT_EXIST` in case of an error or if the scanned book doesn't exist in the inventory.

After the stocktaking is complete, the user must press the **STOP** button which is responsible for calling the `REST ("/stop")` endpoint. 
If all of the barcodes weren't scanned the server returns `NOT_ALL_READ` error. In that case, the user can continue scanning or reset the entire session by pressing the **START** button.

### Examples of used barcodes

* Code 93
    * 391234501
    * 999999999,9
* UPC A (remove the control digit)
    * 00000033116

### Server response structure

The server returns a JSON object that contains status and message.
Status is an integer with the following values.

* OK = 0;
* NOT_EXIST = 10; // scan
* DUPLICATE = 11; // scan
* LOANED = 12; // scan
* NOT_STARTED = 20; // scan
* NOT_ALL_READ = 21; // stop
* INVALID_APIKEY = 30;
* INVALID_TOKEN = 31;

## User requirements

As the librarian won't look at the screen during the stocktaking, an audio recording must be played when
- The barcode is successfully recognized and sent to the server `ok.wav`.
- The material is borrowed `borrowed.wav`.
- The server is down or the number of the barcode does not exist `error.wav`.

The recognized number should be displayed on the screen. Identifying and barcode reading must work **as fast as possible**. Your task is to find innovative methods or mechanisms to increase reading speed.
