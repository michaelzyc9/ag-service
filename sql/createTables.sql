use apt_guide;

CREATE TABLE address
(
  id         		int NOT NULL AUTO_INCREMENT,
  street_number    	varchar(256),
  street			varchar(256),
  city              varchar(256),
  state             varchar(256),
  zip_code       	varchar(256),
  created 			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
  PRIMARY KEY (id)
);


CREATE TABLE apartment
(
  id				int  NOT NULL AUTO_INCREMENT,
  name 				varchar(256)  NOT NULL, 
  address_id 		int NOT NULL,
  website 			varchar(256),
  created 			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
  PRIMARY KEY (id),
  FOREIGN KEY (address_id) REFERENCES address(id)
);


CREATE TABLE floor_plan
(
  id				int	NOT NULL AUTO_INCREMENT,
  apt_id            int,
  bed   			float,
  bath				float,
  price_from		int,
  created 			TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (apt_id) REFERENCES apartment(id)
);


CREATE TABLE image
(
  id				int NOT NULL AUTO_INCREMENT,
  apt_id			int,
  link				varchar(256),
  description       varchar(256),
  created 			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
  PRIMARY KEY (id),
  FOREIGN KEY (apt_id) REFERENCES apartment(id)
);