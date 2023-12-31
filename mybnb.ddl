USE mybnb;

DROP TABLE IF EXISTS User, PersonalInfo, CreditCard, Listing, Availability, Rating,
Amenities, TheListings, PaymentMethod, Books, TheBookings, RenterRates, HostRates, LocationInfo;

CREATE TABLE PersonalInfo (
  sin VARCHAR(9) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  address VARCHAR(160) NOT NULL,
  birthdate DATE NOT NULL,
  occupation VARCHAR(70) NOT NULL
);

DELIMITER |
CREATE TRIGGER PersonalInfo_trigger BEFORE INSERT ON PersonalInfo
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.sin = '' THEN
    SET msg = 'Empty SIN violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.name = '' THEN
    SET msg = 'Empty name violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.address = '' THEN
    SET msg = 'Empty address violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.occupation = '' THEN
    SET msg = 'Empty occupation violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.birthdate > DATE_SUB(CURDATE(), INTERVAL 18 YEAR) THEN
    SET msg = 'Not 18+! violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE User (
  userID INT AUTO_INCREMENT PRIMARY KEY,
  sin VARCHAR(9),
  email VARCHAR(50),
  password VARCHAR(50),
  FOREIGN KEY (sin) REFERENCES PersonalInfo (sin) ON DELETE SET NULL,
  UNIQUE (email)
);

DELIMITER |
CREATE TRIGGER User_trigger BEFORE INSERT ON User
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.sin = '' THEN
    SET msg = 'Empty SIN violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.email = '' THEN
    SET msg = 'Empty email violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.password = '' THEN
    SET msg = 'Empty password violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE CreditCard (
  cardnumber VARCHAR(16) NOT NULL,
  expirydate DATE NOT NULL,
  holdername VARCHAR(100) NOT NULL,
  cardID INT AUTO_INCREMENT PRIMARY KEY
);

DELIMITER |
CREATE TRIGGER CreditCard_trigger BEFORE INSERT ON CreditCard
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.cardnumber = '' THEN
    SET msg = 'Empty cardnumber violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.holdername = '' THEN
    SET msg = 'Empty holdername violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.expirydate < CURDATE() THEN
    SET msg = 'Card expired violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE PaymentMethod (
  userID INT,
  cardID INT,
  PRIMARY KEY (userID, cardID),
  FOREIGN KEY (userID) REFERENCES User (userID),
  FOREIGN KEY (cardID) REFERENCES CreditCard (cardID) ON DELETE CASCADE
);

CREATE TABLE LocationInfo (
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  postalcode VARCHAR(10) NOT NULL,
  address VARCHAR(160) PRIMARY KEY,
  city VARCHAR(20) NOT NULL,
  country VARCHAR(30) NOT NULL
);

DELIMITER |
CREATE TRIGGER LocationInfo_trigger BEFORE INSERT ON LocationInfo
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.postalcode = '' THEN
    SET msg = 'Empty postalcode violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.address = '' THEN
    SET msg = 'Empty address violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.city = '' THEN
    SET msg = 'Empty city violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.country = '' THEN
    SET msg = 'Empty country violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE Listing (
  listingID INT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(30) NOT NULL,
  address VARCHAR(160),
  status VARCHAR(10) NOT NULL,
  FOREIGN KEY (address) REFERENCES LocationInfo (address)
);

DELIMITER |
CREATE TRIGGER Listing_trigger BEFORE INSERT ON Listing
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.type = '' THEN
    SET msg = 'Empty type violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.status = '' THEN
    SET msg = 'Empty status violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE Availability (
  listingID INT,
  date DATE,
  price DOUBLE NOT NULL,
  status VARCHAR(16) NOT NULL,
  PRIMARY KEY (listingID, date),
  FOREIGN KEY (listingID) REFERENCES Listing (listingID)
);

DELIMITER |
CREATE TRIGGER Availability_trigger BEFORE INSERT ON Availability
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.price < 0 THEN
    SET msg = 'Negative price violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.date < CURDATE() THEN
    SET msg = 'Date is in the past violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.status = '' THEN
    SET msg = 'Empty status violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE Amenities (
  name VARCHAR(60),
  listingID INT,
  PRIMARY KEY (name, listingID),
  FOREIGN KEY (listingID) REFERENCES Listing (listingID)
);

DELIMITER |
CREATE TRIGGER Amenities_trigger BEFORE INSERT ON Amenities
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.name = '' THEN
    SET msg = 'Empty amenity violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE Rating (
  ratingID INT AUTO_INCREMENT PRIMARY KEY,
  rating INT NOT NULL,
  comment TEXT NOT NULL
);

DELIMITER |
CREATE TRIGGER Rating_trigger BEFORE INSERT ON Rating
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.rating < 0 OR NEW.rating > 5 THEN
    SET msg = 'Invalid rating violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE TheListings (
  userID INT,
  listingID INT,
  PRIMARY KEY (userID, listingID),
  FOREIGN KEY (userID) REFERENCES User (userID),
  FOREIGN KEY (listingID) REFERENCES Listing (listingID)
);

CREATE TABLE Books (
  bookingID INT AUTO_INCREMENT PRIMARY KEY,
  listingID INT,
  startdate DATE NOT NULL,
  enddate DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  card VARCHAR(4) NOT NULL,
  date DATE NOT NULL,
  total DOUBLE NOT NULL,
  FOREIGN KEY (listingID) REFERENCES Listing (listingID)
);

DELIMITER |
CREATE TRIGGER Books_trigger BEFORE INSERT ON Books
FOR EACH ROW BEGIN
DECLARE msg VARCHAR(255);
  IF NEW.status = '' THEN
    SET msg = 'Empty status violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.startdate < CURDATE() THEN
    SET msg = 'Start Date is in the past violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.enddate < CURDATE() THEN
    SET msg = 'End Date is in the past violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  ELSEIF NEW.card = '' THEN
    SET msg = 'Empty last 4 card digit violate!';
    SIGNAL sqlstate '45000' set message_text = msg;
  END IF;
END;
|
DELIMITER ;

DELIMITER |
CREATE TRIGGER Update_Books_trigger AFTER INSERT ON Books
FOR EACH ROW BEGIN
  IF NEW.enddate < CURDATE() AND NEW.status = 'Booked' THEN
    UPDATE Books
    SET status = 'Completed'
    WHERE bookingID = NEW.bookingID;
  END IF;
END;
|
DELIMITER ;

CREATE TABLE TheBookings (
  userID INT,
  bookingID INT,
  PRIMARY KEY (userID, bookingID),
  FOREIGN KEY (userID) REFERENCES User (userID),
  FOREIGN KEY (bookingID) REFERENCES Books (bookingID)
);

CREATE TABLE RenterRates (
  ratingID INT PRIMARY KEY,
  renterID INT,
  hostID INT,
  listingID INT,
  FOREIGN KEY (renterID) REFERENCES User (userID),
  FOREIGN KEY (hostID) REFERENCES User (userID),
  FOREIGN KEY (listingID) REFERENCES Listing (listingID),
  FOREIGN KEY (ratingID) REFERENCES Rating (ratingID)
);

CREATE TABLE HostRates (
  ratingID INT PRIMARY KEY,
  renterID INT,
  hostID INT,
  FOREIGN KEY (renterID) REFERENCES User (userID),
  FOREIGN KEY (hostID) REFERENCES User (userID),
  FOREIGN KEY (ratingID) REFERENCES Rating (ratingID)
);
