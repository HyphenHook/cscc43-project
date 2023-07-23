## **Relational schema:**  
***User*** (<u>userID</u>, name, address, birthdate, occupation, type, SIN, email, password)  
***CreditCard*** (<u>cardnumber</u>, <u>expiredate</u>, holdername)  
***Listing*** (<u>listingID</u>, type, latitude, longitude, postalcode, address, city, country)  
***Amenity*** (<u>name</u>)  
***Availability*** (<u>date</u>, <u>listingID</u>, price)  
***Rating*** (<u>ratingID</u>, rating, comment)  
***Amenities*** (<u>name</u>, <u>listingID</u>)  
***TheListings*** (<u>userID</u>, <u>listingID</u>)  
***PaymentMethod*** (<u>userID</u>, <u>cardnumber</u>, <u>expiredate</u>)  
***TheBookings*** (<u>listingID</u>, <u>date</u>, <u>userID</u>)  
***RenterRates*** (<u>renterID</u>, <u>ratingID</u>, <u>hostID</u>, <u>listingID</u>)  
***HostRates*** (<u>hostID</u>, <u>ratingID</u>, <u>renterID</u>)

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