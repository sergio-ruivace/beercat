INSERT INTO MANUFACTURER(name, nationality) VALUES('Heineken','Netherlands');
INSERT INTO MANUFACTURER(name, nationality) VALUES('Erdinger Weiss','Germany');
INSERT INTO MANUFACTURER(name, nationality) VALUES('Paulaner Brewery','Germany');

--password = 1234
INSERT INTO USER(email, password, manufacturer_id) VALUES('user@heineken.com', '$2a$12$KBVqTlvPv0KmtJo8F4yk3uaRaEQjB4ifEHYYSOASDboUuD.whdYd6', 1);
INSERT INTO USER(email, password, manufacturer_id) VALUES('user@erdinger.com', '$2a$12$KBVqTlvPv0KmtJo8F4yk3uaRaEQjB4ifEHYYSOASDboUuD.whdYd6', 2);
INSERT INTO USER(email, password) VALUES('user@beercat.com' , '$2a$12$KBVqTlvPv0KmtJo8F4yk3uaRaEQjB4ifEHYYSOASDboUuD.whdYd6');

INSERT INTO ROLE(name) VALUES('ADMIN');
INSERT INTO ROLE(name) VALUES('USER');

INSERT INTO USER_ROLES(user_id, roles_id) VALUES(1, 2);
INSERT INTO USER_ROLES(user_id, roles_id) VALUES(2, 2);
INSERT INTO USER_ROLES(user_id, roles_id) VALUES(3, 1);


INSERT INTO BEER(name, description, graduation, type, manufacturer_id, picture_url) VALUES('Heineken','Heineken Lager Beer', 5,'PALE_LAGER', 1, 'https://en.wikipedia.org/wiki/Heineken#/media/File:Heineken_Bottle.jpg');
INSERT INTO BEER(name, description, graduation, type, manufacturer_id, picture_url) VALUES('Heineken 0.0','Brewing a great tasting 0.0% alcohol free malt beverage', 0,'PALE_LAGER', 1, 'https://www.heineken.com/media-us/p0bazdia/heineken-00-bottle.png');
INSERT INTO BEER(name, description, graduation, type, manufacturer_id, picture_url) VALUES('Urweisse','A traditional wheat beer', 5.2,'WHEAT', 2, 'https://int.erdinger.de/bundles/erdingerwebsite/assets/int/urweisse/img/s2_bottle_blurred_lg.png');
INSERT INTO BEER(name, description, graduation, type, manufacturer_id, picture_url) VALUES('weissbier','The No. 1 Weissbier in Germany and one of the world’s favourites', 5.5,'WHEAT', 3, 'https://www.paulaner.com/content/1-our-products/1-weissbier/modules/1-product_intro-c8mu3l/bottle-int-1000x1550-hwbn.png');