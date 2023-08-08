## **Relational schema:**  
***User*** (<ins>userID</ins>, name, address, birthdate, occupation, sin, email, password)  
***CreditCard*** (<ins>cardID</ins>, cardnumber, expirydate, holdername)  
***Listing*** (<ins>listingID</ins>, type, latitude, longitude, postalcode, address, city, country, status)  
***Availability*** (<ins>date</ins>, <ins>listingID</ins>, price, status)  
***Rating*** (<ins>ratingID</ins>, rating, comment)  
***Amenities*** (<ins>name</ins>, <ins>listingID</ins>)  
***TheListings*** (<ins>userID</ins>, <ins>listingID</ins>)  
***PaymentMethod*** (<ins>userID</ins>, <ins>cardID</ins>)  
***Books*** (<ins>bookingID</ins>, <ins>listingID</ins>, startdate, enddate, status, card, date, total)  
***TheBookings*** (<ins>bookingID</ins>, <ins>userID</ins>)  
***RenterRates*** (<ins>renterID</ins>, <ins>ratingID</ins>, <ins>hostID</ins>, <ins>listingID</ins>)  
***HostRates*** (<ins>hostID</ins>, <ins>ratingID</ins>, <ins>renterID</ins>)

## **Assumptions Considered:**  
1. A renter account can have multiple payment method stored.
2. A single credit card can be saved by multiple renter accounts
3. The available types are as follows:
  ```
  - Apartment 
  - House
  - Condo
  ```
4. Comments are part of the Rating
5. All host listing informations are valid locations.
6. For the reports of the number of bookings in a specific time period, bookings are included inside the time period whenever it has a date inside the period.
7. Address for a specific listing also includes the floor number if its an appartment.
8. Each booking is paid with a single card.

## Normalization - 3NF
***User*** (<ins>userID</ins>, name, address, birthdate, occupation, SIN, email, password)  
Decomposes into:  
  - ***User*** (<ins>userID</ins>, sin, email, password)
  - ***PersonalInfo*** (<ins>sin</ins>, name, address, birthdate, occupation)  

***CreditCard*** (<ins>cardID</ins>, cardnumber, expirydate, holdername)  
Stays unchanged.

***Listing*** (<ins>listingID</ins>, type, latitude, longitude, postalcode, address, city, country)  
Decomposes into:
  - ***Listing*** (<ins>listingID</ins>, type, address, status)  
  - ***LocationInfo*** (latitude, longitude, postalcode, <ins>address</ins>, city, country)  

***Availability*** (<ins>date</ins>, <ins>listingID</ins>, price, status)  
Stays unchanged.

***Rating*** (<ins>ratingID</ins>, rating, comment)  
Stays unchanged.

***Amenities*** (<ins>name</ins>, <ins>listingID</ins>)  
Stays unchanged.

***TheListings*** (<ins>userID</ins>, <ins>listingID</ins>)  
Stays unchanged.

***PaymentMethod*** (<ins>userID</ins>, <ins>cardID</ins>)  
Stays unchanged.

***TheBookings*** (<ins>userID</ins>, <ins>bookingID</ins>)  
Stays unchanged.

***Books*** (<ins>bookingID</ins>, <ins>listingID</ins>, startdate, enddate, status, card, date, total)  
Stays unchanged.

***RenterRates*** (<ins>renterID</ins>, <ins>ratingID</ins>, <ins>hostID</ins>, <ins>listingID</ins>)  
Stays unchanged.

***HostRates*** (<ins>hostID</ins>, <ins>ratingID</ins>, <ins>renterID</ins>)  
Stays unchanged.