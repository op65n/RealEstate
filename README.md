# RealEstate
RealEstate extension plugin for GriefPrevention.
<br>
Version: `1.0.0-Alpha` <br>
Native API: `Paper-1.16.4-R0.1-SNAPSHOT` <br>
Source Code: <a href="https://github.com/op65n/RealEstate">github.com/op65n/RealEstate</a> <br>
Developer: `Frcsty` <br>

## How To (Server Owner)
This is a plugin built on PaperAPI, and is required to properly run this plugin.

<b>Installation:</b> <br>
| Place RealEstate-VERSION.jar (`RealEstate-1.0.0-Alpha.jar`) file into the plugins folder. <br>
| Start the server, plugin will generate `RealEstate` directory with files:
 * `config.yml`
 * `sign-transactions.yml`
 * `message-queue.yml` <br>
 
| Stop the server after everything has been loaded. <br>
| Open and configure the plugin to your needs. <br>
| Start the server and enjoy the plugin!

## Events
The plugin provides two basic events, to allow for further customization <br>

 * `RealEstateSignCreateEvent` <br>
Which is fired when a user creates a valid RealEstate sign <br>
The event is caught by a listener, to allow for cancellation of the event in the future <br>
<br>
<br>
 * `RealEstateSignInteractEvent` <br>
Which is fired when a user interacts with a valid ReadEstate sign <br>
The event is caught by a listener, to allow for cancellation of the event in the future <br>

## Features
> Transaction Logging 

 - The plugin logs sign transactions into a file called `sign-transactions.yml` and provides all transaction 
 details within it.

> Message Queue

- The plugin provides a message queue, which ensures offline sellers are notified of sales,
upon logging in to the server.