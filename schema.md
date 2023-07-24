## **Relational schema:**  
***User*** (<ins>userID</ins>, name, address, birthdate, occupation, type, SIN, email, password)  
***CreditCard*** (<ins>cardnumber</ins>, <ins>expiredate</ins>, holdername)  
***Listing*** (<ins>listingID</ins>, type, latitude, longitude, postalcode, address, city, country)  
***Availability*** (<ins>date</ins>, <ins>listingID</ins>, price)  
***Rating*** (<ins>ratingID</ins>, rating, comment)  
***Amenities*** (<ins>name</ins>, <ins>listingID</ins>)  
***TheListings*** (<ins>userID</ins>, <ins>listingID</ins>)  
***PaymentMethod*** (<ins>userID</ins>, <ins>cardnumber</ins>, <ins>expiredate</ins>)  
***TheBookings*** (<ins>listingID</ins>, <ins>date</ins>, <ins>userID</ins>)  
***RenterRates*** (<ins>renterID</ins>, <ins>ratingID</ins>, <ins>hostID</ins>, <ins>listingID</ins>)  
***HostRates*** (<ins>hostID</ins>, <ins>ratingID</ins>, <ins>renterID</ins>)

## **Assumptions Considered:**  
1. A renter account can have multiple payment method stored.
2. A single credit card can be saved by multiple renter accounts
3. The available amenities up-to-date are as follows:
  ```
  - Wifi
  - TV
  - Kitchen
  - Washer
  - Free parking on premises
  - Paid parking on premises
  - Air conditioning
  - Dedicated workspace
  - Pool
  - Hot tub
  - Patio
  - BBQ grill
  - Outdoor dining area
  - Fire pit
  - Pool table
  - Indoor fireplace
  - Piano
  - Exercise equipment
  - Lake access
  - Beach Access
  - ski-in/ski-out
  - Outdoor shower
  - Smoke alarm
  - First aid kit
  - Fire extinguisher 
  - Carbon monoxide alarm
  ```
4. The available room types up-to-date are as follows:
  ```
  - An entire place
  - A room
  - A shared room
  ```
5. Comments are part of the Rating
6. All host listing informations are valid locations.
